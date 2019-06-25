import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.lansrod.mra.extractor.TopCAJ7Extractor;

public class TopCaTest {
	
	
	@Test
    public void shouldExtracTopCA() throws ParseException, Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File f = new File(classLoader.getResource("transactions_20190629.data").getFile());
		TopCAJ7Extractor.getTop100J7(f.getParent(),
				f.getParent(),
				new SimpleDateFormat("yyyyMMdd").parse("20190629"));
		File expected  = new File(classLoader.getResource("top_ca_expected.data").getFile());
		File generated = new File(f.getParent()+"/20190629/top_100_ca_6776b618-bf03-450c-9400-cdf78e768062_20190629_J7.data");
		
		assertEquals("error!", 
				    FileUtils.readFileToString(expected, "utf-8").trim().replace("\n",""), 
				    FileUtils.readFileToString(generated, "utf-8").trim().replace("\n","")); 
	}
	

}
