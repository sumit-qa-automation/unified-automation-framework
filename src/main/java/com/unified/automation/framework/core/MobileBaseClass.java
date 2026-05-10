package com.unified.automation.framework.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class MobileBaseClass {

	protected static AndroidDriver driver;

	public static Properties prop;

	public static final Logger logger = LogManager.getLogger(MobileBaseClass.class);

	public static AndroidDriver getMobileDriver() {
		return driver;
	}

	@BeforeClass
	public void setupMobile() throws MalformedURLException {

		prop = new Properties();

		try (FileInputStream file = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties")) {

			prop.load(file);

			logger.info("Mobile config.properties file loaded successfully");

		} catch (IOException e) {

			logger.error("Unable to load mobile config.properties file", e);

			throw new RuntimeException(e);
		}

		UiAutomator2Options options = new UiAutomator2Options();

		// Platform Configuration

		options.setPlatformName("Android");

		options.setApp(prop.getProperty("mobile.app"));

		options.setDeviceName("Samsung.*");

		options.setAutomationName("UiAutomator2");

		// Sauce Labs Credentials

		String username = System.getenv("SAUCE_USERNAME") != null ? System.getenv("SAUCE_USERNAME")
				: prop.getProperty("sauce.username");

		String accessKey = System.getenv("SAUCE_ACCESS_KEY") != null ? System.getenv("SAUCE_ACCESS_KEY")
				: prop.getProperty("sauce.accesskey");

		// Sauce Labs Options

		options.setCapability("sauce:options", Map.of("username", username, "accessKey", accessKey, "appiumVersion",
				"latest", "build", "Mobile Automation Build", "name", "Android Login Test"));

		String sauceURL = "https://ondemand.eu-central-1.saucelabs.com:443/wd/hub";

		try {

			logger.info("Connecting to Sauce Labs mobile cloud execution");

			driver = new AndroidDriver(new URL(sauceURL), options);

			logger.info("Mobile Driver Started Successfully");

		} catch (Exception e) {

			logger.error("Unable to start mobile session", e);

			throw e;
		}
	}

	@AfterClass
	public void tearDownMobile() {

		if (driver != null) {

			driver.quit();

			logger.info("Mobile Driver Closed Successfully");
		}
	}
}