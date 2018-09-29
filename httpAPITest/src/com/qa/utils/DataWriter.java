package com.qa.utils;

//import org.apache.poi.POIXMLDocument;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.skyscreamer.jsonassert.JSONCompareResult;

import java.text.DecimalFormat;

import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
//import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow; 
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;


public class DataWriter {
	
	public DataWriter() {
				
	}

	public static void writeData(XSSFSheet outputSheet, JSONCompareResult result, String iD, String test_case) {
		// TODO Auto-generated method stub
		try {
				setRowInt(outputSheet,"ID","TestCase","Assert","Failure field:Value");
 
			   XSSFRow row1 = outputSheet.createRow(outputSheet.getLastRowNum() + 1);  
			   row1.setHeightInPoints((short) 25);  
			   // 给这一行赋值  
			   row1.createCell(0).setCellValue(iD);  
			   row1.createCell(1).setCellValue(test_case); 
			   row1.createCell(2).setCellValue("Actual");
			   row1.createCell(3).setCellValue(result.getActual().toString());
			   
			   XSSFRow row2 = outputSheet.createRow(outputSheet.getLastRowNum() + 2); 
			   row2.createCell(2).setCellValue("Expect");
			   row2.createCell(3).setCellValue(result.getExpected().toString());
			   //FileOutputStream os = new FileOutputStream(filePath);  
			   //wb.write(os);  
			   //is.close();  
			  // os.close();  
			  } catch (Exception e) {  
			   e.printStackTrace();  
			  } 
		
		
	}

	public static void writeData(XSSFSheet outputSheet,CellStyle ztStyle) {

		setRowInt(outputSheet,"ID","TestCase","Response");
		ztStyle.setFillForegroundColor(IndexedColors.RED.getIndex());//PALE_BLUE
        ztStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//BIG_SPOTS  SOLID_FOREGROUND	
                 
        XSSFRow row = outputSheet.getRow(outputSheet.getLastRowNum());  
		Cell color_cell = row.getCell(2);		   			   
		color_cell.setCellStyle(ztStyle);		
		
	}

	public static void writeData(XSSFSheet resultSheet, String result, String iD, String test_case, int i,CellStyle ztStyle) {
		// TODO Auto-generated method stub
		try {
				setRowInt(resultSheet,"ID","TestCase","Result");				 
	            //ztStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
	            //ztStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//BIG_SPOTS  SOLID_FOREGROUND	            
			
			   XSSFRow row = resultSheet.createRow(resultSheet.getLastRowNum() + 1);  
			   row.setHeightInPoints((short) 25);  
			   // 给这一行赋值  
			   row.createCell(0).setCellValue(iD);  
			   row.createCell(1).setCellValue(test_case); 
			   Cell color_cell = row.createCell(2);
			   if(result == "false") {
			       color_cell.setCellValue("Failed");
			       ztStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		           ztStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//BIG_SPOTS  SOLID_FOREGROUND
			       color_cell.setCellStyle(ztStyle);
			   }
			   else if (result == "true") {				   
				   color_cell.setCellValue("Passed");
			       ztStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
	               ztStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//BIG_SPOTS  SOLID_FOREGROUND
	               color_cell.setCellStyle(ztStyle);
			   }
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void writeData(XSSFSheet outputSheet, String asString, String iD, String test_case) {
		// TODO Auto-generated method stub
		
		try { 
				setRowInt(outputSheet,"ID","TestCase","Response");
			  // FileInputStream is = new FileInputStream(filePath); 
			   
			   
			   //Workbook wb =getWorkbook(is);  
			   
			  /* if(!is.markSupported()){    
	                  is = new PushbackInputStream(is,8);             
	              }    
	              if (POIFSFileSystem.hasPOIFSHeader(is)) {       //Excel2003及以下版本    
	                  wb = new HSSFWorkbook(is);    
	              }else if (POIXMLDocument.hasOOXMLHeader(is)) {      //Excel2007及以上版本    */
	                  //wb = new XSSFWorkbook(is);      
	             /* }else{    
	                  throw new IllegalArgumentException("你的Excel版本目前poi无法解析！");                      
	              }*/
			   
			   
			   //System.out.println("aaa");  
			   XSSFSheet sheet1 = outputSheet;//wb.getSheetAt(0);  
			   //System.out.println(sheet1.getLastRowNum());  
			   XSSFRow row = sheet1.createRow(sheet1.getLastRowNum() + 1);  
			   row.setHeightInPoints((short) 25);  
			   // 给这一行赋值  
			   row.createCell(0).setCellValue(iD);  
			   row.createCell(1).setCellValue(test_case);  
			   row.createCell(2).setCellValue(asString);
			   //FileOutputStream os = new FileOutputStream(filePath);  
			   //wb.write(os);  
			   //is.close();  
			  // os.close();  
			  } catch (Exception e) {  
			   e.printStackTrace();  
			  }  
			  
		
		
		
		
		
		/* try {
	           // XSSFWorkbook wb = new XSSFWorkbook();
	            for(int sheetnum=0;sheetnum<map.size();sheetnum++){
	                XSSFSheet sheet = outputSheet;
	                //List<String[]> list = map.get(sheetnum);
	                for(int i=0;i<list.size();i++){
	                    XSSFRow row = sheet.createRow(i);
	                    String[] str = list.get(i);
	                    for(int j=0;j<str.length;j++){
	                        XSSFCell cell = row.createCell(j);
	                        cell.setCellValue(str[j]);
	                    }
	                }
	            }
	            FileOutputStream outputStream = new FileOutputStream(fileName);
	            wb.write(outputStream);
	            outputStream.close();
	        } catch (FileNotFoundException e) {
	            // TODO 自动生成的 catch 块
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO 自动生成的 catch 块
	            e.printStackTrace();
	        }*/
		
	}

	public static void writeData(XSSFSheet comparsionSheet, String str1, String str2, String id, String test_case) {
		// TODO Auto-generated method stub
		try {
			setRowInt(comparsionSheet,"ID","TestCase","Assert","Failure field:Value");
  
			   XSSFRow row1 = comparsionSheet.createRow(comparsionSheet.getLastRowNum() + 1);  
			   row1.setHeightInPoints((short) 25);  
			   // 给这一行赋值  
			   row1.createCell(0).setCellValue(id);  
			   row1.createCell(1).setCellValue(test_case); 
			   row1.createCell(2).setCellValue(str1);
			   row1.createCell(3).setCellValue(str2);
			  } catch (Exception e) {  
			   e.printStackTrace();  
			  } 
		
		
	}

	public static void writeData(XSSFSheet resultSheet, double totalcase, double failedcase, String startTime,
			String endTime) {
		// TODO Auto-generated method stub
		try {
				setRowInt(resultSheet,"ID","TestCase","Result");
			  
			   XSSFRow row1 = resultSheet.createRow(resultSheet.getLastRowNum() + 1);  
			   row1.setHeightInPoints((short) 25);  
			   // 给这一行赋值  
			   row1.createCell(1).setCellValue("Pass Percentage");  
			   double percentage = (totalcase-failedcase)/totalcase;
		        DecimalFormat df = new DecimalFormat("0.00%");
			   row1.createCell(2).setCellValue( df.format(percentage)); 
			   XSSFRow row2 = resultSheet.createRow(resultSheet.getLastRowNum() + 1);  
			   row2.setHeightInPoints((short) 25); 
			   row2.createCell(1).setCellValue("Start Time");  
			   row2.createCell(2).setCellValue(startTime);
			   XSSFRow row3 = resultSheet.createRow(resultSheet.getLastRowNum() + 1);  
			   row3.setHeightInPoints((short) 25); 
			   row3.createCell(1).setCellValue("End Time");  
			   row3.createCell(2).setCellValue(endTime);
			  } catch (Exception e) {  
			   e.printStackTrace();  
			  } 
		
		
	}
	
	public static void setRowInt(XSSFSheet resultSheet,String str1,String str2,String str3 ) {
		
		if(resultSheet.getPhysicalNumberOfRows() == 0) {
			XSSFRow row1 = resultSheet.createRow(0);  
			row1.setHeightInPoints((short) 25);  
			   // 给这一行赋值  
			row1.createCell(0).setCellValue(str1);  
			row1.createCell(1).setCellValue(str2); 
			row1.createCell(2).setCellValue(str3);
		}
		
	}
	
   public static void setRowInt(XSSFSheet resultSheet,String str1,String str2,String str3,String str4 ) {
				
	   if(resultSheet.getPhysicalNumberOfRows() == 0) {
			XSSFRow row1 = resultSheet.createRow(0);  
			row1.setHeightInPoints((short) 25);  
			   // 给这一行赋值  
			row1.createCell(0).setCellValue(str1);  
			row1.createCell(1).setCellValue(str2); 
			row1.createCell(2).setCellValue(str3);
			row1.createCell(3).setCellValue(str4);
		}
		
	}

}
