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

public class TopCAJ7Extractor {

	public static Logger log = Logger.getLogger(TopCAJ7Extractor.class);

	public static String inputPath;
	public static String outputPath;
	public static Date date;

	/**
	 * map qui contenant les donnees d'un fichier de transaction 
	 */
	public static Map<String, Map<Integer,Integer>> data = new HashMap<String, Map<Integer,Integer>>();

	/**
	 * 	map temporaire pour stoker les donnees d'une magasin et le calcule de prix
	 * liberer des qu'on recupere 100 top ca pour une magasin a un date donnee
	 */
	public static Map<String, Map<Integer,BigDecimal>> tmp = new HashMap<String, Map<Integer,BigDecimal>>();

	/**
	 * map contenant les referentiel produit d'une magasin pour faire le jointure
	 * liberer des que le calcule de jointure est faite
	 */
	public static Map<Integer, BigDecimal> refProd = new HashMap<Integer, BigDecimal>();

	/**
	 * map contenant les top 100 chiffre d'affaire par magasin sur le 7 jour
	 */
	public static Map<String, Map<Integer,BigDecimal>> top100CA = new HashMap<String, Map<Integer,BigDecimal>>();



	/**
	 *  liste qui contient les fichiers des transactions de 7 jours 
	 */
	public static List<File> trxFiles;




	public static void getTop100CAJ7(String inputPath , String outputPath , Date date) throws Exception {

		getTransactionFiles(inputPath, date);

		for (File transactionFile : trxFiles) {

			log.info("processing file :" + transactionFile.getName());
			
			//recupere la date de fichier de transaction
			String trxDate = transactionFile.getName().split("\\_")[1].replaceAll(".data", "");
			
			//remplir le map avec le donnnee de transaction
 			data = TransactionReader.getBataByMagasinID(transactionFile);
 			
 			
			for (Entry<String, Map<Integer, Integer>> entry  : data.entrySet()) {
				//recupere id de magasin
				String id_mag = entry.getKey();	
				//alimenter le map de referentiel produit par id de magasin et la date correspondante
				refProd = ReferentielProdReader.getRefProduit(new File(inputPath+
						"/"+Config.PREFIX_REF_PROD+id_mag
						+"_"+trxDate
						+Config.FILE_EXT));

				//calcule des prix dans la map temporaires
				entry.getValue().entrySet().stream().forEach(en -> {
					Integer productID = en.getKey();
					Integer qt = en.getValue();
					BigDecimal price = refProd.get(productID).multiply(new BigDecimal(qt));
					if(tmp.get(id_mag)!=null)
						tmp.get(id_mag).put(en.getKey(),price);
					else
						tmp.put(id_mag, new HashMap(){{ put(productID, price); }});
				});
				//vider le map de referentiel pour liberer de la memoire
				refProd.clear();
				
				//trie et selection de 100 premier ligne 
				//remplir le map de chiffre d'affaire
				tmp.get(id_mag).entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
				.limit(100)
				.forEach(in->{
					Integer prodID = in.getKey();
					if(top100CA.get(id_mag)!=null) {
						if(top100CA.get(id_mag).get(prodID)!=null) {
							//reduce by product id , somme des quantite
							top100CA.get(id_mag).put(prodID, in.getValue()
									.add(top100CA.get(id_mag).get(prodID)));
						}
						else
							top100CA.get(id_mag).put(prodID, in.getValue());
					}
					else
						top100CA.put(id_mag, new HashMap(){{ put(prodID, in.getValue()); }});
				});
				//vider le map temporaire pour liberer de la memoire 
				tmp.clear();
			}
			//vider le map de transaction
			data.clear();
		}
		
		//trie de map contenant top 100 ca sur 7 jours 
		//selection de 100 premier ligne
		for (Entry<String, Map<Integer, BigDecimal>> entry : top100CA.entrySet()) {
			String id_mag = entry.getKey();
			FileWriter fw = new  FileWriter(outputPath+ "/top_100_ca_"+id_mag+"_"+Config.dateFormat.format(date)+"_J7.data");
			entry.getValue().entrySet().stream().sorted(
					/* trier descendant selon la quantite*/
					Map.Entry.comparingByValue(Collections.reverseOrder()))
			/* selectionner le 100 premier ligne*/
			.limit(100).forEach(line->{
				try {
					fw.write(line.getKey()+"|"+line.getValue()+"\n");
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			});
			//s'assurer que le contenu du buffer est ecrit dans la distination et le vider pour liberer la meroire
			fw.flush();
			// fermer le stream
			fw.close();
		}
	}

//fonction qui permet de collecter les fichiers des transactions sur 7 jours a partir de la 
//date fournie en paramtre 
//si une fichier qui correspond a une date donne n'existe pas alors elle ne sera pas pris en compte 
	
public static List<File> getTransactionFiles(String inputPath , Date date) {
		
		if (trxFiles == null)
			trxFiles = new ArrayList<>();
		
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 7; i++) {
			cal.setTime(date);
			cal.add(Calendar.DATE, i);
			String path = inputPath + "/" + Config.PREFIX_TRX + Config.dateFormat.format(cal.getTime()) + Config.FILE_EXT;
			File transactionFile = new File(path);
			if(transactionFile.exists())
				trxFiles.add(new File(path));
		}
		return trxFiles;
	}

}
