package ru.sbsoft.system.grid;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * @author balandin
 * @since May 18, 2015 3:10:28 PM
 */
@Entity
public class SYS_OBJECT implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SYS_OBJECT")
    @SequenceGenerator(name = "SYS_OBJECT", sequenceName = "SYS_OBJECT_ID", allocationSize = 1)
    private long RECORD_ID;
    private char APPLICATION;
    private char TYPE;
    private String ALIAS;
    private String CONTEXT;
    private String MODIFIER;

    public SYS_OBJECT() {
    }

    public long getRECORD_ID() {
        return RECORD_ID;
    }

    public void setRECORD_ID(long RECORD_ID) {
        this.RECORD_ID = RECORD_ID;
    }

    public char getAPPLICATION() {
        return APPLICATION;
    }

    public void setAPPLICATION(char APPLICATION) {
        this.APPLICATION = APPLICATION;
    }

    public char getTYPE() {
        return TYPE;
    }

    public void setTYPE(char TYPE) {
        this.TYPE = TYPE;
    }

    public String getALIAS() {
        return ALIAS;
    }

    public void setALIAS(String ALIAS) {
        this.ALIAS = ALIAS;
    }

    public String getCONTEXT() {
        return CONTEXT;
    }

    public void setCONTEXT(String CONTEXT) {
        this.CONTEXT = CONTEXT;
    }

    public String getMODIFIER() {
        return MODIFIER;
    }

    public void setMODIFIER(String MODIFIER) {
        this.MODIFIER = MODIFIER;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.RECORD_ID ^ (this.RECORD_ID >>> 32));
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
        final SYS_OBJECT other = (SYS_OBJECT) obj;
        return this.RECORD_ID == other.RECORD_ID;
    }
}
