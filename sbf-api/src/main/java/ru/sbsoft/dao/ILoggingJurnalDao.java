/*
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.dao;

import java.math.BigDecimal;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.consts.LoggingEventType;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author sokolov
 */
public interface ILoggingJurnalDao {
    
    void putEventWithUser(String userName, NamedItem event, LoggingEventType eventType, BigDecimal objectId, IFormModel model);
    
    void putEvent(NamedItem event, LoggingEventType eventType, BigDecimal objectId, IFormModel model);
    
}
