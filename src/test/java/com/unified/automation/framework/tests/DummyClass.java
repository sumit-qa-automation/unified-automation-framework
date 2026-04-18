package com.unified.automation.framework.tests;

import org.testng.annotations.Test;

import com.unified.automation.framework.core.BaseClass;

public class DummyClass extends BaseClass {
	@Test
	public void dummyTest()
	{
		String title=getDriver().getTitle();
		assert title.equals("OrangeHRM"):"Test Failed";
	    
		System.out.println("Test Passed");
	}

}
