package findyou.testcase;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
//import org.json.JSONObject;
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
import com.qa.utils.HttpClientHelper;
import com.qa.utils.RecordHandler;
import com.qa.utils.SheetUtils;
import com.qa.utils.Utils;
import io.restassured.response.Response;
import net.sf.json.JSONObject;

class interface_gui_test implements ITest  {
	 private Response response;
	    private DataReader myInputData;
	    private DataReader myBaselineData;
	    private String template;
	    private HttpResponse response2;

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
	    	//filePath = "D:/java_workspace/httpAPITest/workbook.xlsx";
	        try {
	            wb = new XSSFWorkbook(new FileInputStream(filePath));
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        //读取Excel (WorkBook) 的 ‘Input’ 和 ‘Baseline’ sheet
	        inputSheet = wb.getSheet("Input");
	        baselineSheet = wb.getSheet("Baseline");

	        //新建‘Output’, ‘Comparison’, ‘Result’ 三个空sheet
	        SheetUtils.removeSheetByName(wb, "Output");
	        SheetUtils.removeSheetByName(wb, "Comparison");
	        SheetUtils.removeSheetByName(wb, "Result");
	        outputSheet = wb.createSheet("Output");
	        comparsionSheet = wb.createSheet("Comparison");
	        resultSheet = wb.createSheet("Result");

	        //读取http_request_template.txt 内容转成string
	        try {
	            InputStream is = NewTest.class.getClassLoader().getResourceAsStream("http_request_template.txt");
	            template = IOUtils.toString(is, Charset.defaultCharset());
	        } catch (Exception e) {
	            AssertJUnit.fail("Problem fetching data from input file:" + e.getMessage());
	        }
	        
	        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        startTime = sf.format(new Date());
	    }

	    /***
	     * 首先用DataReader构造函数，读取Excel中Input的数据，放入HashMap<String, RecordHandler>, Map的key值就是test case的ID，
	     * value是RecordHandler对象，此对象中一个重要的成员属性就是input sheet里面 column和value 的键值对，遍历Map将test case ID 与 test case的value 
	     * 即input sheet前两列的值放入List<Object[]> ，最后返回List的迭代器iterator (为了循环调用@Test方法)
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
	                System.out.print("看看test_id会是啥+++++"+test_ID);
	                String test_case = entry.getValue().get("TestCase");
	                System.out.print("kankan test_case是啥------"+test_case);
	                if (!test_ID.equals("") && !test_case.equals("")) {
	                    test_IDs.add(new Object[] { test_ID, test_case });
	                }
	                totalcase++;
	            }
	            
	            myBaselineData = new DataReader(baselineSheet, true, true, 0);

	        return test_IDs.iterator();
	    }
	    
	    /***
	     * 测试方法，由DataProvider提供数据，首先根据ID去取myInputData里的RecordHandler， 由它和template 去生成request, 
	     * 然后执行request 返回response，这些工作都是由HTTPReqGen.java这个类完成的，借助io.restassured， 
	     * 返回的response是一个JSON体，通过org.skyscreamer.jsonassert.JSONCompare 与Baseline中事先填好的期望结果（同样也是JSON格式）进行比较，
	     * 根据结果是Pass还是Fail, 都会相应的往Excel里的相应Sheet写结果。
	     * @param ID
	     * @param test_case
	     * @throws IOException 
	     * @throws UnsupportedOperationException 
	     */

	    @Test(dataProvider = "WorkBookData", description = "ReqGenTest")
	    public void api_test(String ID, String test_case) throws UnsupportedOperationException, IOException {

	    	//HttpClientHelper myReqGen = new HttpClientHelper();
	    	HashMap<String,String> record = myInputData.get_record(ID).get_map(); 
	    	ztStyle =  wb.createCellStyle();
	    	
	    	StringBuffer resultBuffer = null;	    		
    		BufferedReader br = null;
    		
	    	try {	    		
		    	String call_type,url = new String();
		    	call_type = record.get("call_type");
		    	
		    	url = record.get("host")+record.get("call_suff");
		    	System.out.print("\n URL:\n");
		    	System.out.print(url);
		    	//JSONObject jstring = JSONObject.fromObject(record.get("Body"));
	    		Map<String, Object> params = JSONObject.fromObject(record.get("Body"));
	    		System.out.print("\n http   请求参数MAP ： \n");
	    		System.out.print(params);
		    	if(call_type.equals("POST")) {	
		    		System.out.print("\n http  POST 请求： \n");
		    		response2 = HttpClientHelper.httpClientPost2(url, params, "UTF-8");
		    	}else if (call_type.equals("GET")) {	
		    		System.out.print("\n http  GET 请求： \n");
		    		response2 = HttpClientHelper.httpClientGet2(url, params, "UTF-8");
		    	}	        
	            //myReqGen.generate_request(template, myInputData.get_record(ID));
	            //response = myReqGen.perform_request();
	        } catch (Exception e) {
	            AssertJUnit.fail("Problem using HTTPRequestGenerator to generate response: " + e.getMessage());
	        }
	    	
	        String baseline_message = myBaselineData.get_record(ID).get("Response");
	        //System.out.print(response.statusCode());
	        //System.out.println(response);

            
          /*  br = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
            
			String temp;
			resultBuffer = new StringBuffer();
			while ((temp = br.readLine()) != null) {
				resultBuffer.append(temp);
			}
			System.out.print("\n http:请求结果！\n");
            System.out.print(resultBuffer);
           */ 
	        //if (response !=null && response.statusCode() == 200) {
	        if(response2 !=null && response2.getStatusLine().getStatusCode() == 200) {
	            try {
	            	br = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
	                
	    			String temp;
	    			resultBuffer = new StringBuffer();
	    			while ((temp = br.readLine()) != null) {
	    				resultBuffer.append(temp);
	    			}
	    			System.out.print("\n http:请求结果！\n");
	                System.out.print(resultBuffer);
	                DataWriter.writeData(outputSheet, resultBuffer.toString(), ID, test_case);	                
	                JSONCompareResult result = JSONCompare.compareJSON(baseline_message,  resultBuffer.toString(), JSONCompareMode.NON_EXTENSIBLE);	                
	                if (!result.passed()) {
	                    DataWriter.writeData(comparsionSheet, result, ID, test_case);
	                    DataWriter.writeData(resultSheet, "false", ID, test_case, 0,ztStyle);//1
	                    DataWriter.writeData(outputSheet,ztStyle);
	                    failedcase++;
	                } else {
	                    DataWriter.writeData(resultSheet, "true", ID, test_case, 0,ztStyle);//1
	                }
	            } catch (JSONException e) {
	                DataWriter.writeData(comparsionSheet, "", "Problem to assert Response and baseline messages: "+e.getMessage(), ID, test_case);
	                DataWriter.writeData(resultSheet, "error", ID, test_case, 0,ztStyle);//1
	                failedcase++;
	                AssertJUnit.fail("Problem to assert Response and baseline messages: " + e.getMessage());
	            }finally {
	    			if (br != null) {
	    				try {
	    					br.close();
	    				} catch (IOException e) {
	    					br = null;
	    					throw new RuntimeException(e);
	    				}
	    			}
	    		}
	            
	        }else {
	        	if(response2 != null)        		
	        		DataWriter.writeData(outputSheet, response2.getStatusLine().toString(), ID, test_case);
	     
	            if (response2 != null && baseline_message.equals(response2.getStatusLine().toString())) {
	                DataWriter.writeData(resultSheet, "true", ID, test_case, 0,ztStyle);//1
	            } else {
	            	if(response2 != null)
	            		 res = response2.getStatusLine().toString();           	
	                DataWriter.writeData(comparsionSheet, baseline_message, res, ID, test_case);
	                DataWriter.writeData(resultSheet, "false", ID, test_case, 0,ztStyle);//1
	                DataWriter.writeData(outputSheet,ztStyle);
	                failedcase++;
	            }
	        }
	    }
	    
	    /*
	     * 写入统计的一些数据   关闭文件流
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