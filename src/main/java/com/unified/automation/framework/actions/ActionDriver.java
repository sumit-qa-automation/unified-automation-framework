package com.unified.automation.framework.actions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.unified.automation.framework.utilities.ExtentReportManager;
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

	/**
	 * Centralized method to get a visible element (waits until visible).
	 */
	private WebElement getElement(By by) {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (TimeoutException te) {
			logger.error("Timed out waiting for visibility of element: " + by.toString(), te);
			throw te;
		} catch (Exception e) {
			logger.error("Unable to locate element: " + by.toString(), e);
			throw e;
		}
	}

	/**
	 * Centralized method to get an element when clickable.
	 */
	private WebElement getElementWhenClickable(By by) {
		try {
			return wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (TimeoutException te) {
			logger.error("Timed out waiting for element to be clickable: " + by.toString(), te);
			throw te;
		} catch (Exception e) {
			logger.error("Unable to locate clickable element: " + by.toString(), e);
			throw e;
		}
	}

	/**
	 * Safe sleep helper for short retries where necessary.
	 */
	private void safeSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
	}

	// ===================== Click / Input / GetText / Compare =====================

	public void click(By by) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement el = getElementWhenClickable(by);
			applyBorder(el, "green");
			el.click();
			ExtentReportManager.logStep("Clicked element: " + desc);
			logger.info("Clicked element --> " + desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to click element: " + desc,
					desc + "_unable_to_click");
			logger.error("Unable to click element: " + desc, e);
		}
	}

	public void enterText(By by, String value) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement el = getElement(by); // ensures visible
			el.clear();
			el.sendKeys(value);
			applyBorder(el, "green");
			ExtentReportManager.logStep("Entered text on " + desc + " -> " + value);
			logger.info("Entered text on " + desc + " -> " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to enter text: " + desc,
					desc + "_enterText_failed");
			logger.error("Unable to enter text into element: " + desc, e);
		}
	}

	public String getText(By by) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement el = getElement(by);
			String txt = el.getText();
			applyBorder(el, "green");
			ExtentReportManager.logStep("Got text from " + desc + " -> " + txt);
			logger.info("Got text from " + desc + " -> " + txt);
			return txt;
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to get text: " + desc,
					desc + "_getText_failed");
			logger.error("Unable to get text from element: " + desc, e);
			return "";
		}
	}

	public boolean compareText(By by, String expectedText) {
		String desc = getElementDescriptionSafely(by);
		try {
			String actualText = getText(by);
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				ExtentReportManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Text Verified Successfully! " + actualText + " equals " + expectedText);
				logger.info("Texts are matching: '{}' equals '{}'", actualText, expectedText);
				return true;
			} else {
				applyBorder(by, "red");
				ExtentReportManager.logFailure(BaseClass.getDriver(), "Text Comparison Failed!",
						"Text Comparison Failed! " + actualText + " not equals " + expectedText);
				logger.error("Text comparison failed: actual='{}' expected='{}'", actualText, expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "CompareText exception for: " + desc,
					desc + "_compare_failed");
			logger.error("Exception while comparing text for: " + desc, e);
			return false;
		}
	}

	// ===================== Display check =====================

	public boolean isDisplayed(By by) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement el = getElement(by);
			boolean displayed = el.isDisplayed();
			if (displayed) {
				applyBorder(el, "green");
				ExtentReportManager.logStep("Element is displayed: " + desc);
				ExtentReportManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed", desc);
				logger.info("Element is displayed: " + desc);
			} else {
				applyBorder(el, "red");
				ExtentReportManager.logFailure(BaseClass.getDriver(), "Element not displayed", desc + "_not_displayed");
				logger.warn("Element is not displayed: " + desc);
			}
			return displayed;
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Element is not displayed: " + desc,
					desc + "_isDisplayed_failed");
			logger.error("Element is not displayed: " + desc, e);
			return false;
		}
	}

	// ===================== Page load & scroll =====================

	public void waitForPageLoad(int timeOutInSec) {
		try {
			WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSec));
			localWait.until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded successfully within {} seconds.", timeOutInSec);
		} catch (Exception e) {
			logger.error("Page did not load within {} seconds. Exception: {}", timeOutInSec, e.getMessage());
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Page did not load", "page_load_failed");
		}
	}

	public void scrollToElement(By by) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement el = getElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
			applyBorder(el, "green");
			ExtentReportManager.logStep("Scrolled to element: " + desc);
			logger.info("Scrolled to element: " + desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to scroll to element: " + desc,
					desc + "_scroll_failed");
			logger.error("Unable to scroll to element: " + desc, e);
		}
	}

	// ===================== Wait helpers (exposed as private centralized methods)
	// =====================

	private void waitForElementToBeClickable(By by) {
		try {
			getElementWhenClickable(by);
		} catch (Exception e) {
			logger.warn("Element not clickable within timeout: " + by.toString(), e);
		}
	}

	private void waitForElementToBeVisible(By by) {
		try {
			getElement(by);
		} catch (Exception e) {
			logger.warn("Element not visible within timeout: " + by.toString(), e);
		}
	}

	// ===================== Element description (safe) =====================

	/**
	 * A safe version of getElementDescription which does not throw if element isn't
	 * present, and does not call driver.findElement blindly.
	 */
	public String getElementDescription(By locator) {
		try {
			WebElement element = getElement(locator);
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeholder = element.getDomAttribute("placeholder");

			if (isNotEmpty(name)) {
				return "Element with name: " + name;
			} else if (isNotEmpty(id)) {
				return "Element with ID: " + id;
			} else if (isNotEmpty(text)) {
				return "Element with text: " + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with class: " + className;
			} else if (isNotEmpty(placeholder)) {
				return "Element with placeholder: " + placeholder;
			} else {
				return "Element located using: " + locator.toString();
			}
		} catch (Exception e) {
			logger.warn("Unable to describe element: " + locator.toString(), e);
			return "Element located using: " + locator.toString();
		}
	}

	/**
	 * Helper which will try to describe element but never throw (used for logging
	 * when element may be missing).
	 */
	private String getElementDescriptionSafely(By locator) {
		try {
			return getElementDescription(locator);
		} catch (Exception e) {
			return locator.toString();
		}
	}

	private boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	private String truncate(String value, int maxLength) {
		if (value == null)
			return "";
		return (value.length() <= maxLength) ? value : value.substring(0, maxLength) + "...";
	}

	// ===================== Border / highlighting helpers =====================

	public void applyBorder(By by, String color) {
		try {
			WebElement element = driver.findElement(by);
			applyBorder(element, color);
		} catch (Exception e) {
			// If element not present, log a warn but don't fail tests
			logger.warn("Failed to apply border to: " + by.toString() + ". Reason: " + e.getMessage());
		}
	}

	private void applyBorder(WebElement element, String color) {
		try {
			if (element == null)
				return;
			String script = "arguments[0].style.border='3px solid " + color + "'";
			((JavascriptExecutor) driver).executeScript(script, element);
			logger.info("Applied border [{}] to element: {}", color, describeElementNoWait(element));
		} catch (StaleElementReferenceException sere) {
			logger.warn("StaleElement when applying border", sere);
		} catch (Exception e) {
			logger.warn("Failed to apply border to element", e);
		}
	}

	/**
	 * Describe a WebElement without additional DOM queries (best-effort).
	 */
	private String describeElementNoWait(WebElement element) {
		try {
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String cls = element.getDomAttribute("class");
			if (isNotEmpty(name))
				return "name=" + name;
			if (isNotEmpty(id))
				return "id=" + id;
			if (isNotEmpty(text))
				return "text=" + truncate(text, 30);
			if (isNotEmpty(cls))
				return "class=" + cls;
			return element.toString();
		} catch (Exception e) {
			return element.toString();
		}
	}

	// ===================== Select / Dropdown =====================

	public void selectByVisibleText(By by, String value) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement dropdownElement = getElement(by);
			Select select = new Select(dropdownElement);
			select.selectByVisibleText(value);
			applyBorder(dropdownElement, "green");
			ExtentReportManager.logStep("Selected dropdown (visible text) " + value + " on " + desc);
			logger.info("Selected dropdown (visible text) '{}' on {}", value, desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to select dropdown by visible text: " + desc,
					desc + "_select_failed");
			logger.error("Unable to select dropdown by visible text for: " + desc, e);
		}
	}

	public void selectByValue(By by, String value) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement dropdownElement = getElement(by);
			Select select = new Select(dropdownElement);
			select.selectByValue(value);
			applyBorder(dropdownElement, "green");
			ExtentReportManager.logStep("Selected dropdown (value) " + value + " on " + desc);
			logger.info("Selected dropdown (value) '{}' on {}", value, desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to select dropdown by value: " + desc,
					desc + "_select_failed");
			logger.error("Unable to select dropdown by value for: " + desc, e);
		}
	}

	public void selectByIndex(By by, int index) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement dropdownElement = getElement(by);
			Select select = new Select(dropdownElement);
			select.selectByIndex(index);
			applyBorder(dropdownElement, "green");
			ExtentReportManager.logStep("Selected dropdown (index) " + index + " on " + desc);
			logger.info("Selected dropdown (index) '{}' on {}", index, desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to select dropdown by index: " + desc,
					desc + "_select_failed");
			logger.error("Unable to select dropdown by index for: " + desc, e);
		}
	}

	public List<String> getDropdownOptions(By by) {
		String desc = getElementDescriptionSafely(by);
		List<String> optionsList = new ArrayList<>();
		try {
			WebElement dropdownElement = getElement(by);
			Select select = new Select(dropdownElement);
			for (WebElement option : select.getOptions()) {
				optionsList.add(option.getText());
			}
			applyBorder(dropdownElement, "green");
			ExtentReportManager.logStep("Retrieved dropdown options for " + desc);
			logger.info("Retrieved dropdown options for {}", desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to get dropdown options: " + desc,
					desc + "_getOptions_failed");
			logger.error("Unable to get dropdown options for: " + desc, e);
		}
		return optionsList;
	}

	// ===================== JavaScript Utilities =====================

	public void clickUsingJS(By by) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement el = getElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
			applyBorder(el, "green");
			ExtentReportManager.logStep("Clicked element using JS: " + desc);
			logger.info("Clicked element using JS: " + desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to click using JS: " + desc,
					desc + "_js_click_failed");
			logger.error("Unable to click using JS for: " + desc, e);
		}
	}

	public void scrollToBottom() {
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
			ExtentReportManager.logStep("Scrolled to bottom of page.");
			logger.info("Scrolled to bottom of page.");
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to scroll to bottom", "scroll_bottom_failed");
			logger.error("Unable to scroll to bottom of page", e);
		}
	}

	public void highlightElementJS(By by) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement el = getElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid yellow'", el);
			ExtentReportManager.logStep("Highlighted element: " + desc);
			logger.info("Highlighted element using JS: " + desc);
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to highlight element using JS: " + desc,
					desc + "_highlight_failed");
			logger.error("Unable to highlight element using JS for: " + desc, e);
		}
	}

	// ===================== Window & Frame =====================

	public void switchToWindow(String windowTitle) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(windowTitle)) {
					ExtentReportManager.logStep("Switched to window: " + windowTitle);
					logger.info("Switched to window: " + windowTitle);
					return;
				}
			}
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Window not found: " + windowTitle,
					"switch_window_failed");
			logger.warn("Window with title '{}' not found.", windowTitle);
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to switch window: " + windowTitle,
					"switch_window_failed");
			logger.error("Unable to switch window: " + windowTitle, e);
		}
	}

	public void switchToFrame(By by) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement frame = getElement(by);
			driver.switchTo().frame(frame);
			ExtentReportManager.logStep("Switched to iframe: " + desc);
			logger.info("Switched to iframe: " + desc);
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to switch to iframe: " + desc,
					desc + "_switchToFrame_failed");
			logger.error("Unable to switch to iframe: " + desc, e);
		}
	}

	public void switchToDefaultContent() {
		try {
			driver.switchTo().defaultContent();
			ExtentReportManager.logStep("Switched back to default content.");
			logger.info("Switched back to default content.");
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to switch back to default content",
					"switch_default_content_failed");
			logger.error("Unable to switch back to default content", e);
		}
	}

	// ===================== Alerts =====================

	public void acceptAlert() {
		try {
			wait.until(ExpectedConditions.alertIsPresent()).accept();
			ExtentReportManager.logStep("Alert accepted.");
			logger.info("Alert accepted.");
		} catch (TimeoutException te) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "No alert found to accept", "accept_alert_failed");
			logger.warn("No alert found to accept", te);
		} catch (NoAlertPresentException nae) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "No alert found to accept", "accept_alert_failed");
			logger.warn("No alert found to accept", nae);
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Error while accepting alert", "accept_alert_failed");
			logger.error("Error while accepting alert", e);
		}
	}

	public void dismissAlert() {
		try {
			wait.until(ExpectedConditions.alertIsPresent()).dismiss();
			ExtentReportManager.logStep("Alert dismissed.");
			logger.info("Alert dismissed.");
		} catch (TimeoutException te) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "No alert found to dismiss", "dismiss_alert_failed");
			logger.warn("No alert found to dismiss", te);
		} catch (NoAlertPresentException nae) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "No alert found to dismiss", "dismiss_alert_failed");
			logger.warn("No alert found to dismiss", nae);
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Error while dismissing alert",
					"dismiss_alert_failed");
			logger.error("Error while dismissing alert", e);
		}
	}

	public String getAlertText() {
		try {
			String text = wait.until(ExpectedConditions.alertIsPresent()).getText();
			ExtentReportManager.logStep("Alert text retrieved: " + text);
			logger.info("Alert text retrieved: " + text);
			return text;
		} catch (TimeoutException te) {
			logger.warn("No alert present to fetch text", te);
			return "";
		} catch (Exception e) {
			logger.error("Unable to fetch alert text", e);
			return "";
		}
	}

	// ===================== Browser actions =====================

	public void refreshPage() {
		try {
			driver.navigate().refresh();
			ExtentReportManager.logStep("Page refreshed successfully.");
			logger.info("Page refreshed successfully.");
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to refresh page", "refresh_page_failed");
			logger.error("Unable to refresh page", e);
		}
	}

	public String getCurrentURL() {
		try {
			String url = driver.getCurrentUrl();
			ExtentReportManager.logStep("Current URL fetched: " + url);
			logger.info("Current URL fetched: " + url);
			return url;
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to fetch current URL",
					"get_current_url_failed");
			logger.error("Unable to fetch current URL", e);
			return null;
		}
	}

	public void maximizeWindow() {
		try {
			driver.manage().window().maximize();
			ExtentReportManager.logStep("Browser window maximized.");
			logger.info("Browser window maximized.");
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to maximize window",
					"maximize_window_failed");
			logger.error("Unable to maximize window", e);
		}
	}

	// ===================== Advanced WebElement Actions (Actions API wrappers)
	// =====================

	public void moveToElement(By by) {
		String desc = getElementDescriptionSafely(by);
		try {
			Actions actions = new Actions(driver);
			WebElement el = getElement(by);
			actions.moveToElement(el).perform();
			ExtentReportManager.logStep("Moved to element: " + desc);
			logger.info("Moved to element --> " + desc);
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to move to element", desc + "_move_failed");
			logger.error("Unable to move to element: " + desc, e);
		}
	}

	public void dragAndDrop(By source, By target) {
		String srcDesc = getElementDescriptionSafely(source);
		String tgtDesc = getElementDescriptionSafely(target);
		try {
			Actions actions = new Actions(driver);
			WebElement src = getElement(source);
			WebElement tgt = getElement(target);
			actions.dragAndDrop(src, tgt).perform();
			ExtentReportManager.logStep("Dragged element: " + srcDesc + " and dropped on " + tgtDesc);
			logger.info("Dragged element: {} and dropped on {}", srcDesc, tgtDesc);
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to drag and drop", srcDesc + "_drag_failed");
			logger.error("Unable to drag and drop: source={} target={}", srcDesc, tgtDesc, e);
		}
	}

	public void doubleClick(By by) {
		String desc = getElementDescriptionSafely(by);
		try {
			Actions actions = new Actions(driver);
			WebElement el = getElement(by);
			actions.doubleClick(el).perform();
			ExtentReportManager.logStep("Double-clicked on element: " + desc);
			logger.info("Double-clicked on element --> " + desc);
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to double-click element",
					desc + "_doubleclick_failed");
			logger.error("Unable to double-click element: " + desc, e);
		}
	}

	public void rightClick(By by) {
		String desc = getElementDescriptionSafely(by);
		try {
			Actions actions = new Actions(driver);
			WebElement el = getElement(by);
			actions.contextClick(el).perform();
			ExtentReportManager.logStep("Right-clicked on element: " + desc);
			logger.info("Right-clicked on element --> " + desc);
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to right-click element",
					desc + "_rightclick_failed");
			logger.error("Unable to right-click element: " + desc, e);
		}
	}

	public void sendKeysWithActions(By by, String value) {
		String desc = getElementDescriptionSafely(by);
		try {
			Actions actions = new Actions(driver);
			WebElement el = getElement(by);
			actions.sendKeys(el, value).perform();
			ExtentReportManager.logStep("Sent keys to element: " + desc + " | Value: " + value);
			logger.info("Sent keys to element --> " + desc + " | Value: " + value);
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to send keys", desc + "_sendkeys_failed");
			logger.error("Unable to send keys to element: " + desc, e);
		}
	}

	public void clearText(By by) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement el = getElement(by);
			el.clear();
			ExtentReportManager.logStep("Cleared text in element: " + desc);
			logger.info("Cleared text in element --> " + desc);
		} catch (Exception e) {
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to clear text", desc + "_clear_failed");
			logger.error("Unable to clear text in element: " + desc, e);
		}
	}

	// ===================== File upload =====================

	public void uploadFile(By by, String filePath) {
		String desc = getElementDescriptionSafely(by);
		try {
			WebElement el = getElement(by);
			el.sendKeys(filePath);
			applyBorder(el, "green");
			ExtentReportManager.logStep("Uploaded file: " + filePath + " on " + desc);
			logger.info("Uploaded file: {} on {}", filePath, desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentReportManager.logFailure(BaseClass.getDriver(), "Unable to upload file: " + desc,
					desc + "_upload_failed");
			logger.error("Unable to upload file for element: " + desc, e);
		}
	}

	// ===================== Utilities =====================

	/**
	 * Attempt to get a description from an element without waiting (best-effort).
	 * Useful when an element reference is available.
	 */
	private String safeDescribeElement(WebElement element) {
		try {
			return describeElementNoWait(element);
		} catch (Exception e) {
			return element.toString();
		}
	}
}