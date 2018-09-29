package com.testng.test.maven_testng;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	XmlSuite suite = new XmlSuite();
		suite.setName("Suite");
		XmlTest test = new XmlTest(suite);
		test.setName("Test_interfaceGuiTest");
		List<XmlClass> classes = new ArrayList<XmlClass>();
		classes.add(new XmlClass("com.test.api.interfaceGuiTest"));
		test.setXmlClasses(classes) ;
		        

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);
		TestNG tng = new TestNG();
		tng.setXmlSuites(suites);
		tng.run();
    }
}
