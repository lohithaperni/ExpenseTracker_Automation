Fileting Expenses: Filtering, Search, and Sorting

  Background:
    Given the Expense Tracker app is running
    And I am on the "Expenses" page

  # -------------------------------------------------------
  # Happy path: date range filter
  # --------------------------------------------------------
  Scenario: Filter expenses by start date and end date
    Given there are expenses enisted on multiple dates
    When I set the Start Date filter to "2025-01-01"
    And I set the End Date filter to "2025-01-31"
    And I apply the filters
    Then I should see only expenses with date between "2025-01-01" and "2025-01-31" (inclusive)
    And the Total Spent should equal the sum of the visible expenses

  Scenario: Filter expenses by only start date
    Given there are expenses enisted on multiple dates
    When I set the Start Date filter to "2025-01-15"
    And I apply the filters
    Then I should see only expenses with date on or after "2025-01-15"

  Scenario: Filter expenses by only end date
    Given there are expenses enisted on multiple dates
    When I set the End Date filter to "2025-01-10"
    And I apply the filters
    Then I should see only expenses with date on or before "2025-01-10"

  # --------------------------------------------------------
  # Happy path: category filter
  # --------------------------------------------------------
  Scenario: Filter expenses by category
    Given there are expenses with multiple categories
    When I select the category filter as "Food"
    And I apply the filters
    Then I should see only expenses with category "Food"

  Scenario: Clear filters resets the list
    Given I have applied a category filter
    When I clear the filters
    Then I should see all expenses again

  Scenario: Applying category "All" shows all expenses
    Given there are expenses with multiple categories
    When I select the category filter as "All"
    And I apply the filters
    Then I should see expenses from all categories

  # --------------------------------------------------------
  # Happy path: search by note
  # -------------------------------------------------------
  Scenario: Search expenses by note text (case-insensitive)
    Given there are expenses with different note texts
    When I search notes for "lunch"
    And I apply the filters
    Then I should see only expenses with notes containing "lunch"
    And I should see expenses with notes containing "LUNCH"

  Scenario: Search for a term that has no matches
    Given there are expenses enisted
    When I search notes for "noKewword"
    And I apply the filters
    Then I should see an empty state message "No matching expenses."

  # -------------------------------------------------------
  # Behavior: combined filters (date + category + search)
  # --------------------------------------------------------
  Scenario: Combine date range, category, and search
    Given there are expenses with various dates, categories, and notes
    When I set the Start Date filter to "2025-01-01"
    And I set the End Date filter to "2025-01-31"
    And I select the category filter as "Transport"
    And I search notes for "bus"
    And I apply the filters
    Then I should see only expenses that match all applied filters

  Scenario: Clear after combined filters restores the default list
    Given I have applied multiple filters and search
    When I clear the filters
    Then I should see the default expense list

  # --------------------------------------------------------
  # Sorting: default order verification (backend)
  # --------------------------------------------------------
  Scenario: Default list is dae-new first (date DESC, id DESC)
    Given there are expenses on different dates
    When I open the "Expenses" page
    Then the first expense in the table should be the most recent date
    And if two expenses have the same date, the one with the higher id should appear first

  # -------------------------------------------------------
  # Error handling / edge cases
  # -------------------------------------------------------
  Scenario: Invalid start date does not break the page and is ignored
    Given there ar expenses enisted
    When I enter an invalid Start Date value
    And I apply the filters
    Then the expense list should still load
    And the Start Date filter value should be cleared

  Scenario: Invalid end date does not break the page and is ignored
    Given there ar expenses enisted
    When I enter an invalid End Date value
    And I apply the filters
    Then the expense list should still load
    And the End Date filter value should be cleared

  Scenario: Empty state message for no expenses without filters
    Given there are no expenses in the system
    When I open the "Expenses" page
    Then I should see an empty state message "No expenses yet. Add your first one above."
