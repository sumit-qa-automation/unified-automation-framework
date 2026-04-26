package com.unified.automation.framework.api.tests;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.unified.automation.framework.utilities.ApiBaseUtility;
import com.unified.automation.framework.utilities.ExtentReportManager;

import io.restassured.response.Response;

public class ApiTest {
	@Test
	public void verifyGetUserAPI() {
		
		SoftAssert softAssert = new SoftAssert();

		// Step1: Define API Endpoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentReportManager.logStep("API Endpoint: " + endPoint);

		// Step2: Send GET Request
		ExtentReportManager.logStep("Sending GET Request to the API");
		Response response = ApiBaseUtility.sendGetRequest(endPoint);

		// Step3: validate status code
		ExtentReportManager.logStep("Validating API Response status code");
		boolean isStatusCodeValid = ApiBaseUtility.validateStatusCode(response, 200);

		softAssert.assertTrue(isStatusCodeValid, "Status code is not as Epxected");

		if (isStatusCodeValid) {
			ExtentReportManager.logStepValidationForAPI("Status Code Validation Passed!");
		} else {
			ExtentReportManager.logFailureAPI("Status Code Validation Failed!");
		}

		// Step4: validate user name
		ExtentReportManager.logStep("Validating response body for username");
		String userName = ApiBaseUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		softAssert.assertTrue(isUserNameValid, "Username is not valid");
		if (isUserNameValid) {
			ExtentReportManager.logStepValidationForAPI("Username Validation Passed!");
		} else {
			ExtentReportManager.logFailureAPI("Username Validation Failed!");
		}

		// Step4: validate email
		ExtentReportManager.logStep("Validating response body for email");
		String userEmail = ApiBaseUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
		softAssert.assertTrue(isEmailValid, "Email is not valid");
		if (isEmailValid) {
			ExtentReportManager.logStepValidationForAPI("Email Validation Passed!");
		} else {
			ExtentReportManager.logFailureAPI("Email Validation Failed!");
		}
		
		softAssert.assertAll();

	}

}


