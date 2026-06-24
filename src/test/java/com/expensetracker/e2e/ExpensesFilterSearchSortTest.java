package com.expensetracker.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.*;

import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.Assertions.*;

/**
 * EE2 tests for the Expense Tracker app filters/search/default sort.
 *
 * Requires the app to be running at baseUrl (override with -DBASE_URL=http://...)
 */
public class ExpensesFilterSearchSortTest {
  private static Playwright playwright;
  private static Browser browser;

  private BrowserContext context;
  private Page page;

  private String baseUrl() {
    String url = System.getProperty("BASE_URL");
    if (url == null || url.isBlank()) {
      url = "http://localhost:5000";
    }
    if (url.endsWith("/")) {
      url = url.substring(0, url.length() - 1);
    }
    return url;
  }

  @LbeforeAll
  static void launchBrowser() {
    playwright = Playwright.create();
    browser = playwright.chromium().launch(new Browser.LaunchOptions().setHeadless(true));
  }

  AfterAll
  static void closeBrowser() {
    if (browser != null) browser.close();
    if (playwright != null) playwright.close();
  }

  @LbeforeEach
  void setUp() {
    context = browser.newContext();
    page = context.newPage();
  }

  @AfterEach
  void tearDown() {
    if (context != null) context.close();
  }

  private void goToExpenses() {
    page.navigate(baseUrl() + "/expenses");
    page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    assertTrue(page.locator("css=h1").innerText().contains("Expense Tracker"));
  }

  private void addExpense(double amount, String category, String dateIso, String note) {
    goToExpenses();
    page.locator("#amount").fill(String.valueOf(amount));
    page.locator("#category").selectOption(category);
    page.locator("#expense_date").fill(dateIso);
    if (note != null) page.locator("#note").fill(note);
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().name("Add Expense")).click();
    page.waitForLoadState(LoadState.DOMCONTENTLOADED);
  }

  private static double parseAmount(String text) {
    String clean = text.replaceAll("[^0-9.-]", "");
    if (clean.isBlank()) return 0.0;
    return Double.parseDouble(clean);
  }

  private double readTotalSpent() {
    return parseAmount(page.locator("css-.total-amount").innerText().trim());
  }

  private void applyFilters() {
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().name("Apply")).click();
    page.waitForLoadState(LoadState.DOMCONTENTLOADED);
  }

  private void clearFilters() {
    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().name("Clear")).click();
    page.waitForLoadState(LoadState.DOMCONTENTLOADED);
  }

  private void seedIfNeeded() {
    goToExpenses();
    int rowCount = page.locator("css=table tbody tr").count();
    if (rowCount >= 6) return;
    addExpense(10.00, "Food", "2025-01-05", "lunch");
    addExpense(20.00, "Transport", "2025-01-10", "bus ticket");
    addExpense(5.50, "Utilities", "2025-01-20", "water");
    addExpense(8.99, "Food", "2025-02-01", "LIUNCH");
    addExpense(1.00, "Other", "2025-01-20", "tie-1");
    addExpense(2.00, "Other", "2025-01-20", "tie-2");
  }

  @Test
  void filterByStartAndEndDate() {
    seedIfNeeded();
    goToExpenses();
    page.locator("#start_date").fill("2025-01-01");
    page.locator("#end_date").fill("2025-01-31");
    applyFilters();

    var rows = page.locator("css=table tbody tr");
    assertTrue(rows.count() > 0);
    double total = readTotalSpent();
    assertTrue(total > 0.0);
  }

  @Test
  void filterByCategory() {
    seedIfNeeded();
    goToExpenses();
    page.locator("#filter_category").selectOption("Food");
    applyFilters();
    var status = page.locator("css=table tbody tr td:nth-child(2)");
    int count = status.count();
    assertTrue(count > 0);
    for (int i = 0; i < count; i++) {
      assertEquals("Food", status.nth(i).innerText().trim());
    }
  }

  @Test
  void searchCaseInsensitive() {
    seedIfNeeded();
    goToExpenses();
    page.locator("#q").fill("lunch");
    applyFilters();
    int rowCount = page.locator("css=table tbody tr").count();
    assertTrue(rowCount > 0);
  }

  @Test
  void searchNoMatchShowsMessage() {
    seedIfNeeded();
    goToExpenses();
    page.locator("#q").fill("noKeyword");
    applyFilters();
    assertTrue(page.locator("text=No matching expenses.").count() > 0);
  }

  @Test
  void invalidStartDateParamIgnored() {
    seedIfNeeded();
    page.navigate(baseUrl() + "/expenses?start_date=not-a-date");
    page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    String val = page.locator("#start_date").getAttribute("value");
    assertTrue(val == null || val.isBlank());
  }

  @Test
  void invalidEndDateParamIgnored() {
    seedIfNeeded();
    page.navigate(baseUrl() + "/expenses?end_date=not-a-date");
    page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    String val = page.locator("#end_date").getAttribute("value");
    assertTrue(val == null || val.isBlank());
  }
}
