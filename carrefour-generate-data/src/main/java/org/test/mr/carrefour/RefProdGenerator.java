package org.test.mr.carrefour;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;

public class RefProdGenerator {

	public static final Logger log = Logger.getLogger(RefProdGenerator.class);

	public static void generate(String path, int nStore,int numProd) throws IOException, ParseException {
		File refMag = null;
		FileWriter fw = null;
		for (int i = 0; i < nStore; i++) {
			String magasinId = UUID.randomUUID().toString();
			for (Date day : Util.getSevenDays()) {
				refMag = new File(path +"/"+Util.PREFIX_MAG + magasinId + "_" + Util.df.format(day) + Util.EXT);
				fw = new FileWriter(refMag);
				log.info("writing to " + refMag.getName());
				StringBuffer str = null;
				for (int j = 1; j <= numProd; j++) {
					str = new StringBuffer();
					str.append(j).append("|").append(Util.randomPrice(100)).append("\n");
					fw.write(str.toString());
					if (j % 1000 == 0) {
						fw.flush();
						log.info(j + " lines are flushed");
					}
				}
				fw.close();
			}
		}
	}

}
