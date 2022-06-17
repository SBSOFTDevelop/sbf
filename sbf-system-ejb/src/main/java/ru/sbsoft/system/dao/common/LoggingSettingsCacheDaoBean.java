/*
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.system.dao.common;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.sbsoft.dao.ILoggingSettingsCacheDao;
import ru.sbsoft.system.common.LoggingSettingsEntity;

/**
 *
 * @author sokolov
 */
@Singleton
@LocalBean
@Remote(ILoggingSettingsCacheDao.class)
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@PermitAll
public class LoggingSettingsCacheDaoBean implements ILoggingSettingsCacheDao {
    
    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager em;
    private final Map<String, Boolean> map = new HashMap<>();
    
    @PostConstruct
    public void init() {
        load();
    }
    
    @Lock(LockType.WRITE)
    @Override
    public void reset() {
        map.clear();
        load();
    }

    @Lock(LockType.READ)
    @Override
    public boolean isActive(String code) {
        Boolean result = map.get(code);
        return result == null ? false : result;
    }
    
    private void load() {
        TypedQuery<LoggingSettingsEntity> query = em.createQuery("select e from LoggingSettingsEntity e", LoggingSettingsEntity.class);
        query.getResultList().forEach(e -> map.put(e.getCode(), e.isActive()));
    }
    
}
