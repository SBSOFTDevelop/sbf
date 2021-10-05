package ru.sbsoft.system.grid;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * @author balandin
 * @since Aug 7, 2015
 */
@Entity
@IdClass(SYS_FILTER_LOOKUP.SYS_FILTER_LOOKUP_PK.class)
public class SYS_FILTER_LOOKUP implements Serializable {

    public static class SYS_FILTER_LOOKUP_PK implements Serializable {

        private long SYS_OBJECT_STORAGE_RECORD_ID;
        private int SYS_FILTER_NUM;
        private int NUM;

        public SYS_FILTER_LOOKUP_PK() {
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 71 * hash + (int) (this.SYS_OBJECT_STORAGE_RECORD_ID ^ (this.SYS_OBJECT_STORAGE_RECORD_ID >>> 32));
            hash = 71 * hash + this.SYS_FILTER_NUM;
            hash = 71 * hash + this.NUM;
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
            final SYS_FILTER_LOOKUP_PK other = (SYS_FILTER_LOOKUP_PK) obj;
            if (this.SYS_OBJECT_STORAGE_RECORD_ID != other.SYS_OBJECT_STORAGE_RECORD_ID) {
                return false;
            }
            if (this.SYS_FILTER_NUM != other.SYS_FILTER_NUM) {
                return false;
            }
            if (this.NUM != other.NUM) {
                return false;
            }
            return true;
        }
    }

    @Id
    private long SYS_OBJECT_STORAGE_RECORD_ID;
    @Id
    private int SYS_FILTER_NUM;
    @Id
    private int NUM;

    private BigDecimal LOOKUP_RECORD_ID;
    private BigDecimal LOOKUP_ENTITY_ID;
    private String LOOKUP_KEY;
    private String LOOKUP_NAME;

    public SYS_FILTER_LOOKUP() {
    }

    public long getSYS_OBJECT_STORAGE_RECORD_ID() {
        return SYS_OBJECT_STORAGE_RECORD_ID;
    }

    public void setSYS_OBJECT_STORAGE_RECORD_ID(long SYS_OBJECT_STORAGE_RECORD_ID) {
        this.SYS_OBJECT_STORAGE_RECORD_ID = SYS_OBJECT_STORAGE_RECORD_ID;
    }

    public int getSYS_FILTER_NUM() {
        return SYS_FILTER_NUM;
    }

    public void setSYS_FILTER_NUM(int SYS_FILTER_NUM) {
        this.SYS_FILTER_NUM = SYS_FILTER_NUM;
    }

    public int getNUM() {
        return NUM;
    }

    public void setNUM(int NUM) {
        this.NUM = NUM;
    }

    public BigDecimal getLOOKUP_RECORD_ID() {
        return LOOKUP_RECORD_ID;
    }

    public void setLOOKUP_RECORD_ID(BigDecimal LOOKUP_RECORD_ID) {
        this.LOOKUP_RECORD_ID = LOOKUP_RECORD_ID;
    }

    public BigDecimal getLOOKUP_ENTITY_ID() {
        return LOOKUP_ENTITY_ID;
    }

    public void setLOOKUP_ENTITY_ID(BigDecimal LOOKUP_ENTITY_ID) {
        this.LOOKUP_ENTITY_ID = LOOKUP_ENTITY_ID;
    }

    public String getLOOKUP_KEY() {
        return LOOKUP_KEY;
    }

    public void setLOOKUP_KEY(String LOOKUP_KEY) {
        this.LOOKUP_KEY = LOOKUP_KEY;
    }

    public String getLOOKUP_NAME() {
        return LOOKUP_NAME;
    }

    public void setLOOKUP_NAME(String LOOKUP_NAME) {
        this.LOOKUP_NAME = LOOKUP_NAME;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (this.SYS_OBJECT_STORAGE_RECORD_ID ^ (this.SYS_OBJECT_STORAGE_RECORD_ID >>> 32));
        hash = 89 * hash + this.SYS_FILTER_NUM;
        hash = 89 * hash + this.NUM;
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
        final SYS_FILTER_LOOKUP other = (SYS_FILTER_LOOKUP) obj;
        if (this.SYS_OBJECT_STORAGE_RECORD_ID != other.SYS_OBJECT_STORAGE_RECORD_ID) {
            return false;
        }
        if (this.SYS_FILTER_NUM != other.SYS_FILTER_NUM) {
            return false;
        }
        if (this.NUM != other.NUM) {
            return false;
        }
        return true;
    }
}
