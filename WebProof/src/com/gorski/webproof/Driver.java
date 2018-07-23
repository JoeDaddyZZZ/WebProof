package com.gorski.webproof;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.uncommons.reportng.HTMLReporter;
import org.uncommons.reportng.JUnitXMLReporter;

public class Driver {
	static String guiClassPath = "com.gorski.webproof";

    @SuppressWarnings("deprecation")
	public static void main(String[] args) 
			throws FileNotFoundException, IOException {
			List<XmlSuite> suites = new ArrayList<>();
        
        String[] fileArray = {"NOAA.xlsx"};
        //String fileName;
        if(args.length > 0) {
        	//fileName = args[0];
        	fileArray = args;
        //} else {
        	//fileName = fileArray[0];
        }
		/*
		 * initialize suite 
		 */
		for (String fileName : fileArray) {
			XmlSuite suite = new XmlSuite();
			suite.addListener(guiClassPath + ".InitDriverListener");
			suite.setName("Clarity GUI " + fileName);
			File testFile = new File(fileName);
			System.out.println("Running test suite with file: "
					+ testFile.getAbsolutePath());
			FileInputStream file = new FileInputStream(testFile);
			if (".csv".equals(fileName.substring(fileName.lastIndexOf('.')))) {
				for (String name : fileArray) {
					XmlTest newTest = new XmlTest(suite);
					newTest.setName(name.substring(0, name.lastIndexOf('.'))
							+ " Test");
					List<XmlClass> testClasses = new ArrayList<>();
					testClasses.add(new XmlClass(guiClassPath + ".TestCreator"));
					newTest.setXmlClasses(testClasses);
					newTest.addParameter(
							"fileName",
							name.substring(name.lastIndexOf("\\") + 1,
							name.length()));
				}
			} else if (".xlsx".equals(fileName.substring(fileName
					.lastIndexOf('.')))) {
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
					String sheetName = workbook.getSheetAt(i).getSheetName();
					XmlTest newTest = new XmlTest(suite);
					newTest.setName(testFile.getName() + " " + sheetName
							+ " Test");
					List<XmlClass> testClasses = new ArrayList<>();
					testClasses.add(new XmlClass(guiClassPath + ".TestCreator"));
					newTest.setXmlClasses(testClasses);
					newTest.addParameter("fileName", fileName);
					newTest.addParameter("sheetName", sheetName);
				}
				workbook.close();
			} else {
				System.out
						.println("Invalid test file format. Only .csv and .xlsx formats are accepted.");
				file.close();
				return;
			}
			file.close();
			suites.add(suite);
		}
		/*
		 * process properties
		 */
		String platform = null;
       //FileInputStream input = null;
		Properties prop = new Properties();
		prop = InitDriverListener.readProps("res/conf.properties");
		platform = prop.getProperty("useDriver");
        /*
         * run suites
         */
        TestNG tng = new TestNG();
        tng.setUseDefaultListeners(false);
		//tng.addListener(guiClassPath + ".InitDriverListener");
        tng.addListener(new HTMLReporter());
        tng.addListener(new JUnitXMLReporter());
        tng.setXmlSuites(suites);
        tng.setOutputDirectory("output/results/"+new SimpleDateFormat("yyyy-MM-dd.HH.mm.ss").format(new Date())+platform);
        tng.run();
    }
}
