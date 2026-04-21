package com.unified.automation.framework.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.unified.automation.framework.core.BaseClass;
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
		// Start logging in Extent Reports
		ExtentReportManager.startTest(testName);
		ExtentReportManager.logStep("Test Started: " + testName);
	}

	// Triggered when a Test succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();

		if (!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentReportManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed Successfully!",
					"Test End: " + testName + " - ✔ Test Passed");
		} else {
			ExtentReportManager.logStepValidationForAPI("Test End: " + testName + " - ✔ Test Passed");
		}

	}

	// Triggered when a Test Fails
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentReportManager.logStep(failureMessage);
		ExtentReportManager.logFailure(BaseClass.getDriver(), "Test Failed!",
				"Test End: " + testName + " - ❌ Test Failed");
		/*
		 * if(!result.getTestClass().getName().toLowerCase().contains("api")) {
		 * ExtentReportManager.logFailure(BaseClass.getDriver(), "Test Failed!",
		 * "Test End: " + testName + " - ❌ Test Failed"); } else {
		 * ExtentReportManager.logFailureAPI("Test End: " + testName +
		 * " - ❌ Test Failed"); }
		 */
	}

	// Triggered when a Test skips
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentReportManager.logSkip("Test Skipped " + testName);
	}

	// Triggered when a suite Starts
	@Override
	public void onStart(ITestContext context) {
		// Initialize the Extent Reports
		ExtentReportManager.getReporter();
	}

	// Triggered when the suite ends
	@Override
	public void onFinish(ITestContext context) {
		// Flush the Extent Reports
		ExtentReportManager.endTest();

	}

}
