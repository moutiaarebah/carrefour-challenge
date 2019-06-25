package org.test.mr.carrefour;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class TransactionGenerator {

	public static final Logger log = Logger.getLogger(TransactionGenerator.class);

	public static List<String> magasins = new ArrayList<String>();

	public static void generate(String path, int numtrx, int numStore, int numpr) throws ParseException, IOException {

		getMagasins(path);
		File transactionFile = null;
		FileWriter fw = null;
		StringBuffer trx = null;
		for (Date day : Util.getSevenDays()) {
			transactionFile = new File(path + "/"+Util.PREFIX_TRX + Util.df.format(day) + Util.EXT);
			fw = new FileWriter(transactionFile);
			log.info("writing transactions to " + transactionFile.getName());

			int random = 0;
			int idTransaction = 0;
			for (int i = 1; i < numtrx; i++) {
				idTransaction++;
				random = Util.random(10, 1);
				i+=random-1;
				for (int j = 0; j < random; j++) {
					trx = new StringBuffer();
					trx.append(idTransaction).append("|").append(Util.df.format(day) + "+0200").append("|")
					.append(magasins.get(Util.random(magasins.size() - 1, 0))).append("|")
					.append(Util.random(numpr, 1)).append("|").append(Util.random(10, 1)).append("\n");
					fw.write(trx.toString());
				}
				if (i % 100000 == 0) {
					fw.flush();
				}
			}
			fw.close();
		}
	}

	public static void getMagasins(String path) {

		File directory = new File(path);
		File[] files = directory.listFiles();
		for (File f : files) {
			if (f.getName().contains("ref"))
				if (!magasins.contains(f.getName().split("_")[2]))
					magasins.add(f.getName().split("_")[2]);
		}
	}

}
