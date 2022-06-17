/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.util;

import com.google.gwt.core.client.GWT;

public final class ImageHelper {

  public static String createModuleBasedUrl(String path) {
    return "url('" + GWT.getModuleBaseURL() + path + "');";
   }
}
