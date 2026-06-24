package com.expensetracker.config;

import java.util.Optional;

public class TestConfig {
  private TestConfig() {}

  public static String baseUrl() {
    return Optional.ofNullable(System.getProperty("baseUrl")).orElse("http://127.0.0.1:5000");
  }
}
