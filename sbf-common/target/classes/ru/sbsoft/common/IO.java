package ru.sbsoft.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

/**
 * @author balandin
 * @since Mar 13, 2013 2:19:49 PM
 */
public class IO {

    private static final int BUFFER_SIZE = 32 * 1024;

    public static String extractFileExtension(String fileName) {
        int n = fileName.lastIndexOf('.');
        return n == -1 ? null : fileName.substring(n + 1);
    }

    public static String trimFileExtension(String fileName) {
        int n = fileName.lastIndexOf('.');
        return n == -1 ? fileName : fileName.substring(0, n);
    }

    public static void close(OutputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignore) {
            }
        }
    }

    public static void close(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignore) {
            }
        }
    }

    public static void close(PrintWriter writer) {
        if (writer != null) {
            writer.close();
        }
    }

    public static void close(RandomAccessFile file) {
        if (file != null) {
            try {
                file.close();
            } catch (IOException ignore) {
            }
        }
    }

    public static void copy(InputStream src, ObjectOutput dest) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int n;
        while ((n = src.read(buffer)) >= 0) {
            dest.write(buffer, 0, n);
        }
    }

    public static void copy(ObjectInput src, OutputStream dest) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int n;
        while ((n = src.read(buffer)) >= 0) {
            dest.write(buffer, 0, n);
        }
    }

    public static void copy(InputStream src, OutputStream dest) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int n;
        while ((n = src.read(buffer)) >= 0) {
            dest.write(buffer, 0, n);
        }
    }

    public static void copy(InputStream src, File dest) throws IOException {
        FileOutputStream destStream = null;
        try {
            destStream = new FileOutputStream(dest);
            copy(src, destStream);
        } finally {
            close(destStream);
        }
    }

    public static void delete(File file) {
        if (file != null && file.exists() && file.isFile()) {
            if (!file.delete()) {
                file.deleteOnExit();
            }
        }
    }

    public static void checkFilesNotEmpty(int count) throws IOException {
        if (count < 1) {
            throw new IOException("Файл не найден");
        }
    }

    public static void checkFileUnique(int count) throws IOException {
        checkFilesNotEmpty(count);
        if (count > 1) {
            throw new IOException("Найдено более одного файла");
        }
    }

    public static byte[] readFile(File file) throws IOException {
        BufferedInputStream bufferedInputStream = null;
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            byteArrayOutputStream = new ByteArrayOutputStream(fileInputStream.available());
            copy(bufferedInputStream, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } finally {
            close(bufferedInputStream);
            close(fileInputStream);
            close(byteArrayOutputStream);
        }
    }

    public static File saveFile(byte[] content, File file) throws IOException {
        ByteArrayInputStream byteArrayInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(content);
            fileOutputStream = new FileOutputStream(file);
            copy(byteArrayInputStream, fileOutputStream);
            return file;
        } finally {
            close(byteArrayInputStream);
            close(fileOutputStream);
        }
    }
}
