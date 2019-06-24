package org.lansrod.mra.reader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public class TransactionReader {

	public static Map<String, Map<Integer, Integer>> getTransactionData(File transactionFile) throws IOException {
		
		Map<String,Map<Integer,Integer>> data = new HashMap<String, Map<Integer,Integer>>();
		
		// ouvrir un stream 
		Stream<String> stream = Files.lines(transactionFile.toPath());
		
		stream.forEach(line->{
			String[] values = line.split("\\|");
			String id_mag = values[2];
			Integer id_prod = Integer.parseInt(values[3]);
			Integer qt = Integer.parseInt(values[4]);
			if(data.get(id_mag)!=null) {
				if(data.get(id_mag).get(id_prod)!=null)
	
					 //si le magasin existe deja dans le map et le produit existe on somme la quantite
					 
					data.get(id_mag).put(id_prod,qt+data.get(id_mag).get(id_prod));
					
					// si le magasin existe deja dans le map et le produit n'existe pas  on ajoute un nouvelle entree
					 
				else data.get(id_mag).put(id_prod, qt);
			}
			else {
	
				 //si le magasin n'existe pas on ajoute une entree
				data.put(id_mag, new HashMap(){{ put(id_prod, qt); }});
			}
		});
		
		//fermer le stream
		stream.close();
		
		return data;
	}


}
