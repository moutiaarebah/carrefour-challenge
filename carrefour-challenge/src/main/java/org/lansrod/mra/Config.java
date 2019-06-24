package org.lansrod.mra;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class Config {
	//--------------------------------------------------------------------------
	//   Constants:
	//--------------------------------------------------------------------------
	public static final String PREFIX_TRX = "transactions_";
	public static final String PREFIX_REF_PROD = "reference_prod_";
	public static final String FILE_EXT = ".data";
	
	//--------------------------------------------------------------------------
	//   format de date:
	//--------------------------------------------------------------------------
	public static final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
}
