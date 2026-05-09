package com.unified.automation.framework.mobile.pages;

import org.openqa.selenium.By;

import com.unified.automation.framework.actions.MobileActionDriver;

import io.appium.java_client.android.AndroidDriver;

public class LoginPageMobile {

	private AndroidDriver driver;
	private MobileActionDriver action;

	public LoginPageMobile(AndroidDriver driver) {

		this.driver = driver;

		action = new MobileActionDriver(driver);
	}

	// ========================= LOCATORS =========================

	private By menuButton = By.xpath("//android.view.ViewGroup[@content-desc=\"open menu\"]");

	private By loginMenu = By.xpath("//android.widget.TextView[@text=\"Log In\"]");

	private By usernameField = By.xpath("//android.widget.EditText[@content-desc=\"Username input field\"]");

	private By passwordField = By.xpath("//android.widget.EditText[@content-desc=\"Password input field\"]");

	private By loginButton = By.xpath("//android.view.ViewGroup[@content-desc=\"Login button\"]");

	private By productsTitle = By.xpath("//android.widget.TextView[@text=\"Products\"]");

	// ========================= ACTION METHODS =========================

	public void clickMenu() {

		action.click(menuButton);
	}

	public void clickLoginMenu() {

		action.click(loginMenu);
	}

	public void enterUsername(String username) {

		action.enterText(usernameField, username);
	}

	public void enterPassword(String password) {

		action.enterText(passwordField, password);
	}

	public void clickLoginButton() {

		action.click(loginButton);
	}

	public boolean verifyProductsPageDisplayed() {

		return action.isDisplayed(productsTitle);
	}

	// ========================= BUSINESS METHOD =========================

	public void login(String username, String password) {

		clickMenu();

		clickLoginMenu();

		enterUsername(username);

		enterPassword(password);

		clickLoginButton();
	}
}