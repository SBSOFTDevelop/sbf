package ru.sbsoft.client.components.form.fields;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.form.AdapterField;

/**
 *
 * @author balandin
 * @since Oct 8, 2013 4:08:48 PM
 */
public class IsFieldAdapter extends BaseAdapter {

	public IsFieldAdapter(AdapterField component, ReadOnlyControl readOnlyControl, HasWidgets mainControl) {
		super(component, readOnlyControl.isReadOnly(), mainControl);
	}

	@Override
	public boolean isValid() {
		return ((AdapterField) getWidget()).isValid();
	}
}
