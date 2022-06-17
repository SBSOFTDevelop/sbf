package ru.sbsoft.client.components.form.fields;

import com.google.gwt.user.client.ui.HasWidgets;
import ru.sbsoft.client.components.form.FormGridView;

/**
 *
 * @author balandin
 * @since Oct 8, 2013 2:35:16 PM
 */
public class FormGridViewAdapter extends BaseAdapter {

	public FormGridViewAdapter(final FormGridView formGridView, HasWidgets mainControl) {
		super(formGridView, formGridView.isReadOnly(), mainControl);
	}

	public FormGridView getFormGridView() {
		return (FormGridView) getWidget();
	}
}
