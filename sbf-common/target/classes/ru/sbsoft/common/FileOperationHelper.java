package ru.sbsoft.common;

import java.io.File;
import java.util.Objects;

/**
 * Помошник для операций, работающих с файлами
 *
 * @author panarin
 */
public class FileOperationHelper {

	/**
	 * Создаёт директорию для файлового обмена операций или возвращает ранее созданную
	 *
	 * @param sessionContext
	 * @return созданную директорию
	 */
	/*public*/ static File createOperationDir(final String userName) {
		final File operationDir = new File("./operations");
		if (!operationDir.exists()) {
			operationDir.mkdir();
		}
		final File userOperationDir = new File(operationDir, userName);
		if (!userOperationDir.exists()) {
			userOperationDir.mkdir();
		}
		return userOperationDir;
	}

	/**
	 * Создаёт пустую директорию (если директория уже есвть, то чистит её)
	 * @param parentDir
	 * @param dirName
	 * @return созданную директорию
	 */
	/*public*/ static File createEmptyDir(final File parentDir, final String dirName) {
		final File dir = new File(parentDir, dirName);
		if (dir.exists()) {
			delete(dir);
		}
		dir.mkdir();
		return dir;
	}

	private static void delete(final File dir) {
		for (final File child : Objects.requireNonNull(dir.listFiles())) {
			if (child.isDirectory()) {
				delete(child);
			} else {
				child.delete();
			}
		}
		dir.delete();
	}
}
