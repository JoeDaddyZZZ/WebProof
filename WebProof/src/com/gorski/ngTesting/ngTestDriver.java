package com.gorski.ngTesting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.uncommons.reportng.HTMLReporter;
import org.uncommons.reportng.JUnitXMLReporter;

public class ngTestDriver {
	static String guiClassPath = "com.gorski.ngTesting.";

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		/*
		 * read arguments
		 */
		String[] tests = new String[] {
				// testParm1 = true/false if group should have access
			"TestTemplate,user,group,parameters",
				};
		String suiteName = "";
		suiteName = "Other";
		/*
		 * create suite
		 */
		List<XmlSuite> suites = new ArrayList<>();
		XmlSuite suite = new XmlSuite();
		suite.setName("NG Testing " + suiteName);
		System.out.println("Running test suite  " + suiteName);
		int count = 0;
		/*
		 * for each item in the tests array add a test.
		 */
		for (String test : tests) {
			List<XmlClass> testClasses = new ArrayList<>();
			System.out.println(test);
			String[] vals = test.split(",");
			/*
			 * add test
			 */
			XmlTest newTest = new XmlTest(suite);
			String testClass = vals[0];
			String testName =testClass+"."+vals[1]+"."+vals[2];
			for(int i=3;i<vals.length;i++) {
					testName = testName.concat("."+vals[i]);
			}
			newTest.setName(testName);
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("testName", vals[1]);
			parameters.put("testGroup", vals[2]);
			parameters.put("testSchema", vals[3]);
			String parm1 = vals[4];
			if(vals.length>5) {
				for(int i=5;i<vals.length;i++) {
					parm1=parm1+","+vals[i];
				}
			}
			parameters.put("testParm1", parm1);
			newTest.setParameters(parameters);
			testClasses.add(new XmlClass(guiClassPath + testClass));
			newTest.setXmlClasses(testClasses);
			count++;
		}
		suites.add(suite);
			System.out.println(" tests found " + count);
		/*
		 * run suites
		 */
		TestNG tng = new TestNG();
		tng.setUseDefaultListeners(false);
		tng.addListener(new HTMLReporter());
		tng.addListener(new JUnitXMLReporter());
		tng.setXmlSuites(suites);
		tng.setOutputDirectory("output/results/"
				+ new SimpleDateFormat("yyyy-MM-dd.HH.mm.ss")
						.format(new Date()) + suiteName);
		tng.run();
	}
}