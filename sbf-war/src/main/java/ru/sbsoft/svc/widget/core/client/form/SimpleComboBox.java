/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiConstructor;
import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.cell.core.client.form.TriggerFieldCell.TriggerFieldAppearance;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;

/**
 * A combo box that creates and manages a {@link ListStore} of {@code <T>}
 * instances. Values are added to the list store using {@link #add} and removed
 * from the list store using {@link #remove(Object)}.
 * <p/>
 * If the selection list is already in a list store for some other purpose, you
 * may find it easier to use {@link ComboBox} directly.
 * 
 * @param <T> the combo box type
 */
public class SimpleComboBox<T> extends ComboBox<T> {

  /**
   * Creates an empty combo box in preparation for values to be added to the
   * selection list using {@link #add}.
   * 
   * @param labelProvider the label provider that implements the interface to
   *          the data model associated with this combo box and is responsible
   *          for returning the value displayed to the user
   */
  @UiConstructor
  public SimpleComboBox(LabelProvider<? super T> labelProvider) {
    this(labelProvider, GWT.<TriggerFieldAppearance>create(TriggerFieldAppearance.class));
  }

  /**
   * Creates an empty combo box with the given appearance in preparation for values
   * to be added to the selection list using {@link #add}.
   * @param labelProvider the label provider that implements the interface to
   *          the data model associated with this combo box and is responsible
   *          for returning the value displayed to the user
   * @param appearance the appearance to use when rendering this widget
   */
  public SimpleComboBox(LabelProvider<? super T> labelProvider, TriggerFieldAppearance appearance) {
    super(new ListStore<T>(new ModelKeyProvider<T>() {
      @Override
      public String getKey(T item) {
        return item.toString();
      }
    }), labelProvider, appearance);
  }

  /**
   * Creates a new simple combo box with the given cell.
   * 
   * @param cell the cell
   */
  public SimpleComboBox(ComboBoxCell<T> cell) {
    super(cell);
  }

  /**
   * Adds the values to the list of items displayed in the drop down.
   * 
   * @param values the values to add
   */
  public void add(List<T> values) {
    getStore().addAll(values);
  }

  /**
   * Adds the value to the list of items displayed in the drop down.
   * 
   * @param value the value to add
   */
  public void add(T value) {
    getStore().add(value);
  }

  /**
   * Returns the selected index.
   * 
   * @return the index or -1 if no selection
   */
  public int getSelectedIndex() {
    T c = getValue();
    if (c != null) {
      return getStore().indexOf(c);
    }
    return -1;
  }

  /**
   * Removes the item from the list of items displayed in the drop down.
   * 
   * @param remove the value to remove
   */
  public void remove(T remove) {
    getStore().remove(remove);
  }

}
