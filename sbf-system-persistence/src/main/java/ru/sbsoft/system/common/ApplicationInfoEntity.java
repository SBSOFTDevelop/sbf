/*
 * Copyright (c) 2021 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.system.common;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import ru.sbsoft.dao.entity.BaseEntity;

/**
 *
 * @author sokolov
 */
@Entity
@Table(name = "SR_APPINFO")
public class ApplicationInfoEntity extends BaseEntity {

    @Id
    private BigDecimal id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "logo")
    private String logo;
    
    @Override
    public Object getIdValue() {
        return id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
