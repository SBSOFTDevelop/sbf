package ru.sbsoft.system.common;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Класс сущности "Файловое хранилище".
 * <p> Описание полей класса:
 * <ul>
 * <li><code>SR_STORAGE</code>-Файловое хранилище;</li>
 * <li><code>STORAGE_ID</code> -Первичный ключ;</li>
 * <li><code>ALIAS</code> - Алиас записи;</li>
 * <li><code>DESCRIPTION</code>-Описание;</li>
 * <li><code>FILENAME</code>-Имя файла;</li>
 * <li><code>CONTENT</code> - Содержимое фала в блобе;</li>
 * <li><code>CREATE_DATE</code> - Дата создания;</li>
 * <li><code>CREATE_USER</code> - Имя пользователя.</li>
 *
 * </ul>
 * @author balandin
 * @since Apr 4, 2013 2:31:55 PM
 */
@Table(name = "SR_STORAGE")
@Entity
public class StorageEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "SR_STORAGE_SEQGEN", sequenceName = "STORAGE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SR_STORAGE_SEQGEN")
    private Long STORAGE_ID;
    private String ALIAS;
    private String DESCRIPTION;
    private String FILENAME;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] CONTENT;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date CREATE_DATE;
    private String CREATE_USER;

    public StorageEntity() {
    }

    public Long getSTORAGE_ID() {
        return STORAGE_ID;
    }

    public void setSTORAGE_ID(Long STORAGE_ID) {
        this.STORAGE_ID = STORAGE_ID;
    }

    public String getALIAS() {
        return ALIAS;
    }

    public void setALIAS(String ALIAS) {
        this.ALIAS = ALIAS;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public byte[] getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(byte[] CONTENT) {
        this.CONTENT = CONTENT;
    }

    public Date getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(Date CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public String getCREATE_USER() {
        return CREATE_USER;
    }

    public void setCREATE_USER(String CREATE_USER) {
        this.CREATE_USER = CREATE_USER;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final StorageEntity other = (StorageEntity) obj;
        return Objects.equals(this.STORAGE_ID, other.STORAGE_ID);
    }
}
