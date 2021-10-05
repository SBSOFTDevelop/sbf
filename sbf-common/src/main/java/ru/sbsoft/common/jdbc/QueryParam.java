package ru.sbsoft.common.jdbc;

import javax.persistence.TemporalType;

/**
 * @author balandin
 * @since May 5, 2014 3:55:54 PM
 */
public interface QueryParam {

	public Object getValue();

	public TemporalType getTemporalType();
}
