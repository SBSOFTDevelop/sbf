package ru.sbsoft.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author balandin
 * @since Oct 15, 2013 7:35:30 PM
 */
public class Config {

	private final Properties props;

	public Config(Properties props) {
		if (props == null) {
			throw new IllegalArgumentException();
		}
		this.props = props;
	}

	public static Config load(InputStream in) throws ConfigException {
		if (in == null) {
			throw new ConfigException("Null resource");
		}
		try {
			Properties p = new Properties();
			p.load(in);
			return new Config(p);
		} catch (IOException e) {
			throw new ConfigException("Error on loading properties", e);
		} finally {
			IO.close(in);
		}
	}

	public String getString(String name, String defValue) {
		String s = props.getProperty(name);
		return (s == null) ? defValue : s;
	}

	public String getString(String name) throws ConfigException {
		String s = getString(name, null);
		if (s == null) {
			throw new ConfigException(String.format("Parameter [%s] not exists", name));
		}
		return s;
	}

	public long getLong(String name, long defValue) throws ConfigException {
		String s = getString(name, null);
		return (s == null) ? defValue : parseLong(s, name);
	}

	public long getLong(String name) throws ConfigException {
		String s = getString(name);
		return parseLong(s, name);
	}

	private long parseLong(String s, String name) throws ConfigException {
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			throw new ConfigException(String.format("Parameter [%s] unexpected long value [%s]", name, s), e);
		}
	}
}
