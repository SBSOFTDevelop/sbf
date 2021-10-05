package ru.sbsoft.shared.meta;

import java.io.Serializable;

/**
 * Класс представляет коллекцию экземпляров класса {@link Row}.
 * @author balandin
 */
public class Rows implements Serializable {

	private Row[] items;

	public Rows() {
	}

	public Row[] getItems() {
		return items;
	}

	public void setItems(Row[] items) {
		this.items = items;
	}
}
