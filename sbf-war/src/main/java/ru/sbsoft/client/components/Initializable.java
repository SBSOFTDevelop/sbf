package ru.sbsoft.client.components;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author vk
 */
public interface Initializable {
    void init(AsyncCallback<Void> doneInformer);
}
