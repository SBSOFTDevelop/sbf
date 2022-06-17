package ru.sbsoft.system.grid;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * @author balandin
 * @since Aug 4, 2015
 */
@Entity
@IdClass(SYS_COMPOSITE_PK.class)
public class SYS_FILTER implements Serializable {

    @Id
    private long SYS_OBJECT_STORAGE_RECORD_ID;
    @Id
    private int NUM;
    private int PARENT_NUM;
    //
    private int NOPE; // BOOL
    private int SENS; // BOOL
    //
    private String TYPE;
    private String CONDITION;
    private String PARAM1;
    private String PARAM2;
    private String PARAM3;

    public SYS_FILTER() {
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

    public int getPARENT_NUM() {
        return PARENT_NUM;
    }

    public void setPARENT_NUM(int PARENT_NUM) {
        this.PARENT_NUM = PARENT_NUM;
    }

    public int getNOPE() {
        return NOPE;
    }

    public void setNOPE(int NOPE) {
        this.NOPE = NOPE;
    }

    public int getSENS() {
        return SENS;
    }

    public void setSENS(int SENS) {
        this.SENS = SENS;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getCONDITION() {
        return CONDITION;
    }

    public void setCONDITION(String CONDITION) {
        this.CONDITION = CONDITION;
    }

    public String getPARAM1() {
        return PARAM1;
    }

    public void setPARAM1(String PARAM1) {
        this.PARAM1 = PARAM1;
    }

    public String getPARAM2() {
        return PARAM2;
    }

    public void setPARAM2(String PARAM2) {
        this.PARAM2 = PARAM2;
    }

    public String getPARAM3() {
        return PARAM3;
    }

    public void setPARAM3(String PARAM3) {
        this.PARAM3 = PARAM3;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (int) (this.SYS_OBJECT_STORAGE_RECORD_ID ^ (this.SYS_OBJECT_STORAGE_RECORD_ID >>> 32));
        hash = 73 * hash + this.NUM;
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
        final SYS_FILTER other = (SYS_FILTER) obj;
        if (this.SYS_OBJECT_STORAGE_RECORD_ID != other.SYS_OBJECT_STORAGE_RECORD_ID) {
            return false;
        }
        return this.NUM == other.NUM;
    }
}
