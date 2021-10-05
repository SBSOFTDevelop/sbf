package ru.sbsoft.system.common;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.dao.entity.IFormEntity;

/**
 * Класс сущности Фильтр к пользовательскому отчету
 * @author sokolov
 */
@Entity
@Table(name = "SR_REPFILTER")
public class CustomRepfilterEntity extends BaseEntity implements IFormEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREPFILTER_GEN")
    @SequenceGenerator(name = "CREPFILTER_GEN", sequenceName = "SYS_SEQUENCE", allocationSize = 1)
    private BigDecimal id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rep_id", nullable = false)
    private CustomReportEntity report;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "val", nullable = false)
    private String value;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public CustomReportEntity getReport() {
        return report;
    }

    public void setReport(CustomReportEntity report) {
        this.report = report;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
