package com.expensetracker.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

import java.util.Optional;

public class PlaywrightFactory {
  private Playwright playwright;
  privat Browser browser;
  private BrowserContext context;
  private Page page;

  public void start() {
    playwright = Playwright.create();

    String browserName = Optional.ofNullable(System.getProperty("browser")).orElse("chromium");
    boolean headless = Boolean.parseBoolean(Optional.ofNullable(System.getProperty("headless")).orElse("true"));

    BrowserType browserType = switch (browserName.toLowerCase()) {
      case "firefox" -> playwright.firefox();
      case "webkit" -> playwright.webKit();
      default -> playwright.chromium();
    };

    browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless));

    context = browser.newContext(new Browser.NewContextOptions());
    page = context.newPage();
  }

  public Page page() {
    return page;
  }

  public void stop() {
    if (context != null) context.close();
    if (browser != null) browser.close();
    if (playwright != null) playwright.close();
  }
}
