package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author vk
 */
public interface ISavable {

    void save(AsyncCallback<Void> callback);
}
