package com.unified.automation.framework.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.unified.automation.framework.core.BaseClass;
import com.unified.automation.framework.pages.HomePage;
import com.unified.automation.framework.pages.LoginPage;

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
		loginPage.login("admin", "admin123");
		Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after login");
	    homePage.logout();
	}

}
