package org.lansrod.mra.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ReferentielProdReader {

	public static Map<Integer, BigDecimal> getRefProduit(File file) throws NumberFormatException, IOException {
		
		Map<Integer, BigDecimal> refProd = new HashMap<Integer, BigDecimal>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = br.readLine()) != null) {
			Integer productId = Integer.parseInt(line.split("\\|")[0]);
			BigDecimal prix = new BigDecimal(line.split("\\|")[1]);
			refProd.put(productId,prix);
		}
		return refProd;
	}

}
