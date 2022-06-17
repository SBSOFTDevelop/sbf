/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.dnd.core.client;

/**
 * DND public constants and enumerations.
 */
public class DND {

  /**
   * Operation public enumeration which sets the operation performed by a drop
   * target.
   */
  public enum Operation {
    COPY, MOVE
  }

  /**
   * Feedback public enumeration which sets the type of visual feedback a drop
   * target will display.
   */
  public enum Feedback {
    APPEND, INSERT, BOTH
  }

  /**
   * TreeSource public enumeration which specifies the type of drops that are
   * allowed with a tree drop target.
   */
  public enum TreeSource {
    LEAF, NODE, BOTH
  }

}
