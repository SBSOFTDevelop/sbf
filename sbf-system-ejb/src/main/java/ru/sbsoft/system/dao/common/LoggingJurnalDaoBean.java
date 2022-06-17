/*
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.system.dao.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ru.sbsoft.dao.ILoggingJurnalDao;
import ru.sbsoft.dao.ILoggingSettingsCacheDao;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.consts.LoggingEventType;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.system.common.LoggingJurnalEntity;

/**
 *
 * @author sokolov
 */
@Stateless
@Remote(ILoggingJurnalDao.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@PermitAll
public class LoggingJurnalDaoBean implements ILoggingJurnalDao {

    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager em;

    @Resource
    private SessionContext sessionContext;

    @EJB
    private ILoggingSettingsCacheDao loggingSettingsCacheDao;

    @Override
    public void putEventWithUser(String userName, NamedItem event, LoggingEventType eventType, BigDecimal objectId, IFormModel model) {
        if (loggingSettingsCacheDao.isActive(event.getCode())) {
            LoggingJurnalEntity entity = new LoggingJurnalEntity();
            entity.setCode(event.getCode());
            entity.setEventType(eventType);
            entity.setObjectId(objectId);
            if (model != null) {
                final Gson gson = new GsonBuilder()
                        .registerTypeAdapter(TreeNode.class, new TreeNodeGsonAdapter())
                        .create();
                entity.setModelClass(model.getClass().getName());
                entity.setObjectJson(gson.toJson(model));
            }
            entity.checkTehnologyFields(sessionContext);
            if (userName != null) {
                entity.getCreateInfoFields().setCreateUser(userName);
            }
            em.persist(entity);
        }
    }

    @Override
    public void putEvent(NamedItem event, LoggingEventType eventType, BigDecimal objectId, IFormModel model) {
        putEventWithUser(null, event, eventType, objectId, model);
    }

}
