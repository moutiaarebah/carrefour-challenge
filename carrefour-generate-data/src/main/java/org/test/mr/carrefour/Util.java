package org.test.mr.carrefour;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Util {
	
	//constants
	public static final String PREFIX_MAG = "reference_prod_";
	public static final String PREFIX_TRX = "transactions_";
	public static final String EXT = ".data";
	
	
	public static   List<String> days  = null;

	public static final DateFormat df = new SimpleDateFormat("yyyyMMdd");


	public static List<Date> getSevenDays() throws ParseException, java.text.ParseException {
		List<Date >days = new ArrayList();
		Date today = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 7; i++) {
			cal.setTime(today);
			cal.add(Calendar.DATE, i);
			days.add(cal.getTime());
		}
		return days;
	}


	public static BigDecimal randomPrice(int range) {
		BigDecimal max = new BigDecimal(range);
		BigDecimal randFromDouble = new BigDecimal(Math.random());
		BigDecimal actualRandomDec = randFromDouble.multiply(max);
		actualRandomDec = actualRandomDec
				.setScale(2, BigDecimal.ROUND_DOWN);
		return actualRandomDec;
	}

	public static int random( int max , int min) {
		return	(int) (( Math.random() *(max - min + 1) ) + min);
	}

}
