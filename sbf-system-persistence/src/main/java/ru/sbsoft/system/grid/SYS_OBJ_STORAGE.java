package ru.sbsoft.system.grid;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

/**
 * @author balandin
 * @since May 18, 2015 3:10:37 PM
 */
@Entity
public class SYS_OBJ_STORAGE implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SYS_OBJ_STORAGE")
    @SequenceGenerator(name = "SYS_OBJ_STORAGE", sequenceName = "SYS_OBJ_STORAGE_ID", allocationSize = 1)
    private long RECORD_ID;
    private long SYS_OBJECT_RECORD_ID;
    private char TYPE;
    private String USER_NAME;
    private String STORAGE_NAME;
    @Lob
    private char[] DATA;
    private int IS_DEFAULT;

    public SYS_OBJ_STORAGE() {
    }

    public long getRECORD_ID() {
        return RECORD_ID;
    }

    public void setRECORD_ID(long RECORD_ID) {
        this.RECORD_ID = RECORD_ID;
    }

    public long getSYS_OBJECT_RECORD_ID() {
        return SYS_OBJECT_RECORD_ID;
    }

    public void setSYS_OBJECT_RECORD_ID(long SYS_OBJECT_RECORD_ID) {
        this.SYS_OBJECT_RECORD_ID = SYS_OBJECT_RECORD_ID;
    }

    public char getTYPE() {
        return TYPE;
    }

    public void setTYPE(char TYPE) {
        this.TYPE = TYPE;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public char[] getDATA() {
        return DATA;
    }

    public void setDATA(char[] DATA) {
        this.DATA = DATA;
    }

    public int getIS_DEFAULT() {
        return IS_DEFAULT;
    }

    public void setIS_DEFAULT(int IS_DEFAULT) {
        this.IS_DEFAULT = IS_DEFAULT;
    }

    public String getSTORAGE_NAME() {
        return STORAGE_NAME;
    }

    public void setSTORAGE_NAME(String STORAGE_NAME) {
        this.STORAGE_NAME = STORAGE_NAME;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.RECORD_ID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SYS_OBJ_STORAGE other = (SYS_OBJ_STORAGE) obj;
        return Objects.equals(this.RECORD_ID, other.RECORD_ID);
    }
}
