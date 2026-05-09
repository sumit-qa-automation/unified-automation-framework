package com.unified.automation.framework.actions;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.unified.automation.framework.core.BaseClass;
import com.unified.automation.framework.core.MobileBaseClass;
import com.unified.automation.framework.utilities.ExtentReportManager;

import io.appium.java_client.android.AndroidDriver;

public class MobileActionDriver {

	private AndroidDriver driver;
	private WebDriverWait wait;

	public static final Logger logger = BaseClass.logger;

	public MobileActionDriver(AndroidDriver driver) {

		this.driver = driver;

		int explicitWait = Integer.parseInt(MobileBaseClass.prop.getProperty("explicitWait"));

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));

		logger.info("MobileActionDriver initialized with explicitWait: " + explicitWait + " seconds.");
	}

	/**
	 * Wait until element visible
	 */
	private WebElement getElement(By by) {

		try {

			return wait.until(ExpectedConditions.visibilityOfElementLocated(by));

		} catch (TimeoutException te) {

			logger.error("Timed out waiting for mobile element visibility: " + by.toString(), te);

			throw te;

		} catch (Exception e) {

			logger.error("Unable to locate mobile element: " + by.toString(), e);

			throw e;
		}
	}

	/**
	 * Wait until element clickable
	 */
	private WebElement getElementWhenClickable(By by) {

		try {

			return wait.until(ExpectedConditions.elementToBeClickable(by));

		} catch (TimeoutException te) {

			logger.error("Timed out waiting for mobile element clickable: " + by.toString(), te);

			throw te;

		} catch (Exception e) {

			logger.error("Unable to locate clickable mobile element: " + by.toString(), e);

			throw e;
		}
	}

	// ========================= CLICK =========================

	public void click(By by) {

		try {

			WebElement element = getElementWhenClickable(by);

			element.click();

			ExtentReportManager.logStep("Clicked mobile element: " + by.toString());

			logger.info("Clicked mobile element --> " + by.toString());

		} catch (Exception e) {

			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to click mobile element",
					"mobile_click_failed");

			logger.error("Unable to click mobile element: " + by.toString(), e);
		}
	}

	// ========================= ENTER TEXT =========================

	public void enterText(By by, String value) {

		try {

			WebElement element = getElement(by);

			element.clear();

			element.sendKeys(value);

			ExtentReportManager.logStep("Entered text in mobile element: " + by.toString() + " -> " + value);

			logger.info("Entered text in mobile element --> " + by.toString());

		} catch (Exception e) {

			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to enter text in mobile element",
					"mobile_enter_text_failed");

			logger.error("Unable to enter text in mobile element: " + by.toString(), e);
		}
	}

	// ========================= GET TEXT =========================

	public String getText(By by) {

		try {

			WebElement element = getElement(by);

			String text = element.getText();

			ExtentReportManager.logStep("Retrieved text from mobile element: " + by.toString() + " -> " + text);

			logger.info("Retrieved text from mobile element --> " + by.toString());

			return text;

		} catch (Exception e) {

			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to retrieve text from mobile element",
					"mobile_get_text_failed");

			logger.error("Unable to retrieve text from mobile element: " + by.toString(), e);

			return "";
		}
	}

	// ========================= DISPLAYED =========================

	public boolean isDisplayed(By by) {

		try {

			WebElement element = getElement(by);

			boolean displayed = element.isDisplayed();

			if (displayed) {

				ExtentReportManager.logStep("Mobile element displayed: " + by.toString());

				logger.info("Mobile element displayed --> " + by.toString());

			} else {

				ExtentReportManager.logFailure(BaseClass.getDriver(), "Mobile element not displayed",
						"mobile_element_not_displayed");
			}

			return displayed;

		} catch (Exception e) {

			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to verify mobile element visibility",
					"mobile_visibility_failed");

			logger.error("Unable to verify mobile element visibility: " + by.toString(), e);

			return false;
		}
	}
}