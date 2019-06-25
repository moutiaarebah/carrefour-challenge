package org.lansrod.mra;

import java.io.File;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.lansrod.mra.extractor.TopCAJ7Extractor;
import org.lansrod.mra.extractor.TopCAJ7Extractor;
import org.lansrod.mra.extractor.TopVenteExtractor;

public class Manager {


	public static Logger log = Logger.getLogger(TopCAJ7Extractor.class);

	/**
	 * chemin du fichier de transaction et referentiel produit 
	 */
	private static String inputPath;
	/**
	 * chemin du fichier de resultat 
	 */
	
	private static String outputPath;

	/**
	 * date du  fichier transaction a traiter
	 */
	private static Date date;

	/**
	 * Numero de la tache 1 ou 7 
	 */
	public static int  taskNumber;

	/**
	 * 
	 * @param -d,--date
	 *            <arg> date of the transaction file ( format : yyyyMMdd , required)
	 * @param -i,--input
	 *            <arg> input file path (required)
	 * @param -o,--output
	 *            <arg> output file (optional) default value $HOME/out ,
	 * @param -t,--task
	 *            <arg> Number of task (1 => top_100_ventes_<ID_MAGASIN>_YYYYMMDD.data ,
	 *            						7 =>top_100_ca_<ID_MAGASIN>_YYYYMMDD-J7.data)
	 * @throws IOException
	 * @throws ParseException
	 */

	/**
	 * example  
	 * 
	 * java -jar  <jarname>	-i <path> -d <date> -o <path>  -t <num>
	 *  or 
	 * java -jar  <jarname>	--input <path> --date <date> --output <path>  --task <num>
	 * 
	 * ordre n'est pas important
	 */
	public static void main(String[] args) throws Exception {
		
		// temp de depart
		long start = System.currentTimeMillis();
		//preparation des parametres
		parseArgs(args);
		switch (taskNumber) {
		case 1:
			//calcule de top 100 vente par magasin pour la date de parametre
			TopVenteExtractor.getTop100(inputPath, outputPath, date);
			break;
		case 7:
			//calcule de top 100 chiffre d'affaire par magasin sur 7 jours a partir de la date de parametre
			TopCAJ7Extractor.getTop100CAJ7(inputPath, outputPath, date);
			break;
		default:
			log.error("invalid task number");
			System.exit(-1);
		}
		//temp de fin
		long end  = System.currentTimeMillis();
		//calcule de temp d'execution
		log.info("task completed in " + (end-start)/1000 + " seconds");

	}

	public static void parseArgs(String args[]) throws Exception {

		Options options = new Options();

		Option in = new Option("i", "input", true, "input file path");
		in.setRequired(true);
		options.addOption(in);

		Option out = new Option("o", "output", true, "output file");
		out.setRequired(false);
		options.addOption(out);

		Option d = new Option("d", "date", true, "date of the transaction file (yyyyMMdd) ");
		d.setRequired(true);
		options.addOption(d);

		Option t = new Option("t", "task", true,"Number of task\n"
				+ "1 => top_100_ventes_<ID_MAGASIN>_YYYYMMDD.data\n"
				+ "7 =>top_100_ca_<ID_MAGASIN>_YYYYMMDD-J7.data\n");
		t.setRequired(true);
		options.addOption(t);

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
		inputPath = cmd.getOptionValue("input");
		date = Config.dateFormat.parse(cmd.getOptionValue("date"));

		taskNumber = Integer.parseInt(cmd.getOptionValue("task"));

		if (cmd.hasOption("output"))
			outputPath = cmd.getOptionValue("output");
		else
			outputPath = System.getProperty("user.home") + "/out";

		File outputDirectory = new File(outputPath);
		if (!outputDirectory.exists())
			outputDirectory.mkdirs();
	}

}
