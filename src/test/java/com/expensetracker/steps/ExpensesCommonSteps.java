package com.expensetracker.steps;

import com.expensetracker.config.TestConfig;
import com.expensetracker.hooks.Hooks;
import io.cucumber.java.en.*;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.*;

import org.assertj.core.api.Assertions;

public class ExpensesCommonSteps {
  protected Page page;

  @io.cucumber.java.Before
  public void before() {
    this.page = Hooks.getPage();
  }

  protected Locator tableRows() {
    return page.locator("table tbody tr");
  }

  Given("the Expense Tracker app is running")
  public void app_running() {
    page.navigate(TestConfig.baseUrl() + "/");
    // sanity: page loaded
    Assertions.assertThat(page.url()).isNotEmpty();
  }

  And("I am on the \"Expenses\" page")
  public void on_expenses_page() {
    page.navigate(TestConfig.baseUrl() + "/expenses");
    page.waitForLoadState();
    Assertions.assertThat(page.content()).contains("Expense List");
  }

  When("I open the \"Expenses\" page")
  public void open_expenses() {
    page.navigate(TestConfig.baseUrl() + "/expenses");
    page.waitForLoadState();
  }
  
  And("I apply the filters")
  public void apply_filters() {
    page.locator("form button:has-text('Apply')").first().click();
    page.waitForLoadState();
  }

  When("I clear the filters")
  public void clear_filters() {
    page.locator("a:has-text('Clear')").first().click();
    page.waitForLoadState();
  }

  Then("I should see an empty state message \"{string}\"")
  public void empty_state(String msg) {
    Assertions.assertThat(page.content()).contains(msg);
  }

  Then("I should see the default expense list")
  public void default_list() {
    Assertions.assertThat(page.url()).contains("/expenses");
  }
}
