package ru.sbsoft.client.components;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Событие выбора пункта меню.
 * @param <T>
 * @author Sokoloff
 */
public class SBSelectHandler<T> implements SelectHandler, SelectionHandler<T> {

	@Override
	public void onSelection(SelectionEvent<T> event) {
		onSelectEvent();
	}

	@Override
	public void onSelect(SelectEvent event) {
		onSelectEvent();
	}

	public void onSelectEvent() {
	}
}
