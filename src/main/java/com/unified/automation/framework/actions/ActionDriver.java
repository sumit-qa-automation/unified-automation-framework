package com.unified.automation.framework.actions;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.unified.automation.framework.core.BaseClass;

public class ActionDriver {
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("ActionDriver initialized with explicitWait: " + explicitWait + " seconds.");
	}

	// Wait for element to be clickable
	public void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.info("Element is not clickable:" + e.getMessage());
		}
	}

	// Wait for element to be visible
	public void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.info("Element is not visible:" + e.getMessage());
		}
	}

	// Method to click on element
	public void click(By by) {
		try {
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			logger.info("click on element");
		} catch (Exception e) {
			logger.error("Unable to click on element:" + e.getMessage());
		}
	}

	// Method to enter text into an input field
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Text value entered is:"+value);
		} catch (Exception e) {
			logger.error("Unable to enter value in input box:" + e.getMessage());
		}
	}

	// Method to get text
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			logger.error("Unable to get text:" + e.getMessage());
			return "";
		}
	}

	// Method to compare two text
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				logger.info("Text are matching:" + actualText + "equals" + expectedText);
				return true;
			} else {
				logger.error("Text are not matching:" + actualText + "not equals" + expectedText);
				return false;
			}

		} catch (Exception e) {
			logger.error("Unable to compare text:" + e.getMessage());

		}
		return false;
	}

	// Method to check if element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).isDisplayed();

		} catch (Exception e) {
			logger.error("Element is not displaed:" + e.getMessage());
			return false;
		}
	}

	// Wait for page load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded successfully");
		} catch (Exception e) {
			logger.error("Page did not load within" + timeOutInSec + "seconds.Exception:" + e.getMessage());
		}
	}

	// Method for Scroll to element
	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("argumrnts[0],scrollIntoView(true);", element);
		} catch (Exception e) {
			logger.error("Unable to locate element");
		}
	}

}
