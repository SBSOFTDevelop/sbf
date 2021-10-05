package ru.sbsoft.system.common;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.dao.entity.IFormEntity;
import ru.sbsoft.shared.consts.CustomReportParamType;

/**
 * Класс сущности Параметр к пользовательскому отчету
 * @author sokolov
 */
@Entity
@Table(name = "SR_REPPARAM")
public class CustomRepparamEntity extends BaseEntity implements IFormEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREPPARAM_GEN")
    @SequenceGenerator(name = "CREPPARAM_GEN", sequenceName = "SYS_SEQUENCE", allocationSize = 1)
    private BigDecimal id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rep_id", nullable = false)
    private CustomReportEntity report;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "param_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomReportParamType paramType;

    @Column(name = "param_name", nullable = false)
    private String name;

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

    public CustomReportParamType getParamType() {
        return paramType;
    }

    public void setParamType(CustomReportParamType paramType) {
        this.paramType = paramType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
