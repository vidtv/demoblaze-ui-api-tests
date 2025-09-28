Test project for UI and API automation built with **Java**, **Playwright**, **Rest Assured**, **JUnit5**, **Maven**, 
**Allure Reporting**, and **GitHub Actions CI/CD**.
 
## Features
 
- **Java** for test scripting
- **Playwright** for browser automation
- **JUnit5** for test execution and reporting
- **Rest Assured** for API testing
- **Allure Reporting** for detailed test reports
- **GitHub Actions** for CI/CD automation
- **Maven** for dependency management

## Setup & Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/vidtv/demoblaze-ui-api-tests.git
    cd demoblaze-ui-api-tests
    ```
   
2. Install dependencies using Maven:
   ```bash
   mvn clean install
   ```
   
3. Ensure you have Java (JDK 11 or higher) and Maven installed on your machine.
4. Ensure Playwright dependencies are installed (e.g., npx playwright install or Maven plugin).

## Running Tests
To run all tests:
- ```bash
   mvn test
   ```
  
## Generating Allure Reports
After running tests, generate the Allure report using:
```bash
   mvn allure:serve
   ```
This will start a local server and open the report in your default web browser.

## CI/CD with GitHub Actions
- The project is configured to run tests automatically on each pull request to the repository using GitHub Actions CI/CD.
- You can find the workflow configuration in the `.github/workflows` directory.

## Live Test Report
- A live test report can be accessed [here](https://vidtv.github.io/demoblaze-ui-api-tests/).