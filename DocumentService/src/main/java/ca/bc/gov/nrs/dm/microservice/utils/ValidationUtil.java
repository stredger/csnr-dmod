package ca.bc.gov.nrs.dm.microservice.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ValidationUtil {

	public static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	 
	public static boolean isValidDateFormat(String dateInput) {
		boolean result = true;
		try {
    		DATE_FORMAT.parse(dateInput);
    	}catch(Exception e) {
    		result = false;
    	}
		
		return result;
	}
}
