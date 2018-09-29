package com.test.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;
//import javax.xml.parsers.ParserConfigurationException;

//import org.testng.ITestNGListener;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.*;


class api_main {

	public static void main(String[] args) throws SAXException {
		// TODO Auto-generated method stub
		/*TestNG tng = new TestNG();
		tng.run();
		TestListenerAdapter tla = new TestListenerAdapter();
		//XmlSuite xs = new XmlSuite();
		Parser parser = new Parser("../src/main/resources/testng.xml");
	
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		try {
		suites = parser.parseToList();
		Iterator its = suites.iterator();
		while(its.hasNext()) {
			System.out.print(its.next());
		}
		
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		};
		tng.setXmlSuites(suites);
		//tng.addListener(rtl); 
		tng.addListener(tla);
		tng.run(); 
	*/
		
		TestNG testng = new TestNG();
	    //List<String> suites = Lists.newArrayList();
		List<String> suites = new ArrayList<String>();
	    suites.add("src/main/resources/testng.xml");
	    testng.setTestSuites(suites);
	    testng.run();
		
	    
	    
		/*XmlSuite suite = new XmlSuite();  
        
        suite.setName("Api gui tests");  
        suite.setVerbose(1);  
        suite.setThreadCount(2);  
          
        Map<String, String> parameters = new HashMap<String, String>();  
        parameters.put("workBook", "D:/java_workspace/httpAPITest/workbook.xlsx");  
        suite.setParameters(parameters);  
          
        XmlTest test = new XmlTest();  
        test.setName("Test_interfaceGuiTest");  
        //test.setParallel("methods");  
        //test.setExcludedGroups(Arrays.asList(  
       //     new String[] {"excludeThisGroup"}  
       // ));  
          
        XmlClass[] classes = new XmlClass[] {  
            new XmlClass("com.test.api.interfaceGuiTest"),  
            //new XmlClass("testng.Test2")  
        };  
        test.setXmlClasses(Arrays.asList(classes));  
          
        TestNG tng = new TestNG();  
        tng.setXmlSuites(Arrays.asList(new XmlSuite[] {suite}));  
          
        tng.run();
	   */
	}

	

}
