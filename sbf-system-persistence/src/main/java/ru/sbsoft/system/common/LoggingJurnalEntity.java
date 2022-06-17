/*
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.system.common;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.dao.entity.CreateInfoFields;
import ru.sbsoft.dao.entity.ICreateInfoEntity;
import ru.sbsoft.dao.entity.IFormEntity;
import ru.sbsoft.shared.consts.LoggingEventType;

/**
 * Класс сущности журнала логгирования событий
 * 
 * @author sokolov
 */
@Entity
@Table(name = "SR_LOGJURNAL")
public class LoggingJurnalEntity extends BaseEntity implements IFormEntity, ICreateInfoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOGJURNAL_GEN")
    @SequenceGenerator(name = "LOGJURNAL_GEN", sequenceName = "SR_LOGJURNAL_SEQ", allocationSize = 1)
    private BigDecimal id;

    @Column(name = "code", nullable = false)
    private String code;
    
    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoggingEventType eventType;
    
    @Column(name = "object_id", nullable = true)
    private BigDecimal objectId;
    
    @Column(name = "object_json", nullable = true)
    private String objectJson;

    @Column(name = "model_class", nullable = true)
    private String modelClass;

    @Embedded
    private CreateInfoFields createInfoFields;

    @Override
    public Object getIdValue() {
        return id;
    }

    @Override
    public CreateInfoFields getCreateInfoFields() {
        return createInfoFields;
    }

    @Override
    public void setCreateInfoFields(CreateInfoFields createInfoFields) {
        this.createInfoFields = createInfoFields;
    }

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

    public LoggingEventType getEventType() {
        return eventType;
    }

    public void setEventType(LoggingEventType eventType) {
        this.eventType = eventType;
    }

    public BigDecimal getObjectId() {
        return objectId;
    }

    public void setObjectId(BigDecimal objectId) {
        this.objectId = objectId;
    }

    public String getObjectJson() {
        return objectJson;
    }

    public void setObjectJson(String objectJson) {
        this.objectJson = objectJson;
    }

    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass(String modelClass) {
        this.modelClass = modelClass;
    }
    
}
