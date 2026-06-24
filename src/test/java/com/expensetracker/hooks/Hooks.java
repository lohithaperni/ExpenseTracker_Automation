package com.expensetracker.hooks;

import com.expensetracker.config.PlaywrightFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import com.microsoft.playwright.Page;

public class Hooks {
  private static PlaywrightFactory factory;
  private static Page page;

  @before
  public void before(Scenario scenario) {
    factory = new PlaywrightFactory();
    factory.start();
    page = factory.page();
  }

  @After
  public void after(Scenario scenario) {
    try {
      if (scenario.isFailed() && page != null) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        scenario.attach(screenshot, "image/png", "failure-screenshot");
      }
    } finally {
      if (factory != null) factory.stop();
    }
  }

  public static Page getPage() {
    return page;
  }
}
