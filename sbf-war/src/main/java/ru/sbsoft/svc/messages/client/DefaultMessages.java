/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.messages.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Default locale-sensitive messages for SVC. This class uses
 * {@link GWT#create(Class)} to create an instance of an automatically generated
 * subclass that implements the {@link XMessages} interface. See the package
 * containing {@link XMessages} for the property files containing the translated
 * messages. See {@link Messages} for more information.
 */
public class DefaultMessages {

  private static XMessages instance;

  /**
   * Returns an instance of an automatically generated subclass that implements
   * the {@link XMessages} interface containing default locale-sensitive
   * messages for SVC.
   * 
   * @return locale-sensitive messages for SVC
   */
  public static XMessages getMessages() {
      if (instance == null) {
          instance = GWT.create(XMessages.class);
      }
      return instance;
  }

  public static void setMessages(XMessages messages) {
      instance = messages;
  }
  
}
