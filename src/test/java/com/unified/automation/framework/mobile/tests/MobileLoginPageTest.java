package com.unified.automation.framework.mobile.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.unified.automation.framework.core.MobileBaseClass;
import com.unified.automation.framework.mobile.pages.LoginPageMobile;

public class MobileLoginPageTest extends MobileBaseClass {

	private LoginPageMobile loginPage;

	@BeforeMethod
	public void initializePages() {

		loginPage = new LoginPageMobile(getMobileDriver());
	}

	@Test
	public void verifyValidLoginTest() {

		loginPage.login("bob@example.com", "10203040");

		Assert.assertTrue(loginPage.verifyProductsPageDisplayed(), "Products page is not displayed after login");
	}
}