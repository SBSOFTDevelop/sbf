package ru.sbsoft.meta.columns;

import ru.sbsoft.shared.meta.Wrapper;

/**
 * Класс представляет метаинформацию о футере (подвале) колонки сетки. 
 * <p>Класс является базовым абстрактным классом для классов, представляющих агрегатные SQL функции (SUM, AVG, MIN, MAX,COUNT(*), COUNT).
 * @author balandin
 * @since May 16, 2014 3:57:02 PM
 */
public abstract class AbstractColumnFooter {

	public AbstractColumnFooter() {
	}

	public abstract String build(String clause);

	public abstract Wrapper wrap(Object value);
}
