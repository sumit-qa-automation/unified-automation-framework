package com.unified.automation.framework.tests;

import org.testng.annotations.Test;

import com.unified.automation.framework.core.BaseClass;

public class DummyClass2 extends BaseClass {
	@Test
	public void dummyTest2()
	{
		String title=driver.getTitle();
		assert title.equals("OrangeHRM"):"Test Failed";
	    
		System.out.println("Test Passed");
	}

}
