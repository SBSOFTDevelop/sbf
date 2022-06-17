package ru.sbsoft.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author balandin
 * @since Oct 15, 2013 7:33:18 PM
 */
public class BuildInfo {

	private final Config config;
	//
	private static final String PROP_VERSION = "project.version";
	private static final String PROP_REVISION = "project.version.revision";
	private static final String PROP_TIMESTAMP = "project.version.time";
	//
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
	private static final String DEF_VALUE = "unavailable";

	public BuildInfo(Config config) {
		this.config = config;
	}

	public String getVersion() {
		if (config == null) {
			return DEF_VALUE;
		}
		return config.getString(PROP_VERSION, DEF_VALUE);
	}

	public String getRevision() {
		if (config == null) {
			return DEF_VALUE;
		}
		String result = config.getString(PROP_REVISION, DEF_VALUE);
		if (result.startsWith("$")) {
			return DEF_VALUE;
		}
		return result;
	}

	public String getBuidDateTime() {
		if (config == null) {
			return DEF_VALUE;
		}
		try {
			final long time = config.getLong(PROP_TIMESTAMP);
			return FORMAT.format(new Date(time));
		} catch (ConfigException ex) {
			return DEF_VALUE;
		}
	}
}
