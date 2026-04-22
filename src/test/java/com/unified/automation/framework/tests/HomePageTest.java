package com.unified.automation.framework.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.unified.automation.framework.core.BaseClass;
import com.unified.automation.framework.pages.HomePage;
import com.unified.automation.framework.pages.LoginPage;
import com.unified.automation.framework.utilities.DataProviders;

public class HomePageTest extends BaseClass {
	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verifyHomePageDashboardLogo(String username, String password) {
		loginPage.login(username, password);
		Assert.assertTrue(homePage.verifyOrangeHRMlogo(), "Logo is not visible");
		homePage.logout();
	}

}
