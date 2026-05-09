# Unified Automation Framework

![CI](https://github.com/sumit-qa-automation/unified-automation-framework/actions/workflows/maven.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-17-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.27.0-green)
![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red)
![Maven](https://img.shields.io/badge/Maven-Build-blue)
![GitHub Actions](https://img.shields.io/badge/CI/CD-GitHub_Actions-black)
![API Testing](https://img.shields.io/badge/API-REST_Assured-brightgreen)

Enterprise-grade Unified Automation Framework built using Selenium WebDriver, TestNG, REST Assured, Java, and Maven with integrated CI/CD pipeline using GitHub Actions.

---

# Framework Overview

This framework is designed to support scalable and maintainable automation testing for both Web and API applications using industry-standard tools and best practices.

The framework supports:

- Web Automation using Selenium WebDriver
- API Automation using REST Assured
- TestNG Framework Integration
- Maven Build Management
- GitHub Actions CI/CD Integration
- Parallel Execution Support
- Headless Browser Execution
- Smoke & Regression Suite Execution
- Data-Driven Testing using Excel
- Extent Reporting with Screenshots
- Nightly Scheduled Regression Execution
- Cloud-Based Test Execution

---

# Key Features

- Selenium WebDriver UI Automation
- REST Assured API Automation
- Page Object Model (POM) Design Pattern
- Parallel Test Execution
- Data-Driven Testing using Excel
- Extent Reports with Screenshots
- Maven Build Lifecycle Integration
- GitHub Actions CI/CD Integration
- Smoke and Regression Suite Separation
- Nightly Scheduled Regression Execution
- Headless Chrome Execution
- Logging using Log4j2
- Modular and Scalable Framework Design
- Recruiter-Friendly Framework Architecture

---

# Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming Language |
| Selenium WebDriver | UI Automation |
| TestNG | Test Execution Framework |
| REST Assured | API Automation |
| Maven | Build Management |
| GitHub Actions | CI/CD |
| Extent Reports | Reporting |
| Apache POI | Excel Handling |
| Log4j2 | Logging |
| WebDriverManager | Driver Management |

---

# Project Structure

```text
unified-automation-framework
│
├── .github/workflows
│   └── maven.yml
│
├── src/main/java
│   ├── actions
│   ├── core
│   ├── listeners
│   ├── pages
│   └── utilities
│
├── src/main/resources
│   ├── config.properties
│   └── log4j2.xml
│
├── src/test/java
│   ├── api/tests
│   └── web/tests
│
├── src/test/resources
│   ├── testdata
│   ├── testng-smoke.xml
│   └── testng-regression.xml
│
├── test-output
│
├── pom.xml
│
└── README.md
```

---

# Framework Architecture

The framework follows a layered architecture:

```text
Test Layer
    ↓
Page Object Layer
    ↓
Action Layer
    ↓
Core Driver Layer
    ↓
Utilities & Reporting Layer
```

---

# Test Suite Strategy

## Smoke Suite

Smoke suite executes critical validation scenarios.

Includes:
- Dummy Test
- Login Validation Tests

Triggered on:
- Push Events
- Pull Requests
- Manual Workflow Execution

---

## Regression Suite

Regression suite executes complete automation coverage.

Includes:
- UI Automation Tests
- API Automation Tests
- Homepage Validation
- Login Validation
- Full Regression Coverage

Triggered on:
- Nightly Scheduled Execution

---

# CI/CD Pipeline

The framework uses GitHub Actions for cloud-based automated execution.

## Pipeline Flow

```text
Code Push / Pull Request
            ↓
GitHub Actions Trigger
            ↓
Maven Build
            ↓
Smoke Suite Execution
            ↓
Extent Report Generation
            ↓
Artifact Upload
```

---

# GitHub Actions Workflow Features

- Automated Build Validation
- Cloud-Based Test Execution
- Headless Browser Execution
- Maven Dependency Caching
- Smoke Suite Execution on Push
- Regression Suite Execution on Schedule
- TestNG Report Archival
- Extent Report Artifact Upload

---

# Nightly Regression Execution

Regression suite executes automatically every night using GitHub Actions scheduler.

```yaml
schedule:
  - cron: '30 18 * * *'
```

Execution Time:
- 12:00 AM IST

---

# Parallel Execution

Framework supports parallel execution using TestNG.

```xml
parallel="classes"
thread-count="4"
```

---

# Data-Driven Testing

The framework supports Excel-based test execution using Apache POI.

Supported:
- Multiple User Login Validation
- Dynamic Test Data Handling
- Reusable Data Providers

---

# Reporting

The framework generates:

- Extent Reports
- TestNG Reports
- Maven Surefire Reports
- Screenshot Captures on Failures

Reports are automatically archived in GitHub Actions artifacts after execution.

---

# Running Tests Locally

## Run Smoke Suite

```bash
mvn test -DsuiteXmlFile=src/test/resources/testng-smoke.xml
```

---

## Run Regression Suite

```bash
mvn test -DsuiteXmlFile=src/test/resources/testng-regression.xml
```

---

## Run in Headless Mode

```bash
mvn clean test -Dbrowser=chrome -Dheadless=true
```

---

# Framework Highlights

- Enterprise Automation Architecture
- Unified Web + API Automation
- CI/CD Integrated Framework
- Cloud Execution Ready
- Parallel Execution Support
- Modular and Scalable Design
- Recruiter-Friendly Project Structure

---

# Future Enhancements

- Cross Browser Matrix Execution
- Docker Selenium Grid
- Allure Reporting
- BrowserStack / LambdaTest Integration
- Mobile Automation Integration
- AI-Based Test Generation
- Self-Healing Automation
- Advanced Reporting Dashboards

---

# Framework Screenshots

## GitHub Actions CI/CD

_Add workflow execution screenshot here_

---

## Extent Reports

_Add Extent Report screenshot here_

---

# Author

## Sumit Gohatre

QA Automation Engineer | SDET

### GitHub Profile
https://github.com/sumit-qa-automation

---

# License

This project is intended for learning, automation framework development, and portfolio demonstration purposes.