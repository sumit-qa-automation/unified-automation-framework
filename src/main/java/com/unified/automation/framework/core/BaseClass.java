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
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.unified.automation.framework.actions.ActionDriver;
import com.unified.automation.framework.utilities.ExtentReportManager;
import com.unified.automation.framework.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	@BeforeSuite
	public void initializeConfiguration() throws IOException {
		// Load the configuration file by giving path of file
		prop = new Properties();
		FileInputStream file = new FileInputStream("src/main/resources/config.properties");
		prop.load(file);
		logger.info("config.properties file is loaded");
        
		ExtentReportManager.getReporter();
	}

	@BeforeMethod
	public synchronized void setup() throws IOException {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(1);
		// Sample logger message
		logger.info("WebDriver Initialized and Browser Maximized");
		logger.trace("This is a Trace message");
		logger.error("This is a error message");
		logger.debug("This is a debug message");
		logger.fatal("This is a fatal message");
		logger.warn("This is a warm message");

		/*
		 * // Initialize the ActionDriver only once if (actionDriver == null) {
		 * actionDriver = new ActionDriver(driver);
		 * logger.info("ActionDriver initlialized for thread: " +
		 * Thread.currentThread().getId()); }
		 */
		
		// Initialize the ActionDriver for the current thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for thread:"+Thread.currentThread());
		
		

	}

	private synchronized void launchBrowser() {
		// Initialize the WebDriver based on browser defined in config.properties file
		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			//driver = new ChromeDriver();
			driver.set(new ChromeDriver());
			ExtentReportManager.registerDriver(getDriver());
			logger.info("ChromeDriver Initialized");
		} else if (browser.equalsIgnoreCase("edge")) {
			//driver = new EdgeDriver();
			driver.set(new EdgeDriver());
			ExtentReportManager.registerDriver(getDriver());
			logger.info("EdgeDriver Initialized");
		} else if (browser.equalsIgnoreCase("firefox")) {
			//driver = new FirefoxDriver();
			driver.set(new FirefoxDriver());
			ExtentReportManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Initialized");
		} else {
			throw new IllegalArgumentException("Browser not supported");
		}

	}

	// Configure browser setting such as implicit wait and maximize browser and
	// navigate url
	private void configureBrowser() {
		// Implicit Wait, First convert String to int
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// Maximize the browser
		getDriver().manage().window().maximize();

		// Navigate to url
		try {
			getDriver().get(prop.getProperty("url_base"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to the UR:" + e.getMessage());
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
		//driver = null;
       //actionDriver = null;
        ExtentReportManager.endTest();

	}

	// Getter method for WebDriver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			logger.info("WebDriver is not initiated");
			throw new IllegalStateException("WebDriver is not initiated");
		}
		return driver.get();
	}

	// Getter method for WebDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			logger.info("ActionDriver is not initiated");
			throw new IllegalStateException("ActionDriver is not initiated");
		}
		return actionDriver.get();
	}

	// Setter method for Driver
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}

}
