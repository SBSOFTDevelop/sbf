package ru.sbsoft.operation.kladr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import net.sf.sevenzipjbinding.ArchiveFormat;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import ru.sbsoft.common.IO;
import ru.sbsoft.common.Strings;

/**
 * 
 * Класс, представляющий методы чтения, записи 7z архива.
 * 
 * @author balandin
 * @since Mar 13, 2013 3:16:42 PM
 */
public class ArchiveReader {

    private final String ACCESS_MODE = "r";
    private final RandomAccessFile accessFile;
    private ISevenZipInArchive archive;
    private HashMap<String, Integer> cache;
    

    public ArchiveReader(String fileName, ArchiveFormat format) throws FileNotFoundException, SevenZipException {
        this.accessFile = new RandomAccessFile(new File(fileName), ACCESS_MODE);
        try {
            this.archive = SevenZip.openInArchive(format, new RandomAccessFileInStream(accessFile));
        } catch (SevenZipException ex) {
            IO.close(accessFile);
            throw ex;
        }
        initCache();
    }

    public File read(String entryName) throws SevenZipException, IOException {
        String key = entryName.toLowerCase();
        if (!cache.containsKey(key)) {
            throw new SevenZipException("Entry " + entryName + " not found");
        }

        final Integer entryIndex = cache.get(key);
        final File file = File.createTempFile("kladr", "dbf");
        final Writer writer = new Writer(file);
        try {
            archive.extractSlow(entryIndex, writer);
        } catch (SevenZipException exception) {
            file.delete();
            throw exception;
        } finally {
            writer.close();
        }
        file.deleteOnExit();
        return file;
    }

    private void initCache() throws SevenZipException {
        cache = new HashMap<String, Integer>();
        for (int entryIndex = 0; entryIndex < archive.getNumberOfItems(); entryIndex++) {
            final String key = Strings.coalesce(archive.getStringProperty(entryIndex, PropID.PATH)).toLowerCase();
            if (!key.isEmpty()) {
                cache.put(key, entryIndex);
            }
        }
    }

    public static ArchiveFormat selectFormat(String name) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void close() {
        if (archive != null) {
            try {
                archive.close();
                archive = null;
            } catch (SevenZipException ignore) {
            }
        }
        IO.close(accessFile);
    }

    static class Writer implements ISequentialOutStream {

        private final FileOutputStream outputStream;

        private Writer(File file) throws FileNotFoundException {
            this.outputStream = new FileOutputStream(file, false);
        }

        public int write(byte[] bytes) throws SevenZipException {
            try {
                outputStream.write(bytes);
                return bytes.length;
            } catch (IOException ex) {
                throw new SevenZipException(ex);
            }
        }

        private void close() {
            IO.close(outputStream);
        }
    }
}
