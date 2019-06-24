package org.lansrod.mra.extractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.lansrod.mra.Config;
import org.lansrod.mra.reader.ReferentielProdReader;
import org.lansrod.mra.reader.TransactionReader;

public class Top100CAJ7Extractor {

	public static Logger log = Logger.getLogger(Top100CAJ7Extractor.class);
	
	/**
	 * map qui contenant les donnees d'un fichier de transaction liberer a la fin de
	 * traitement d'un fichier de transaction <magasin_id,<produit_id,quantite>>
	 */
	public static Map<String, Map<Integer, Integer>> dataTrx = new HashMap<String, Map<Integer, Integer>>();

	/**
	 * map contenant les referentiel produit d'une magasin pour faire le jointure
	 * liberer des que les calcule sur les prix est fait <produit,prix>
	 */
	public static Map<Integer, BigDecimal> refProd = new HashMap<Integer, BigDecimal>();

	/**
	 * map contenant le chiffre d'affaire par produit  <prod_id ,
	 * quantite*prix>> liberer a la fin de traitement du magasin
	 */
	public static Map<Integer, BigDecimal> dataCA = new HashMap<Integer, BigDecimal>();

	/**
	 * liste qui contient 7 fichiers des transactions sur 7 jours
	 */
	public static List<File> trxFiles;

	public static void getTop100CAJ7(String inputPath , String outputPath , Date date ) throws Exception {

		getTransactionFiles(inputPath,date);

		for (File trxFile : trxFiles) {
			
			log.info("processing file :" + trxFile.getName());

			String trxDate = trxFile.getName().split("_")[1].replaceAll(".data", "");
			
			//alimenter le map de transaction
			dataTrx = TransactionReader.getTransactionData(trxFile);

			for (Entry<String, Map<Integer, Integer>> entry : dataTrx.entrySet()) {
				
				String id_mag = entry.getKey();
				
				//alimenter le map referentiel produit
				refProd = ReferentielProdReader.getRefProduit(
						new File(inputPath + "/" + Config.PREFIX_REF_PROD + id_mag + "_" + trxDate + Config.FILE_EXT));
				
				
				//alimenter le map de chiffre d'affaire 
				entry.getValue().entrySet().stream().forEach(en -> {
					Integer productID = en.getKey();
					Integer qt = en.getValue();
					BigDecimal price = refProd.get(productID).multiply(new BigDecimal(qt));
					dataCA.put(productID, price);
				});
				
				// vider le map de referentiel produit
				refProd.clear();
				
				//creation de dossier pour partitionner le resultat selon la date
				File folder = new File(outputPath + "/" + trxDate);

				if (!folder.exists()) 
					folder.mkdirs();

				//ouvrir un stream vers un fichier de la forme outputPath/top_100_ca_<id_mag>_<yyyyMMdd>_J7.data
				FileWriter fw = new FileWriter(
						folder.getAbsolutePath() + "/top_100_ca_" + id_mag + "_" + trxDate + "_J7.data");
				

				dataCA.entrySet().stream().sorted(
						/* trie descendant selon le chiffre d'affaire */
						Map.Entry.comparingByValue(Collections.reverseOrder()))
						/* selection de 100 premier ligne */
						.limit(100).forEach(ln -> {
							try {
								//ecrire dans le fichier (produit|ca)
								fw.write(ln.getKey() + "|" + ln.getValue() + "\n");
							} catch (IOException e) {
								log.error(e.getMessage());
							}
						});
				//s'assurer que le contenu du buffer est ecrit dans la distination et le vider pour liberer la meroire
				fw.flush();
				// fermer le stream
				fw.close();
				//vider le map de chiffre d'affaire 
				dataCA.clear();
			}
			//vider le map de transaction

			dataTrx.clear();
		}

	}

	public static List<File> getTransactionFiles(String inputPath , Date date) {
		
		if (trxFiles == null)
			trxFiles = new ArrayList<>();
		
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 7; i++) {
			cal.setTime(date);
			cal.add(Calendar.DATE, i);
			String path = inputPath + "/" + Config.PREFIX_TRX + Config.dateFormat.format(cal.getTime()) + Config.FILE_EXT;
			trxFiles.add(new File(path));
		}
		
		return trxFiles;
	}

}
