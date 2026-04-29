package com.unified.automation.framework.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.unified.automation.framework.actions.ActionDriver;
import com.unified.automation.framework.utilities.ExtentReportManager;
import com.unified.automation.framework.utilities.LoggerManager;

import io.github.bonigarcia.wdm.WebDriverManager;

// Optional: Uncomment if using WebDriverManager
// import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

	protected static Properties prop;
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	protected static ThreadLocal<SoftAssert> softAssert = new ThreadLocal<>();

	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	@BeforeSuite
	public void initializeConfiguration() throws IOException {
		// Load configuration properties from file
		prop = new Properties();
		FileInputStream file = new FileInputStream("src/main/resources/config.properties");
		prop.load(file);
		logger.info("config.properties file is loaded");
	}

	@BeforeMethod
	public synchronized void setup() throws IOException {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());

		// Initialize browser and apply configuration
		launchBrowser();
		configureBrowser();

		staticWait(1);

		logger.info("WebDriver Initialized and Browser Maximized");
		logger.trace("This is a Trace message");
		logger.error("This is a error message");
		logger.debug("This is a debug message");
		logger.fatal("This is a fatal message");
		logger.warn("This is a warm message");

		// Initialize ActionDriver for the current thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for thread:" + Thread.currentThread());
	}

	private synchronized void launchBrowser() {

		// Read browser and headless configuration with system property override support
		String browser = System.getProperty("browser", prop.getProperty("browser"));
		boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", prop.getProperty("headless")));

		if (browser.equalsIgnoreCase("chrome")) {

			ChromeOptions options = new ChromeOptions();

			// Apply headless configuration if enabled
			if (isHeadless) {
				options.addArguments("--headless=new");
				options.addArguments("--disable-gpu");
				options.addArguments("--window-size=1920,1080");
			}

			options.addArguments("--disable-notifications");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");

			driver.set(new ChromeDriver(options));
			logger.info("ChromeDriver Initialized | Headless: " + isHeadless);

		} else if (browser.equalsIgnoreCase("edge")) {

			// Optional: Enable if using WebDriverManager
			WebDriverManager.edgedriver().setup();

			EdgeOptions options = new EdgeOptions();

			// Apply headless configuration if enabled
			if (isHeadless) {
				options.addArguments("--headless=new");
				options.addArguments("--disable-gpu");
				options.addArguments("--window-size=1920,1080");
			}

			options.addArguments("--disable-notifications");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");

			driver.set(new EdgeDriver(options));
			logger.info("EdgeDriver Initialized | Headless: " + isHeadless);

		} else if (browser.equalsIgnoreCase("firefox")) {

			FirefoxOptions options = new FirefoxOptions();

			// Apply headless configuration if enabled
			if (isHeadless) {
				options.addArguments("--headless");
			}

			options.addArguments("--disable-notifications");

			driver.set(new FirefoxDriver(options));
			logger.info("FirefoxDriver Initialized | Headless: " + isHeadless);

		} else {
			throw new IllegalArgumentException("Browser not supported");
		}

		// Register driver instance with reporting utility
		ExtentReportManager.registerDriver(getDriver());
	}

	private void configureBrowser() {

		// Apply implicit wait from configuration
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", prop.getProperty("headless")));

		// Maximize window only when running in non-headless mode
		if (!isHeadless) {
			getDriver().manage().window().maximize();
		}

		// Navigate to base URL
		try {
			getDriver().get(prop.getProperty("url_base"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to the URL: " + e.getMessage());
		}
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				logger.info("Unable to quit driver:" + e.getMessage());
			}
		}
		logger.info("WebDriver Instance is closed.");
		driver.remove();
		actionDriver.remove();
	}

	// Returns the WebDriver instance for the current thread
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			logger.info("WebDriver is not initiated");
			throw new IllegalStateException("WebDriver is not initiated");
		}
		return driver.get();
	}

	// Returns the ActionDriver instance for the current thread
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			logger.info("ActionDriver is not initiated");
			throw new IllegalStateException("ActionDriver is not initiated");
		}
		return actionDriver.get();
	}

	// Sets the ThreadLocal driver instance
	public void setDriver(ThreadLocal<WebDriver> driver) {
		BaseClass.driver = driver;
	}

	// Utility method to introduce static wait
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	// Returns loaded properties
	public static Properties getProp() {
		return prop;
	}
}