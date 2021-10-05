package ru.sbsoft.client.components.kladr;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.client.components.form.CustomField;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.client.utils.HLD;
import ru.sbsoft.shared.kladr.AddressModel;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Поле адреса. Для редактирования используется отдельное окно.
 */
public class AddressEdit extends CustomField<AddressModel>
		implements HasValueChangeHandlers<AddressModel>,
		AllowBlankControl, ReadOnlyControl {

	private static final int BUTTON_SIZE = 32;
	private static final int BUTTON_MARGIN = 4;
	//
	private final TextArea fieldAddress;
	private final TextButton btnRegAddress;
	private AddressModel value;
	private AddressEditForm addressForm;
	//
	private EmptyValidator<AddressModel> emptyValidator;
	private boolean allowBlank = true;

	public AddressEdit(final int height, final String title) {
		super();

		setHeight(height);

		fieldAddress = new TextArea();
		fieldAddress.setHeight(height);
		fieldAddress.setReadOnly(true);

		btnRegAddress = new TextButton("", SBFResources.GENERAL_ICONS.Home());
		btnRegAddress.setToolTip(I18n.get(SBFEditorStr.hintEditAddress));
		btnRegAddress.setPixelSize(BUTTON_SIZE, BUTTON_SIZE);

		HorizontalLayoutContainer c = new HorizontalLayoutContainer();
		c.add(fieldAddress, HLC.FILL);
		c.add(btnRegAddress, new HLD(BUTTON_SIZE + 2 * BUTTON_MARGIN, -1, new Margins((height - BUTTON_SIZE) / 2, 0, 0, BUTTON_MARGIN)));
		setWidget(c);

		btnRegAddress.addSelectHandler(new SelectEvent.SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (addressForm == null) {
					addressForm = new AddressEditForm(AddressEdit.this, true);
					addressForm.setHeading(title);
				}
				addressForm.show(getValue());
			}
		});
	}

	public boolean isReadOnly() {
		return !btnRegAddress.isEnabled();
	}

	public void setReadOnly(boolean readOnly) {
		btnRegAddress.setEnabled(!readOnly);
	}

	public AddressModel getValue() {
		return value;
	}

	public void setValue(final AddressModel value) {
		setValue(value, false);
	}

	public void setValue(final AddressModel value, boolean fireEvents) {
		if (!ClientUtils.equals(this.value, value)) {
			this.value = value;
			fieldAddress.setValue(value == null ? null : value.getFullAddress());
			if (fireEvents) {
				fireValueChangeEvent();
			}
		}
	}

	public TextArea getFieldAddress() {
		return fieldAddress;
	}

	public void setAllowBlank(boolean value) {
		if (!value) {
			if (emptyValidator == null) {
				emptyValidator = new EmptyValidator<AddressModel>();
			}
			if (!getValidators().contains(emptyValidator)) {
				getValidators().add(0, emptyValidator);
			}
		} else if (emptyValidator != null) {
			removeValidator(emptyValidator);
		}
		this.allowBlank = value;
	}

	public boolean isAllowBlank() {
		return allowBlank;
	}

	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<AddressModel> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	private void fireValueChangeEvent() {
		ValueChangeEvent.fire(this, value);
	}
}
