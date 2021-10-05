package ru.sbsoft.common;

import java.io.File;
import java.io.FileWriter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author panarin
 */
public class FileOperationHelperTest {

	public FileOperationHelperTest() {
	}

	@Test
	public void testCreateOperationDir() {
		System.out.println("createOperationDir");
		final String userName = "vova";
		final File expResult = new File("./operations/vova");
		expResult.deleteOnExit();
		final File result = FileOperationHelper.createOperationDir(userName);
		assertEquals(expResult, result);
		assertTrue(result.exists());
	}

	@Test
	public void testCreateEmptyDir() throws Exception {
		System.out.println("createEmptyDir");
		final File parentDir = new File("testDir");
		parentDir.mkdir();
		final String dirName = "newDir";
		final File newDir = new File(parentDir, dirName);
		newDir.mkdir();
		final File childDir = new File(newDir, "childDir");
		childDir.mkdir();
		final File newFile = new File(childDir,"test.txt");
		final FileWriter fileWriter = new FileWriter(newFile);
		fileWriter.write("Тестовая строка");
		fileWriter.flush();
		fileWriter.close();

		final File expResult = new File("testDir/newDir");
        expResult.deleteOnExit();
		final File result = FileOperationHelper.createEmptyDir(parentDir, dirName);
		assertEquals(expResult, result);
		assertTrue(result.exists());
		assertEquals(0, expResult.list().length);
        expResult.delete();
	}
}