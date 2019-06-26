import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.lansrod.mra.extractor.TopVenteExtractor;

public class TopVenteTest {
	
	@Test
    public void shouldExtracTopVente() throws ParseException, Exception {
		
		ClassLoader classLoader = getClass().getClassLoader();
		File f = new File(classLoader.getResource("transactions_20190629.data").getFile());
		TopVenteExtractor.getTop100(f.getParent(),
				f.getParent(),
				new SimpleDateFormat("yyyyMMdd").parse("20190629"));
		File expected  = new File(classLoader.getResource("top_vente_expected.data").getFile());
		File generated = new File(classLoader
				.getResource("top_100_ventes_6776b618-bf03-450c-9400-cdf78e768062_20190629.data").getFile());
//		assertEquals("error!", 
//					 FileUtils.readFileToString(expected, "utf-8").trim(), 
//					 FileUtils.readFileToString(generated, "utf-8").trim()); 
		BufferedReader br1 = new BufferedReader(new FileReader(expected));
		BufferedReader br2 = new BufferedReader(new FileReader(generated));

		String line_expected;
		String line_generated;

		while ((line_expected = br1.readLine()) != null) {
			line_generated = br2.readLine();
			assertEquals(line_expected.trim(), line_generated.trim());

		}
		
	}

}
