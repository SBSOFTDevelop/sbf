package ru.sbsoft.operation.kladr;

import ru.sbsoft.operation.kladr.DBFReader;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DBFReaderBankSeekTest {

	private static DBFReader instance = null;

	public DBFReaderBankSeekTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		instance = new DBFReader(DBFReaderBankSeekTest.class.getResourceAsStream("BNKSEEK.DBF"));
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		if (null != instance) {
			instance.close();
		}
	}

	@Test
	public void testGetHeader() throws Exception {
		System.out.println("getHeader");
		DBFReader.Header result = instance.getHeader();
		assertNotNull(result);
		assertEquals(3470L, result.getRecordsCount());
	}

	@Test
	public void testGetRecord() throws Exception {
		System.out.println("getRecord");
		Map result = instance.getRecord();
		assertNotNull(result);
		assertEquals("040009002", result.get("NEWNUM"));
		assertEquals("ПУ БАНКА РОССИИ N 10462", result.get("NAMEP"));
		assertEquals("010462", result.get("IND"));
	}
}