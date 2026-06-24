# ExpenseTracker_Automation

Playwright (Java) E2E automation for **ExpenseTracker**.

## Prequisites
- Java 17+
- Maven 3.8+
- Running app: default expects http://localhost:5000

## Run tests
1. Start the app
   - from `lohithaperni/ExpenseTracker` repo, run: `python app.py` (or however you start the Flask app)

2. Install browsers + run tests:
   ```bash
   mvn -q -DBASE_URL=http://localhost:5000 test
   ```

Note: Browsers are installed via the Playwright Maven plugin at test-compile phase.
