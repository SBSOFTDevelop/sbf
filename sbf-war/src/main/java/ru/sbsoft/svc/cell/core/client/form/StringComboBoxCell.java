/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client.form;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ListStore;

/**
 * A combo box cell for use with strings, providing optional support for
 * entering arbitrary text and adding that text to the drop down.
 * <p/>
 * To enable the user to enter arbitrary text in the combo box text field, set
 * the <i>force selection</i> property to true using
 * {@link #setForceSelection(boolean)}.
 * <p/>
 * To add user entered text to the combo box drop down, set the <i>add user
 * values</i> property to true using {@link #setAddUserValues(boolean)}.
 */
public class StringComboBoxCell extends ComboBoxCell<String> {

  private boolean isAddUserValues;
  private List<String> userValues;

  /**
   * Creates a combo box cell for use with strings.
   * <p>
   * A simple store for strings can be created using:
   * 
   * <pre>
   * new ListStore&lt;String&gt;(new ModelKeyProvider&lt;String&gt;() {
   *   &#64;Override
   *   public String getKey(String item) {
   *     return item.toString();
   *   }
   * })
   * </pre>
   * <p/>
   * A simple label provider for use with strings can be created using:
   * <p/>
   * 
   * <pre>
   * new LabelProvider&lt;String&gt;() {
   *   &#64;Override
   *   public String getLabel(String item) {
   *     return item;
   *   }
   * }
   * </pre>
   * 
   * @param store the store to contain the strings
   * @param labelProvider returns a label for a given string
   */
  public StringComboBoxCell(ListStore<String> store, LabelProvider<? super String> labelProvider) {
    super(store, labelProvider);
  }

  /**
   * Creates a combo box cell for use with strings with a specified appearance rather than the default.
   * @param store the store to contain the strings
   * @param labelProvider returns a label for a given string
   * @param appearance the appearance to use when rendering this cell
   */
  public StringComboBoxCell(ListStore<String> store, LabelProvider<? super String> labelProvider, TriggerFieldAppearance appearance) {
    super(store, labelProvider, appearance);
  }

  /**
   * Clears the list of values typed by the user.
   */
  public void clearUserValues() {
    if (userValues != null) {
      for (String value : userValues) {
        getStore().remove(value);
      }
      userValues.clear();
    }
  }

  /**
   * Returns a list containing values typed by the user in the combo box text
   * field. These values are saved when the focus leaves the combo box if the
   * <i>force selection</i> property is false and the <i>add user values</i>
   * property is true.
   * 
   * @return a list of values typed by the user
   */
  public List<String> getUserValues() {
    return userValues == null ? Collections.<String> emptyList() : userValues;
  }

  /**
   * Returns true to indicate that arbitrary values typed by the user will be
   * added to the drop down list.
   * 
   * @return true to indicate values typed by user will be saved in drop down
   *         list
   */
  public boolean isAddUserValues() {
    return isAddUserValues;
  }

  /**
   * Set to true to add arbitrary values typed by the user to the drop down
   * list; be sure to also set <i>force selection</i> to false.
   * <p/>
   * This value is false by default (i.e. user values are not added to the drop
   * down list).
   * 
   * @param isAddUserValues true to add arbitrary values typed by the user to
   *          the drop down list
   */
  public void setAddUserValues(boolean isAddUserValues) {
    this.isAddUserValues = isAddUserValues;
  }

  @Override
  protected String getByValue(String value) {
    String selection = super.getByValue(value);
    if (selection == null && !isForceSelection()) {
      selection = value;
      if (isAddUserValues) {
        if (userValues == null) {
          userValues = new LinkedList<String>();
        }
        getStore().add(selection);
        if (!userValues.contains(selection)) {
          userValues.add(selection);
        }
      }
    }
    return selection;
  }

}