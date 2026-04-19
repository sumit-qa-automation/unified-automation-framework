package com.unified.automation.framework.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.unified.automation.framework.core.BaseClass;
import com.unified.automation.framework.pages.HomePage;
import com.unified.automation.framework.pages.LoginPage;
import com.unified.automation.framework.utilities.ExtentReportManager;

public class LoginPageTest extends BaseClass {
	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
    @Test
	public void verifyValidLoginTest() {
    	ExtentReportManager.startTest("Valid login test");
    	ExtentReportManager.logStep("Navigating to login page");
		loginPage.login("admin", "admin123");
		ExtentReportManager.logStep("Verify admin tab is visible");
		Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after login");
	    homePage.logout();
	}
    @Test
	public void verifyInValidLoginTest() {
		loginPage.login("admin", "admin1");
		String expectedErrorMessage="Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed:Invalid error message");
	}

}
