package ru.sbsoft.client.utils;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.client.components.IWindow;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class WindowAsyncCallback<T> implements AsyncCallback<T> {

    private IWindow window;

    public WindowAsyncCallback() {
    }

    public WindowAsyncCallback(IWindow window) {
        this.window = window;
    }

    public void setWindow(IWindow window) {
        this.window = window;
    }

    public IWindow getWindow() {
        return window;
    }

    @Override
    public void onFailure(Throwable caught) {
        unmask();
        ClientUtils.ErrorHandler handler = null;
        if (window instanceof ClientUtils.ErrorHandler) {
            handler = (ClientUtils.ErrorHandler) window;
        }
        ClientUtils.alertException(caught, handler);
    }

    @Override
    public void onSuccess(T result) {
        try {
            onResult(result);
        } finally {
            unmask();
        }
    }

    private void unmask() {
        if (window != null) {
            window.unmask();
        }
    }

    protected void onResult(T result) {
    }
}
