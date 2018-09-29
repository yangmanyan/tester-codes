package com.test.api;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;


import com.qa.utils.DataReader;
import com.qa.utils.DataWriter;
import com.qa.utils.HTTPReqGen;
import com.qa.utils.RecordHandler;
import com.qa.utils.SheetUtils;
import com.qa.utils.Utils;
import io.restassured.response.Response;

public class interfaceGuiTest implements ITest {

    private Response response;
    private DataReader myInputData;
    private DataReader myBaselineData;
    private String template;
    
    

    public String getTestName() {
        return "API Test";
    }
    
    String filePath = "";
    String res = "";
    
    XSSFWorkbook wb = null;
    XSSFSheet inputSheet = null;
    XSSFSheet baselineSheet = null;
    XSSFSheet outputSheet = null;
    XSSFSheet comparsionSheet = null;
    XSSFSheet resultSheet = null;
    CellStyle ztStyle = null;
    
    
    private double totalcase = 0;
    private double failedcase = 0;
    private String startTime = "";
    private String endTime = "";

    
    @BeforeTest
    @Parameters("workBook")
    public void setup(String path) {
        filePath = path;
    	
        try {
            wb = new XSSFWorkbook(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        inputSheet = wb.getSheet("Input");
        baselineSheet = wb.getSheet("Baseline");

        
        SheetUtils.removeSheetByName(wb, "Output");
        SheetUtils.removeSheetByName(wb, "Comparison");
        SheetUtils.removeSheetByName(wb, "Result");
        outputSheet = wb.createSheet("Output");
        comparsionSheet = wb.createSheet("Comparison");
        resultSheet = wb.createSheet("Result");

      
        try {
            InputStream is = interfaceGuiTest.class.getClassLoader().getResourceAsStream("http_request_template.txt");
            //template = IOUtils.toString(is, Charset.defaultCharset());
            template = IOUtils.toString(is);
        } catch (Exception e) {
            AssertJUnit.fail("Problem fetching data from input file:" + e.getMessage());
        }
        
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = sf.format(new Date());
    }

    /***
     * @param context
     * @return
     */
    @Test
	@DataProvider(name = "WorkBookData")
    
    protected Iterator<Object[]> testProvider(ITestContext context) {

        List<Object[]> test_IDs = new ArrayList<Object[]>();
        	
            myInputData = new DataReader(inputSheet, true, true, 0);
            Map<String, RecordHandler> myInput = myInputData.get_map();

            // sort map in order so that test cases ran in a fixed order
            Map<String, RecordHandler> sortmap = Utils.sortmap(myInput);
            
            for (Map.Entry<String, RecordHandler> entry : sortmap.entrySet()) {
                String test_ID = entry.getKey();
                System.out.print("to see what is test_id+++++"+test_ID);
                String test_case = entry.getValue().get("TestCase");
                System.out.print("to see what is test_case++++"+test_case);
                if (!test_ID.equals("") && !test_case.equals("")) {
                    test_IDs.add(new Object[] { test_ID, test_case });
                }
                totalcase++;
            }
            
            myBaselineData = new DataReader(baselineSheet, true, true, 0);

        return test_IDs.iterator();
    }
    
    /***

     * @param ID
     * @param test_case
     */

    @Test(dataProvider = "WorkBookData", description = "ReqGenTest")
    public void api_test(String ID, String test_case) {
    	ztStyle =  wb.createCellStyle();
        HTTPReqGen myReqGen = new HTTPReqGen();
        System.out.print("\r\nTest faction is run !!!\r\n");
        System.out.print(test_case+"\r\n");
        try {
            myReqGen.generate_request(template, myInputData.get_record(ID));
            response = myReqGen.perform_request();
        } catch (Exception e) {
            AssertJUnit.fail("Problem using HTTPRequestGenerator to generate response: " + e.getMessage());
        }
        
        String baseline_message = myBaselineData.get_record(ID).get("Response");
        //System.out.print(response.statusCode());
        //System.out.println(response);

        if (response !=null && response.statusCode() == 200) {
            try {
            	
                DataWriter.writeData(outputSheet, response.asString(), ID, test_case);
                
                JSONCompareResult result = JSONCompare.compareJSON(baseline_message, response.asString(), JSONCompareMode.NON_EXTENSIBLE);
                
                if (!result.passed()) {
                    DataWriter.writeData(comparsionSheet, result, ID, test_case);
                    DataWriter.writeData(resultSheet, "false", ID, test_case, 0,ztStyle);
                    DataWriter.writeData(outputSheet,ztStyle);
                    failedcase++;
                } else {
                    DataWriter.writeData(resultSheet, "true", ID, test_case, 0,ztStyle);
                }
            } catch (JSONException e) {
                DataWriter.writeData(comparsionSheet, "", "Problem to assert Response and baseline messages: "+e.getMessage(), ID, test_case);
                DataWriter.writeData(resultSheet, "error", ID, test_case, 0,ztStyle);
                failedcase++;
                AssertJUnit.fail("Problem to assert Response and baseline messages: " + e.getMessage());
            }
        }else {
        	if(response!=null)  
        		
        		DataWriter.writeData(outputSheet, response.statusLine(), ID, test_case);
     
            if (response!=null && baseline_message.equals(response.statusLine())) {
                DataWriter.writeData(resultSheet, "true", ID, test_case, 0,ztStyle);
            } else {
            	if(response!=null)
            		 res = response.statusLine();           	
                DataWriter.writeData(comparsionSheet, baseline_message, res, ID, test_case);
                DataWriter.writeData(resultSheet, "false", ID, test_case, 0,ztStyle);
                DataWriter.writeData(outputSheet,ztStyle);
                failedcase++;
            }
        }
    }
    
    /*
     *
     */

    @AfterTest
    public void teardown() {
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime = sf.format(new Date());
        DataWriter.writeData(resultSheet, totalcase, failedcase, startTime, endTime);
        
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            wb.write(fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}