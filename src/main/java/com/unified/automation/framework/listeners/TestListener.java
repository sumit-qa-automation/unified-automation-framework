package com.unified.automation.framework.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.unified.automation.framework.core.BaseClass;
import com.unified.automation.framework.core.MobileBaseClass;
import com.unified.automation.framework.utilities.ExtentReportManager;
//import com.unified.automation.framework.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer {

	/*
	 * @Override public void transform(ITestAnnotation annotation, Class testClass,
	 * Constructor testConstructor, Method testMethod) {
	 * annotation.setRetryAnalyzer(RetryAnalyzer.class); }
	 */

	// Triggered when a test starts
	@Override
	public void onTestStart(ITestResult result) {

		String testName = result.getMethod().getMethodName();

		ExtentReportManager.startTest(testName);

		ExtentReportManager.logStep("Test Started: " + testName);
	}

	// Triggered when a Test succeeds
	@Override
	public void onTestSuccess(ITestResult result) {

		String testName = result.getMethod().getMethodName();

		String className = result.getTestClass().getName().toLowerCase();

		// ================= MOBILE =================

		if (className.contains("mobile")) {

			ExtentReportManager.logStepWithScreenshot(MobileBaseClass.getMobileDriver(), "Mobile Test Passed Successfully!",
					"Test End: " + testName + " - ✔ Mobile Test Passed");

		}

		// ================= WEB =================

		else if (!className.contains("api")) {

			ExtentReportManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed Successfully!",
					"Test End: " + testName + " - ✔ Test Passed");

		}

		// ================= API =================

		else {

			ExtentReportManager.logStepValidationForAPI("Test End: " + testName + " - ✔ Test Passed");
		}
	}

	// Triggered when a Test Fails
	@Override
	public void onTestFailure(ITestResult result) {

		String testName = result.getMethod().getMethodName();

		String failureMessage = result.getThrowable().getMessage();

		String className = result.getTestClass().getName().toLowerCase();

		ExtentReportManager.logStep(failureMessage);

		// ================= MOBILE =================

		if (className.contains("mobile")) {

			ExtentReportManager.logFailure(MobileBaseClass.getMobileDriver(), "Mobile Test Failed!",
					"Test End: " + testName + " - ❌ Mobile Test Failed");

		}

		// ================= WEB =================

		else if (!className.contains("api")) {

			ExtentReportManager.logFailure(BaseClass.getDriver(), "Test Failed!",
					"Test End: " + testName + " - ❌ Test Failed");

		}

		// ================= API =================

		else {

			ExtentReportManager.logFailureAPI("Test End: " + testName + " - ❌ API Test Failed");
		}
	}

	// Triggered when a Test skips
	@Override
	public void onTestSkipped(ITestResult result) {

		String testName = result.getMethod().getMethodName();

		ExtentReportManager.logSkip("Test Skipped " + testName);
	}

	// Triggered when suite starts
	@Override
	public void onStart(ITestContext context) {

		ExtentReportManager.getReporter();
	}

	// Triggered when suite ends
	@Override
	public void onFinish(ITestContext context) {

		ExtentReportManager.endTest();
	}
}