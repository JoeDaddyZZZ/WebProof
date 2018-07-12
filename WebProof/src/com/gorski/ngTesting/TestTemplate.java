package com.gorski.ngTesting;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


public class TestTemplate {

	@Test(description = "sysIdData")
	@Parameters({ "testName", "testGroup", "testParm1" })
	public void runTest(String clientName, String clientGroup, String testParm1) {
		SoftAssert softAssert = new SoftAssert();
		/*
		 * finalize
		 */
		softAssert.assertAll();
	}
}

