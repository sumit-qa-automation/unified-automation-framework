package com.unified.automation.framework.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseClass {

	protected Properties prop;
	protected WebDriver driver;
    @BeforeMethod
	public void setup() throws IOException {
		// Load the configuration file by giving path of file
		prop = new Properties();
		FileInputStream file = new FileInputStream("src/main/resources/config.properties");
		prop.load(file);

		// Initialize the WebDriver based on browser defined in config.properties file
		String browser = prop.getProperty("browser");
test
		if (browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else {
			throw new IllegalArgumentException("Browser not supported");
		}

		// Implicit Wait, First convert String to int
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// Maximize the browser
		driver.manage().window().maximize();

		// Navigate to url
		driver.get(prop.getProperty("url_base"));

	}
    @AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

}
