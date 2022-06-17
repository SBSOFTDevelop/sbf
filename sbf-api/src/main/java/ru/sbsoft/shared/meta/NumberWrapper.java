package ru.sbsoft.shared.meta;

/**
 * Обобщенный класс-обертка для значений типа Number.
 * Предназначен для объектов, которые должны передаваться 
 * на GWT-клиент и/или обратно на сервер.
 * @author balandin
 */
public class NumberWrapper<T extends Number> extends Wrapper<T> {

	public NumberWrapper() {
	}

	public NumberWrapper(T value) {
            super(value);
	}
}
