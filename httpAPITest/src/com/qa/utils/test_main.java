package com.qa.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class test_main {

	private String call_host = "";
	  private String call_suffix = "";
	  private String call_string = "";
	  private String call_type = "";
	  private String body = "";
	  private Map<String, String> headers = new HashMap<String, String>();
	  private HashMap<String, String> cookie_list = new HashMap<String, String>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String template = "<<call_type>> <<call_stuff>> HTTP/1.1 \r\n"
				+ "Host: <<host>> \r\n"
				+ "Authorization: <<AuthScheme>> <<AuthCreds>>\r\n"
				+ "Accept: <<Accept>> \r\n"
				+ "Content-Type: <<content-Type>>";
		HashMap<String,String> record = new HashMap<String,String>();
		record.put("TestCase",     "TestCase1");     
		record.put("call_type",     "GET");     
		record.put("host",     "http://caipiaoapi10.stg3.24cp.com");     
		record.put("call_stuff",     "/?act=crontab_currentissue");
		record.put("AuthScheme",     "");
		record.put("AuthCreds",     "");
		record.put("Accept",     "json");
		record.put("content-Type",     "application/json");
		record.put("Body",     "");
		
		test_main test = new test_main();
		try {
			test.generate_request(template,record);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void generate_request(String template, HashMap<String, String> record) throws Exception {

	    String filled_template = "";
	    Boolean found_replacement = true;
	    headers.clear();
	    
	    try {
	      
	      // Splits template into tokens, separating out the replacement strings
	      // like <<id>>
	      String[] tokens = tokenize_template(template);
	      System.out.println("tokenwwwww +++");
          System.out.println(Arrays.toString(tokens));
          System.out.println(" +++\r\n");
	      // Repeatedly perform replacements with data from record until no
	      // replacements are found
	      // If a replacement's result is an empty string, it will not throw an
	      // error (but will throw one if there is no column for that result)
	      while(found_replacement) {
	        found_replacement = false;
	        filled_template = "";
	        int f = 0;
	        System.out.println("token +++");
            System.out.println(Arrays.toString(tokens));
            System.out.println(" +++\r\n");
	        for(String item: tokens) {
	     
	          if(item.startsWith("<<") && item.endsWith(">>")) {
	            found_replacement = true;
	            item = item.substring(2, item.length() - 2);
	            
	            if( !record.containsKey(item)) {
	              //logger.error("Template contained replacement string whose value did not exist in input record:[" + item + "]");
	            	System.out.println("Template contained replacement string whose value did not exist in input record:[" + item + "]");
	            }            
	            
	            item = record.get(item);
	          }
	          f++;
	          filled_template += item;
	          System.out.println("filled_template=="+f+" +++");
	          System.out.println(filled_template);
	          System.out.println(" +++\r\n");
	        }
	  
	        tokens = tokenize_template(filled_template);
	      }
	      
	    } catch (Exception e) {
	      //logger.error("Problem performing replacements from template: ", e);
	    	System.out.println("Exception 1");
	    }

	    try {
	      
	      // Feed filled template into BufferedReader so that we can read it line
	      // by line.
	      InputStream stream = IOUtils.toInputStream(filled_template, "UTF-8");
	      BufferedReader in = new BufferedReader(new InputStreamReader(stream));
	      String line = "";
	      String[] line_tokens;
	      
	      // First line should always be call type followed by call suffix
	      line = in.readLine();
	      line_tokens = line.split(" ");
	      call_type = line_tokens[0];
	      call_suffix = line_tokens[1];

	      // Second line should contain the host as it's second token
	      line = in.readLine();
	      line_tokens = line.split(" ");
	      call_host = line_tokens[1];

	      // Full call string for RestAssured will be concatenation of call
	      // host and call suffix
	      call_string = call_host + call_suffix;

	      // Remaining lines will contain headers, until the read line is
	      // empty
	      line = in.readLine();
	      while(line != null && !line.equals("")) {

	        String lineP1 = line.substring(0, line.indexOf(":")).trim();
	        String lineP2 = line.substring(line.indexOf(" "), line.length()).trim();

	        headers.put(lineP1, lineP2);

	        line = in.readLine();
	      }

	      // If read line is empty, but next line(s) have data, create body
	      // from them
	      if(line != null && line.equals("")) {
	        body = "";
	        while( (line = in.readLine()) != null && !line.equals("")) {
	          body += line;
	        }
	      }

	    } catch(Exception e) {
	      //logger.error("Problem setting request values from template: ", e);
	      System.out.println("exception 2");
	    }

	   //return this;
	  }

private String[] tokenize_template(String template) {
    return template.split("(?=[<]{2})|(?<=[>]{2})");
  }


}
