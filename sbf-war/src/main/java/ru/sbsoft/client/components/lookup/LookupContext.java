package ru.sbsoft.client.components.lookup;

/**
 * @author balandin
 * @since Jul 11, 2013 6:11:55 PM
 */
public class LookupContext {

	private boolean expanded;
	private boolean userQuery;

	public LookupContext() {
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isUserQuery() {
		return userQuery;
	}

	public void setUserQuery(boolean userQuery) {
		this.userQuery = userQuery;
	}

}
