# CLAUDE.md

# Claude Code Repository Instructions

This repository contains a Unified Automation Framework built using:

- Selenium WebDriver
- REST Assured
- Appium
- TestNG
- Maven
- GitHub Actions
- Sauce Labs

## Repository Goals

- Maintain enterprise-grade framework structure.
- Generate reusable and maintainable automation code.
- Preserve framework architecture consistency.
- Avoid hardcoded values.
- Follow CI/CD-compatible design.

## Framework Structure

- Web automation classes:
  src/test/java/web/tests

- API automation classes:
  src/test/java/api/tests

- Mobile automation classes:
  src/test/java/mobile/tests

- Reusable actions:
  src/main/java/actions

- Core setup:
  src/main/java/core

- Utilities:
  src/main/java/utilities

## Automation Guidelines

- Use Page Object Model.
- Use reusable action methods.
- Avoid duplicate locators.
- Keep waits reusable.
- Use proper logging.
- Maintain reporting compatibility.

## Mobile Automation Guidelines

- Use Appium with AndroidDriver.
- Use Sauce Labs cloud execution.
- Maintain mobile screenshots in reports.
- Keep device configuration reusable.

## CI/CD Guidelines

- Maintain GitHub Actions compatibility.
- Keep workflows modular.
- Use GitHub Secrets for credentials.
- Preserve report publishing logic.

## Reporting Guidelines

- Use Extent Reports.
- Capture screenshots on failures.
- Maintain clean logging.

## Important Notes

- Do not break existing framework architecture.
- Keep framework scalable and modular.
- Follow Java and TestNG best practices.