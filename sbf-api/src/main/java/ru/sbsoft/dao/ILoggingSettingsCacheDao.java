/*
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.dao;

/**
 * Кэш настроек журналирования событий
 * 
 * @author sokolov
 */
public interface ILoggingSettingsCacheDao {
    
    void reset();
    
    boolean isActive(String code);
    
}
