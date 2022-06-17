/*
 * Copyright (c) 2021 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.system.common;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.dao.entity.IFormEntity;
import ru.sbsoft.dao.entity.IUpdateInfoEntity;
import ru.sbsoft.dao.entity.UpdateInfoFields;

/**
 * Класс сущности настроек объектов логгирования
 * 
 * @author sokolov
 */
@Entity
@Table(name = "SR_LOGSETINGS")
public class LoggingSettingsEntity extends BaseEntity implements IFormEntity, IUpdateInfoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOGSETINGS_GEN")
    @SequenceGenerator(name = "LOGSETINGS_GEN", sequenceName = "SYS_SEQUENCE", allocationSize = 1)
    private BigDecimal id;

    @Column(name = "code", nullable = false)
    private String code;
    
    @Column(name = "active", nullable = false)
    private boolean active;
    
    @Embedded
    private UpdateInfoFields updateInfoFields;


    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public UpdateInfoFields getUpdateInfoFields() {
        return updateInfoFields;
    }

    @Override
    public void setUpdateInfoFields(UpdateInfoFields updateInfoFields) {
        this.updateInfoFields = updateInfoFields;
    }
    
}
