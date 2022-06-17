package ru.sbsoft.operation.kladr;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ru.sbsoft.common.IO;

/**
 * Класс для чтение dBase-III таблиц в досовской кодировке. 
 * @author balandin
 * @since Feb 7, 2013 3:00:37 PM
 */
public class DBFReader implements Closeable {

    private static final byte SIG_DBASE_III = (byte) 0x03;
    private final byte FIELDS_TERMINATOR = 0x0D;
    private final byte DELETED_RECORD = 0x2A; // *
    private final byte VALID_RECORD = 0x20;
    private final byte END_OF_DATA = 0x1A;
    //
    private static final String CHARSET = "CP866";
    private final DataInputStream stream;
    private Header header;

    public DBFReader(InputStream in) throws IOException {
        this.stream = new DataInputStream(in);
    }

    /**
     * Метод чтения заголовка dBase таблицы из стримера.
     * после чтения указатель <code>startPosition</code> устанавливается
     * на начало таблицы описания полей dbf файла.
     * @return заголовок
     * @throws IOException
     */
    public Header getHeader() throws IOException {
        if (header == null) {
            header = Header.read(this.stream);
            final int tmp = stream.readUnsignedByte();
            if (tmp != FIELDS_TERMINATOR) {
                throw new IOException("TERMINATOR " + tmp);
            }
            int startPosition = this.header.headerLength - (32 + (32 * this.header.size())) - 1;
            if (startPosition > 0) {
                stream.skip(startPosition);
            }
        }
        return header;
    }

    @Override
    public void close() {
        IO.close(stream);
    }

    public static class Header extends ArrayList<Field> {

        private final long recordsCount;
        private final int headerLength;
        private final int recordLength;

        private Header(long recordsCount, int headerLength, int recordLength) {
            this.recordsCount = recordsCount;
            this.headerLength = headerLength;
            this.recordLength = recordLength;
        }

        public static Header read(DataInput dataInput) throws IOException {
            byte[] buffer = new byte[32];
            dataInput.readFully(buffer);
            check(buffer, 0, SIG_DBASE_III);
            check(buffer, 12, 31, (byte) 0);

            Header header = new Header(readInteger(buffer, 4), readShort(buffer, 8), readShort(buffer, 10));
            int fieldsCounth = ((header.headerLength - 1) / 32) - 1;
            for (int i = 0; i < fieldsCounth; i++) {
                header.add(Field.read(dataInput));
            }
            return header;
        }

        public long getRecordsCount() {
            return recordsCount;
        }
    }

    /**
     * Класс представляющий описание поля dBase таблицы.
     * Содержит поля:
     * <ul>
     * <li><code>name</code> - название поля</li>
     * <li><code>type</code> - тип поля (<i>C, D, N</i>)</li>
     * <li><code>length</code> - длина поля</li>
     * <li><code>precision</code> - число знаков после десятичной точки</li>
     * </ul>
     */
    public static class Field {

        private final String name;
        private final byte type;
        private final int length;
        private final byte precision;

        private Field(String name, byte type, int length, byte precision) {
            int space_index = name.indexOf(0);
            if (0 < space_index) {
                this.name = name.substring(0, space_index);
            } else {
                this.name = name;
            }
            this.type = type;
            this.length = length;
            this.precision = precision;
        }

        public static Field read(DataInput dataInput) throws IOException {
            byte[] buffer = new byte[32];
            dataInput.readFully(buffer);
            return new Field(new String(buffer, 0, 11, CHARSET).trim(), buffer[11], buffer[16] & 0xFF, buffer[17]);
        }

        public String getName() {
            return name;
        }

        public byte getType() {
            return type;
        }

        public int getLength() {
            return length;
        }

        public byte getPrecision() {
            return precision;
        }

        @Override
        public String toString() {
            return name + " " + ((char) type) + "[" + length + ((precision != 0) ? ", " + precision : "") + "]";
        }
    }

    public Map<String, Object> getRecord() throws IOException {
        getHeader();
        final byte recordPrefix = stream.readByte();
        switch (recordPrefix) {
            case END_OF_DATA:
                return null;
            case DELETED_RECORD:
                stream.skip(getHeader().recordLength - 1);
                return getRecord();
            case VALID_RECORD:
                return readRecord();
            default:
                throw new IOException("RECORD " + recordPrefix);
        }
    }

    public Map<String, Object> readRecord() throws IOException {
        HashMap<String, Object> result = new HashMap();
        for (Field field : getHeader()) {
            final byte type = field.getType();
            switch (type) {
                case 'C':
                case 'D':
                case 'N':
                    byte[] buffer = new byte[field.getLength()];
                    stream.read(buffer);
                    result.put(field.getName(), new String(buffer, CHARSET).trim());
                    break;
                default:
                    throw new IOException("DbfReader unknown column TYPE " + (char) type);
            }
        }
        return result;
    }

    private static void check(byte[] buffer, int index, int end, byte reference) throws IOException {
        for (int i = index; i <= end; i++) {
            check(buffer, i, reference);
        }
    }

    private static void check(byte[] buffer, int index, byte reference) throws IOException {
        if (buffer[index] != reference) {
            throw new IOException("CHECK [index " + index + "]" + reference);
        }
    }

    private static int readShort(byte[] bytes, int index) {
        int low = bytes[index] & 0xff;
        int high = bytes[index + 1] & 0xff;
        return (high << 8) | low;
    }

    private static long readInteger(byte[] bytes, int index) {
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result = result << 8;
            int tmp = bytes[index + 3 - i] & 0xff;
            result = result | tmp;
        }
        return result;
    }
}
