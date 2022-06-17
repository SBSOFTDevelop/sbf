/*
 * Copyright (c) 2021 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.client;

import ru.sbsoft.shared.ApplicationInfo;

/**
 *
 * @author sokolov
 */
public interface SaveAppInfoHandler {
    
     void onSave(ApplicationInfo model);
    
}
