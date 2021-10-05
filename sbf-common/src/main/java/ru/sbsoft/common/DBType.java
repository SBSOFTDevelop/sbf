package ru.sbsoft.common;

/**
 *
 * @author sychugin
 */
public enum DBType {

    DBTYPE_ORACLE("oracle"),
    DBTYPE_MSSQL("microsoft sql server"),
    DBTYPE_POSTGRES("postgresql"),
    DBTYPE_DB2("db2");
    //DBTYPE_UNKNOWN("unknown");
    private final String dbName;

    private static DBType currentType = null;

//    public static void setCurrentType(DBType currentType) {
//
//        if (DBType.currentType == null) {
//            DBType.currentType = currentType;
//        }
//    }
    public static DBType getCurrentType() {
        return currentType;
    }

    private DBType(String dbName) {
        this.dbName = dbName;
    }

    public static void setCurrentType(String dbName) {
        
        if (null == dbName) {
            return;
        }

        dbName = dbName.toLowerCase();
        if (dbName.indexOf("db2/") == 0) {
            dbName = "db2";
        }
        for (DBType e : DBType.values()) {
            if (e.dbName.equals(dbName.toLowerCase())) {
                currentType = e;
                return;
            }
        }

    }

//    public static DBType getTypeByName(String dbName) {
//        if (null == dbName) {
//            return DBTYPE_UNKNOWN;
//        }
//        if (dbName.indexOf("db2/") == 0) {
//            dbName = "db2";
//        }
//        for (DBType e : DBType.values()) {
//            if (e.dbName.equals(dbName.toLowerCase())) {
//                return e;
//            }
//        }
//        return DBTYPE_UNKNOWN;
//    }

    public String getDbName() {
        return dbName;
    }

}
