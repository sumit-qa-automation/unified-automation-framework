# AGENTS.md

# Unified Automation Framework - AI Agent Instructions

This repository contains an enterprise-grade Unified Automation Framework supporting:

- Web Automation
- API Automation
- Mobile Automation
- CI/CD Integration
- Cloud-Based Test Execution

## Framework Architecture Rules

- Follow layered automation architecture.
- Keep reusable logic inside ActionDriver or MobileActionDriver.
- Maintain separation between Web, API, and Mobile layers.
- Do not place business logic inside test classes.
- Use Page Object Model (POM) design pattern.
- Keep framework modular and scalable.

## Selenium Automation Standards

- Use explicit waits only.
- Avoid Thread.sleep().
- Use reusable locators.
- Use ActionDriver methods for UI interactions.
- Keep assertions inside test classes.

## API Automation Standards

- Use REST Assured for API validation.
- Maintain reusable request specifications.
- Validate response status, body, and schema.
- Keep API utilities reusable.

## Mobile Automation Standards

- Use Appium for Android automation.
- Use Sauce Labs cloud execution.
- Keep mobile-specific methods inside MobileActionDriver.
- Capture screenshots for failures.

## CI/CD Standards

- Maintain GitHub Actions workflow compatibility.
- Keep regression and smoke suites separated.
- Use GitHub Secrets for sensitive credentials.
- Upload reports as workflow artifacts.

## Reporting Standards

- Use Extent Reports for execution reporting.
- Capture screenshots for UI and mobile failures.
- Maintain clean report logging.

## Coding Standards

- Use meaningful class and method names.
- Follow Java naming conventions.
- Avoid duplicated logic.
- Keep framework clean and maintainable.