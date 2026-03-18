# 🚀 Cucumber BDD Automation Framework (Selenium + TestNG)

A scalable and maintainable **UI Automation Framework** built using **Selenium, Cucumber (BDD), and TestNG**.
Designed with industry best practices like **Page Object Model (POM)**, **data-driven testing**, **parallel execution**, and **rich reporting**.

---

## 📌 Tech Stack

* **Language:** Java
* **Automation Tool:** Selenium WebDriver
* **BDD Framework:** Cucumber
* **Test Runner:** TestNG
* **Build Tool:** Maven
* **Reporting:** Extent Reports
* **Design Pattern:** Page Object Model (POM)
* **Data Handling:** Config + JSON (TestDataReader)
* **Version Control:** Git

---

## 🏗️ Framework Architecture

![Framework Architecture](docs/images/framework-architecture.png)

---

## 📊 Sample Extent Report

Below is a sample execution report generated after test run:

![Extent Report](docs/images/extent-report.png)

---

## 🧪 Sample Test Execution Output

```bash
[INFO] Running Cucumber Test Suite

Scenario: Successful login with valid credentials  ✔ PASSED
Scenario: Invalid login attempt                    ✔ PASSED
Scenario: Checkout validation                     ✔ PASSED
Scenario: Product sorting                         ✔ PASSED

===========================================
Tests run: 4, Failures: 0, Skipped: 0
===========================================

BUILD SUCCESS
```

---

## 📂 Project Structure

```
Cucumber_BDD/
│
├── src/main/java/com/cucumberbdd/pageobjects
│   ├── BasePage.java
│   ├── LoginPage.java
│   ├── HomePage.java
│   ├── CheckoutPage.java
│   └── WaitUtils.java
│
├── src/test/java
│   ├── base
│   ├── hooks
│   ├── runner
│   ├── stepDefinitions
│   └── util
│
├── src/test/resources
│   ├── features
│   ├── config.properties
│   └── testdata.json
│
├── pom.xml
├── testng.xml
└── README.md
```

---

## ▶️ How to Run Tests

### 🔹 Using Maven

```bash
mvn clean test
```

### 🔹 Using TestNG

* Right-click `testng.xml`
* Run as → **TestNG Suite**

---

## ⚡ Parallel Execution

Framework supports **scenario-level parallel execution** using:

* `@DataProvider(parallel = true)`
* `testng.xml` thread configuration

---

## 🔁 Retry Mechanism

Failed test cases are automatically retried using:

* `RetryAnalyzer`
* `RetryTransformer`

---

## 📸 Screenshot on Failure

* Screenshots are captured automatically on failure
* Attached in Extent Report

---

## 📦 Test Data Management

| Environment Data 	-> `config.properties` |
| Test Data        	-> `testdata.json`     |

---

## 📌 Sample Scenarios Covered

* Login with valid credentials
* Login with invalid credentials
* Product validation
* Checkout flow validation
* Sorting functionality validation

---

## 🧠 Design Highlights

* Thread-safe WebDriver using `ThreadLocal`
* Clean separation of layers (BDD → Steps → POM → Utils)
* Reusable utilities and centralized configuration
* Extensible and scalable structure

---

## 🚀 Future Enhancements

* CI/CD integration (Jenkins / GitHub Actions)
* Docker execution
* API + UI integration
* Advanced reporting dashboard

---

## 👨‍💻 Author

**Prasanth M**
QA Automation Engineer | Selenium | API Testing | Framework Design

---

## ⭐ If you like this project

Give it a ⭐ on GitHub and feel free to contribute!
