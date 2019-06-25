package org.test.mr.carrefour;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Generator {

	
	public static String path ;
	public static  int numberOfSore ;
	public static  int numberOfProduct;
	public static  int NumberOfTransaction ;

	public static List<Date> days ;

	/**
	 * usage:
	 *@param -np,--numberOfProduct <arg>   number of product per magasin
	 *@param -ns,--numberOfStore <arg>     number of store
	 *@param -p,--path <arg>               input path
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		parseArgs(args);
		getDays();
		RefProdGenerator.generate(path,numberOfSore,numberOfProduct);
		TransactionGenerator.generate(path,NumberOfTransaction,numberOfSore,numberOfProduct);
	}


	public static void parseArgs(String args[]) throws Exception {

		Options options = new Options();

		Option p = new Option("p", "path", true, "input path");
		p.setRequired(false);
		options.addOption(p);

		Option nbStore = new Option("ns", "numberOfStore", true, "number of store");
		nbStore.setRequired(true);
		options.addOption(nbStore);


		Option nTransaction = new Option("nt", "numberofTransaction", true, "number of transaction per file");
		nTransaction.setRequired(true);
		options.addOption(nTransaction);

		Option numProduct = new Option("np", "numberOfProduct", true, "number of product per magasin");
		nbStore.setRequired(true);
		options.addOption(numProduct);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);
			System.exit(1);
		}

		path = cmd.getOptionValue("path");


		numberOfProduct = Integer.parseInt(cmd.getOptionValue("np"));
		numberOfSore = Integer.parseInt(cmd.getOptionValue("ns"));
		NumberOfTransaction = Integer.parseInt(cmd.getOptionValue("nt"));

		File folder = new File(path);
		if (!folder.exists())
			folder.mkdirs();
	}


	public static List<Date> getDays() throws ParseException, java.text.ParseException {

		if(days == null) {
			days = new ArrayList();
			Date today = new Date(System.currentTimeMillis());
			Calendar cal = Calendar.getInstance();
			for (int i = 0; i < 7; i++) {
				cal.setTime(today);
				cal.add(Calendar.DATE, i);
				days.add(cal.getTime());
			}
		}
		return days;
		
	}
}
