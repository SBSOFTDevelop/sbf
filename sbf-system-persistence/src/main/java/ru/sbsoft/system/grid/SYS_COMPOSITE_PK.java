package ru.sbsoft.system.grid;

import java.io.Serializable;

/**
 * @author balandin
 * @since Aug 4, 2015
 */
public class SYS_COMPOSITE_PK implements Serializable {

    private long SYS_OBJECT_STORAGE_RECORD_ID;
    private int NUM;

    public SYS_COMPOSITE_PK() {
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + (int) (this.SYS_OBJECT_STORAGE_RECORD_ID ^ (this.SYS_OBJECT_STORAGE_RECORD_ID >>> 32));
        hash = 61 * hash + this.NUM;
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
        final SYS_COMPOSITE_PK other = (SYS_COMPOSITE_PK) obj;
        if (this.SYS_OBJECT_STORAGE_RECORD_ID != other.SYS_OBJECT_STORAGE_RECORD_ID) {
            return false;
        }
        return this.NUM == other.NUM;
    }
    
    public void setSYS_OBJECT_STORAGE_RECORD_ID(long SYS_OBJECT_STORAGE_RECORD_ID) {
        this.SYS_OBJECT_STORAGE_RECORD_ID = SYS_OBJECT_STORAGE_RECORD_ID;
    }

    public long getSYS_OBJECT_STORAGE_RECORD_ID() {
        return SYS_OBJECT_STORAGE_RECORD_ID;
    }

    public void setNUM(int NUM) {
        this.NUM = NUM;
    }

    public int getNUM() {
        return NUM;
    }
}
