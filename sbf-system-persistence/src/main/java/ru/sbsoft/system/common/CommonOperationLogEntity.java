package ru.sbsoft.system.common;

import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.SessionContext;
import javax.persistence.Basic;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.session.SessionUtils;
import ru.sbsoft.shared.model.OperationEventType;

@MappedSuperclass
public abstract class CommonOperationLogEntity extends BaseEntity {

    public static final String OPERATION_GENERATOR = "CommonOperationLogEntityGenerator";

    @Id
    @SequenceGenerator(name = OPERATION_GENERATOR, sequenceName = "SR_SEQUENCE_LOG", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = OPERATION_GENERATOR)
    private BigDecimal RECORD_ID;
    @Enumerated(EnumType.STRING)
    private OperationEventType TYPE_VALUE;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String TRACE;
    private String MESSAGE;
    private String CREATE_USER;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date CREATE_DATE;

    @Override
    public Object getIdValue() {
        return RECORD_ID;
    }

    public BigDecimal getRECORD_ID() {
        return RECORD_ID;
    }

    public void setRECORD_ID(BigDecimal RECORD_ID) {
        this.RECORD_ID = RECORD_ID;
    }

    @Override
    public void checkTehnologyFields(final SessionContext sessionContext) {
        CREATE_USER = SessionUtils.getCurrentUserName(sessionContext);
        CREATE_DATE = new Date();
    }

    public OperationEventType getTYPE_VALUE() {
        return TYPE_VALUE;
    }

    public void setTYPE_VALUE(OperationEventType TYPE_VALUE) {
        this.TYPE_VALUE = TYPE_VALUE;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = (MESSAGE == null) ? null : MESSAGE.substring(0, Math.min(MESSAGE.length(), 2048));
    }

    public String getCREATE_USER() {
        return CREATE_USER;
    }

    public void setCREATE_USER(String CREATE_USER) {
        this.CREATE_USER = CREATE_USER;
    }

    public Date getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(Date CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 59 * hash + (this.RECORD_ID != null ? this.RECORD_ID.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        if (obj instanceof CommonOperationLogEntity) {
//            final CommonOperationLogEntity other = (CommonOperationLogEntity) obj;
//            return this.RECORD_ID == other.RECORD_ID || (this.RECORD_ID != null && this.RECORD_ID.equals(other.RECORD_ID));
//        }
//        return false;
//    }
    public String getTRACE() {
        return TRACE;
    }

    public void setTRACE(String TRACE) {
        this.TRACE = TRACE;
    }
}
