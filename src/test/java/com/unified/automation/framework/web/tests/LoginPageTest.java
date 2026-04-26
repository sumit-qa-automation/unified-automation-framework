package com.unified.automation.framework.web.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.unified.automation.framework.core.BaseClass;
import com.unified.automation.framework.pages.HomePage;
import com.unified.automation.framework.pages.LoginPage;
import com.unified.automation.framework.utilities.DataProviders;
import com.unified.automation.framework.utilities.ExtentReportManager;

public class LoginPageTest extends BaseClass {
	private LoginPage loginPage;
	private HomePage homePage;
	
	SoftAssert softAssert=getSoftAssert();

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
    @Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {
    	//ExtentReportManager.startTest("Valid login test");
    	//ExtentReportManager.logStep("Navigating to login page");
		loginPage.login(username, password);
		//ExtentReportManager.logStep("Verify admin tab is visible");
		Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after login");
	    homePage.logout();
	}
    @Test(dataProvider = "inValidLoginData", dataProviderClass = DataProviders.class)
	public void verifyInValidLoginTest(String username, String password) {
		loginPage.login(username, password);
		String expectedErrorMessage="Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed:Invalid error message");
	}

}
