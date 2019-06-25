package org.lansrod.mra.extractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.lansrod.mra.Config;
import org.lansrod.mra.reader.TransactionReader;


public class TopVenteExtractor {


	public static final Logger log = Logger.getLogger(TopVenteExtractor.class);
	
	/**
	 * map contenant les donnee du fichier de transaction sous la forme <mag_id,<prod_id,qt>> 
	 * grouby(prod_id) , sum(qt)
	 */
	public static Map<String,Map<Integer,Integer>> data = new HashMap<String, Map<Integer,Integer>>();

	
	public static void getTop100(String inputPath , String outputPath , Date date ) throws Exception {
		
		//creation de fichier de transcation transcation_<date>.data
		File transactionFile = new File(
				inputPath+"/"+Config.PREFIX_TRX+Config.dateFormat.format(date)+Config.FILE_EXT);
		
		log.info("processing file " +transactionFile.getPath() + "...");
		
 
		data = TransactionReader.getBataByMagasinID(transactionFile);
		
		log.info("writing data in " + outputPath + "..." );
		 
		for (Entry<String, Map<Integer, Integer>> entry : data.entrySet()) {
			String id_mag = entry.getKey();
			
			
			//ouvrir un stream vers un fichier de la forme outputPath/top_100_vantes_<id_mag>_<yyyyMMdd>.data
			FileWriter fw = new  FileWriter(outputPath+ "/top_100_ventes_"+id_mag+"_"+Config.dateFormat.format(date)+".data");
			
			entry.getValue().entrySet().stream().sorted(
					//trie descendant par valeur (quantité)
					Map.Entry.comparingByValue(Collections.reverseOrder()))
			// selection de 100 premiers lignes
			.limit(100)
			.forEach(line->{
				try {
					//écrire  dans le fichier (prod|qt)
					fw.write(line.getKey()+"|"+line.getValue()+"\n");
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			});
			//s'assurer que le contenu du buffer est ecrit dans la distination et le vider pour liberer la meroire
			fw.flush();
			//fermer le stream
			fw.close();
		}
	}	
}
