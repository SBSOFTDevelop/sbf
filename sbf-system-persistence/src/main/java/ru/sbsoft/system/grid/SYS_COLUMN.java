package ru.sbsoft.system.grid;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * @author balandin
 * @since Aug 7, 2015
 */
@Entity
@IdClass(SYS_COMPOSITE_PK.class)
public class SYS_COLUMN implements Serializable {

    @Id
    private long SYS_OBJECT_STORAGE_RECORD_ID;
    @Id
    private int NUM;
    //
    private String ALIAS;
    private int VISIBLE; // BOOL
    private int WIDTH;

    public SYS_COLUMN() {
    }

    public long getSYS_OBJECT_STORAGE_RECORD_ID() {
        return SYS_OBJECT_STORAGE_RECORD_ID;
    }

    public void setSYS_OBJECT_STORAGE_RECORD_ID(long SYS_OBJECT_STORAGE_RECORD_ID) {
        this.SYS_OBJECT_STORAGE_RECORD_ID = SYS_OBJECT_STORAGE_RECORD_ID;
    }

    public int getNUM() {
        return NUM;
    }

    public void setNUM(int NUM) {
        this.NUM = NUM;
    }

    public String getALIAS() {
        return ALIAS;
    }

    public void setALIAS(String ALIAS) {
        this.ALIAS = ALIAS;
    }

    public int getVISIBLE() {
        return VISIBLE;
    }

    public void setVISIBLE(int VISIBLE) {
        this.VISIBLE = VISIBLE;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (this.SYS_OBJECT_STORAGE_RECORD_ID ^ (this.SYS_OBJECT_STORAGE_RECORD_ID >>> 32));
        hash = 53 * hash + this.NUM;
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
        final SYS_COLUMN other = (SYS_COLUMN) obj;
        if (this.SYS_OBJECT_STORAGE_RECORD_ID != other.SYS_OBJECT_STORAGE_RECORD_ID) {
            return false;
        }
        return this.NUM == other.NUM;
    }
}
