package com.qa.utils;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.log4j.Logger;

public class SheetUtils {
	private static Logger logger  = Logger.getLogger(SheetUtils.class);
	
	public static void removeSheetByName(XSSFWorkbook wb, String sheetName) {
		// TODO Auto-generated method stub
		 try {
			    int indexNum = wb.getSheetIndex(sheetName);
			    if(indexNum>0) {
		            //删除Sheet 
		            wb.removeSheetAt(wb.getSheetIndex(sheetName)); 
			    }
			    else {
			    	logger.error("sheet is not exist!");
			    }
	        } catch (Exception e) { 
	        	logger.error("promber is unkown!--",e);
	        } 
	    } 
	}
