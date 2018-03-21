package ca.bc.gov.nrs.dm.microservice.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	public static DateFormat DMS_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	 
	public static boolean isValidDateFormat(String dateInput) {
		boolean result = true;
		try {
			ISO_DATE_FORMAT.parse(dateInput);
    	}catch(Exception e) {
    		result = false;
    	}
		
		return result;
	}
	
	public static String convertISOStringToDMSFormat(String input) {
		String dmsDate = null;
		
		try{
			Date date = ISO_DATE_FORMAT.parse(input);
			dmsDate = DMS_DATE_FORMAT.format(date);
			
		}catch(Exception e) {
			//do nothing
		}
		
		
		return dmsDate;
	}
}
