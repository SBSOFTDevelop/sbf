package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.shared.ApplicationInfo;
import ru.sbsoft.shared.exceptions.ThrowableWrapper;

/**
 * @see IServiceService
 */
public interface IServiceServiceAsync extends ISBFServiceAsync {

	void getLastError(AsyncCallback<ThrowableWrapper> callback);

	void getCurrentUserName(AsyncCallback<String> callback);

	void getApplicationInfo(AsyncCallback<ApplicationInfo> callback);
}
