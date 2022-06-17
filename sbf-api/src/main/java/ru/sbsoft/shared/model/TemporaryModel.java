package ru.sbsoft.shared.model;

import java.math.BigDecimal;
import java.util.Date;

public class TemporaryModel extends TehnologyModel {

    private BigDecimal TEMPORAL_ENTITY_ID;
    private String CODE_VALUE;
    private Date DATE_START;
    private Date DATE_END;

    public TemporaryModel() {
    }

    public TemporaryModel(final TemporaryModel model) {
        this.TEMPORAL_ENTITY_ID = model.TEMPORAL_ENTITY_ID;
        this.CODE_VALUE = model.CODE_VALUE;
    }

    public BigDecimal getTEMPORAL_ENTITY_ID() {
        return TEMPORAL_ENTITY_ID;
    }

    public void setTEMPORAL_ENTITY_ID(BigDecimal TEMPORAL_ENTITY_ID) {
        this.TEMPORAL_ENTITY_ID = TEMPORAL_ENTITY_ID;
    }

    public String getCODE_VALUE() {
        return CODE_VALUE;
    }

    public void setCODE_VALUE(String CODE_VALUE) {
        this.CODE_VALUE = CODE_VALUE;
    }

    public Date getDATE_END() {
        return DATE_END;
    }

    public void setDATE_END(Date DATE_END) {
        this.DATE_END = DATE_END;
    }

    public Date getDATE_START() {
        return DATE_START;
    }

    public void setDATE_START(Date DATE_START) {
        this.DATE_START = DATE_START;
    }
}
