package ru.sbsoft.operation.kladr;

/**
 * Класс управляет хранилищем при импорте справочника КЛАДР.
 * @author balandin
 * @since Mar 15, 2013 4:09:12 PM
 */
public class KLADRStore {

    public static String KLADR_STORE_DIR_1_PARAM = "kladr.store1.dir";
    public static String KLADR_STORE_DIR_2_PARAM = "kladr.store2.dir";
    //
    public static KLADRStore STRORE_A = new KLADRStore(1, 'A');
    public static KLADRStore STRORE_B = new KLADRStore(2, 'B');
    //
    private final Integer index;
    private final char slqPrefix;

    private final String indexDirectoryParam;
    private final String runner;

    public KLADRStore(int index, char slqPrefix) {
        this.index = index;
        this.slqPrefix = slqPrefix;
        this.indexDirectoryParam = getIndexDirParam(index);
        this.runner = "L" + index;
    }

    public Integer getIndex() {
        return index;
    }

    public String getIndexDirectoryParam() {
        return indexDirectoryParam;
    }

    public String wrap(String SQL) {
        return SQL.replaceAll("KD_", "KD" + slqPrefix + "\\_");
    }

    public String getRunner() {
        return runner;
    }

    public char getSlqPrefix() {
        return slqPrefix;
    }

    private String getIndexDirParam(int index) {
        switch (index) {
            case 1:
                return KLADR_STORE_DIR_1_PARAM;
            case 2:
                return KLADR_STORE_DIR_2_PARAM;
            default:
                throw new IllegalArgumentException("Unexpected KLADR store index " + index);
        }
    }
    
    public static KLADRStore indexToStore(int index) {
        switch (index) {
            case 0:
                return null;
            case 1:
                return STRORE_A;
            case 2:
                return STRORE_B;
            default:
                throw new IllegalArgumentException("Unexpected KLADR store index " + index);
        }
    }
}
