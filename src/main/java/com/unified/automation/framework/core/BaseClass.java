package com.unified.automation.framework.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.unified.automation.framework.actions.ActionDriver;

public class BaseClass {

	protected static Properties prop;
	protected static WebDriver driver;
	private static ActionDriver actionDriver;

	@BeforeSuite
	public void initializeConfiguration() throws IOException {
		// Load the configuration file by giving path of file
		prop = new Properties();
		FileInputStream file = new FileInputStream("src/main/resources/config.properties");
		prop.load(file);

	}

	@BeforeMethod
	public void setup() throws IOException {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(7);

		// Initialize the ActionDriver only once
		if (actionDriver == null) {
			actionDriver = new ActionDriver(driver);
			System.out.println("ActionDriver instance is created");
		}

	}

	private void launchBrowser() {
		// Initialize the WebDriver based on browser defined in config.properties file
		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else {
			throw new IllegalArgumentException("Browser not supported");
		}

	}

	// Configure browser setting such as implicit wait and maximize browser and
	// navigate url
	private void configureBrowser() {
		// Implicit Wait, First convert String to int
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// Maximize the browser
		driver.manage().window().maximize();

		// Navigate to url
		try {
			driver.get(prop.getProperty("url_base"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to the UR:" + e.getMessage());
		}

	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			try {
				driver.quit();
			} catch (Exception e) {
				System.out.println("Unable to quit driver:" + e.getMessage());
			}
		}
		System.out.println("WebDriver Instance is closed.");
		driver = null;
		actionDriver = null;

	}

	// Getter method for WebDriver
	public static WebDriver getDriver() {
		if (driver == null) {
			System.out.println("WebDriver is not initiated");
			throw new IllegalStateException("WebDriver is not initiated");
		}
		return driver;
	}

	// Getter method for WebDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver == null) {
			System.out.println("ActionDriver is not initiated");
			throw new IllegalStateException("ActionDriver is not initiated");
		}
		return actionDriver;
	}

	// Setter method for Driver
	public void setDriver(WebDriver driver) {
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
