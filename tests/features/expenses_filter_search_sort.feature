Feature: Filtering, Search, and default sorting for Expenses

  Background:
    Given the Expense Tracker app is running
    And I am on the "Expenses" page

  Scenario: Filter expenses by start date and end date
    Given there are expenses on multiple dates
    When I set the Start Date filter to "2025-01-01"
    And I set the End Date filter to "2025-01-31"
    And I apply the filters
    Then I should see only expenses with date between "2025-01-01" and "2025-01-31" inclusive
    And the Total Spent should equal the sum of the visible expenses

  Scenario: Filter expenses by only start date
    Given there are expenses on multiple dates
    When I set the Start Date filter to "2025-01-15"
    And I apply the filters
    Then I should see only expenses with date on or after "2025-01-15"

  Scenario: Filter expenses by only end date
    Given there are expenses on multiple dates
    When I set the End Date filter to "2025-01-10"
    And I apply the filters
    Then I should see only expenses with date on or before "2025-01-10"

  Scenario: Filter expenses by category
    Given there are expenses with multiple categories
    When I select the category filter as "Food"
    And I apply the filters
    Then I should see only expenses with category "Food"

  Scenario: Applying category "All" shows all expenses
    Given there are expenses with multiple categories
    When I select the category filter as "All"
    And I apply the filters
    Then I should see expenses from all categories

  Scenario: Clear filters resets the list
    Given there are expenses with multiple categories
    And I have applied a category filter "Food"
    When I clear the filters
    Then I should see expenses from all categories

  Scenario: Search expenses by note text is case-insensitive
    Given there are expenses with different note texts
    When I search notes for "lunch"
    And I apply the filters
    Then I should see only expenses with notes containing "lunch" case-insensitively

  Scenario: Search for a term that has no matches shows filtered empty state
    Given there are expenses in the system
    When I search notes for "noKeyword"
    And I apply the filters
    Then I should see an empty state message "No matching expenses."

  Scenario: Combine date range, category, and search
    Given there are expenses with various dates, categories, and notes
    When I set the Start Date filter to "2025-01-01"
    And I set the End Date filter to "2025-01-31"
    And I select the category filter as "Transport"
    And I search notes for "bus"
    And I apply the filters
    Then I should see only expenses that match all applied filters

  Scenario: Default list is newest first by date then id (date DESC, id DESC)
    Given there are expenses on different dates including two on "2025-01-20"
    When I open the "Expenses" page
    Then the first expense in the table should have the most recent date
    And for date "2025-01-20" the expense created last should appear first

  Scenario: Invalid start_date query param is ignored and does not break the page
    Given there are expenses in the system
    When I open the Expenses page with query parameters "start_date=not-a-date"
    Then the expense list should still load
    And the Start Date filter value should be empty

  Scenario: Invalid end_date query param is ignored and does not break the page
    Given there are expenses in the system
    When I open the Expenses page with query parameters "end_date=not-a-date"
    Then the expense list should still load
    And the End Date filter value should be empty

  Scenario: Empty state message for no expenses without filters
    Given there are no expenses in the system
    When I open the "Expenses" page
    Then I should see an empty state message "No expenses yet. Add your first one above."
