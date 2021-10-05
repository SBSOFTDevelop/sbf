package ru.sbsoft.client.components.lookup;

import ru.sbsoft.shared.services.FetchResult;
import ru.sbsoft.shared.model.MarkModel;

/**
 * @author balandin
 * @since Jul 11, 2013 6:11:55 PM
 */
public class LookupContext {

	private boolean expanded;
	private boolean userQuery;
	private FetchResult<MarkModel> lastLoadResult;

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

	public FetchResult<MarkModel> getLastLoadResult() {
		return lastLoadResult;
	}

	public void setLastLoadResult(FetchResult<MarkModel> lastLoadResult) {
		this.lastLoadResult = lastLoadResult;
	}
}
