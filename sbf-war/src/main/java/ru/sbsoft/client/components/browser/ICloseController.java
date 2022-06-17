package ru.sbsoft.client.components.browser;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author vk
 */
public interface ICloseController {

    boolean isCloseSafe();

    void confirmClose(AsyncCallback<Boolean> answer);
}
