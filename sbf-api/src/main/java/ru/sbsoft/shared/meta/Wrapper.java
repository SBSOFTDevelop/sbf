package ru.sbsoft.shared.meta;

import ru.sbsoft.shared.param.DTO;

/**
 * Обобщенный класс-обертка реализует маркерный интерфейс (data transfer object).
 * Предназначен для объектов, которые должны передаваться 
 * на GWT-клиент и/или обратно на сервер.
 * @author balandin
 */
public class Wrapper<T> implements DTO {

	private T value;

	public Wrapper() {
	}

	public Wrapper(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
