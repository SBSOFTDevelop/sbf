package ru.sbsoft.operation.kladr;

import ru.sbsoft.operation.kladr.DBFReader;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DBFReaderSokrBaseTest {

	private static DBFReader instance = null;

	public DBFReaderSokrBaseTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		instance = new DBFReader(DBFReaderSokrBaseTest.class.getResourceAsStream("SOCRBASE.DBF"));
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
		assertEquals(154L, result.getRecordsCount());
	}

	@Test
	public void testGetRecord() throws Exception {
		System.out.println("getRecord");
		Map result = instance.getRecord();
		assertNotNull(result);
		assertEquals("1", result.get("LEVEL"));
		assertEquals("АО", result.get("SCNAME"));
		assertEquals("Автономный округ", result.get("SOCRNAME"));
		assertEquals("101", result.get("KOD_T_ST"));
	}
}