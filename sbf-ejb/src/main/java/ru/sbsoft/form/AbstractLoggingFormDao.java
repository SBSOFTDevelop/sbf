/*
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.form;

import java.math.BigDecimal;
import ru.sbsoft.dao.ILoggingJurnalDao;
import ru.sbsoft.dao.entity.IFormEntity;
import ru.sbsoft.generator.api.Lookup;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.consts.LoggingEventType;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 * FormDao с функционалом логгирования
 *
 * @author sokolov
 * @param <M>
 * @param <E>
 */
public abstract class AbstractLoggingFormDao<M extends IFormModel, E extends IFormEntity> extends AbstractFormDao<M, E> {

    @Lookup
    private ILoggingJurnalDao loggingDao;

    public AbstractLoggingFormDao(Class<M> modelClass, Class<E> entityClass) {
        super(modelClass, entityClass);
    }
    
    public void initMockLogging() {
        this.loggingDao = new ILoggingJurnalDao() {
            @Override
            public void putEventWithUser(String userName, NamedItem event, LoggingEventType eventType, BigDecimal objectId, IFormModel model) {
            }

            @Override
            public void putEvent(NamedItem event, LoggingEventType eventType, BigDecimal objectId, IFormModel model) {
            }
        };
    }

    @Override
    protected void onAfterSave(E e, E oldE) {
        NamedItem loggingEventType = getLoggingEventType();
        if (loggingEventType != null) {
            LoggingEventType eventType = oldE == null ? LoggingEventType.CREATE : LoggingEventType.UPDATE;
            loggingDao.putEvent(loggingEventType, eventType, e.getId(), toModel(e));
        }
    }

    @Override
    public void delRecord(final BigDecimal id) {
        if (null == id) {
            return;
        }
        final E entity = getEm().find(getEntityClass(), id);
        if (null != entity) {
            M oldModel = toModel(entity);
            remove(entity);
            NamedItem loggingEventType = getLoggingEventType();
            if (loggingEventType != null) {
                loggingDao.putEvent(loggingEventType, LoggingEventType.DELETE, null, oldModel);
            }
        }
    }

    protected abstract NamedItem getLoggingEventType();

}
