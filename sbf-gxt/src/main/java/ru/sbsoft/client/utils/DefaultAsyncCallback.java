package ru.sbsoft.client.utils;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Component;

/**
 * Базовый класс для создания объектов обратного вызова GWT. Определяет общее рутинное поведение данного класса
 * объектов. Наследник должен только обработать полученные данные в {@link #onResult(java.lang.Object) }
 *
 * @author balandin
 * @since Dec 26, 2012 5:35:21 PM
 */
public class DefaultAsyncCallback<T> implements AsyncCallback<T> {

    private final Component component;
    private ClientUtils.ErrorHandler errorHandler;

    public DefaultAsyncCallback() {
        this(null);
    }
    
    public DefaultAsyncCallback(Component component) {
        this.component = component;
        if (component instanceof ClientUtils.ErrorHandler) {
            this.errorHandler = (ClientUtils.ErrorHandler)component;
        }
        // mask();
    }

    public void setErrorHandler(ClientUtils.ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void onFailure(Throwable caught) {
        unmask();
        ClientUtils.alertException(caught, errorHandler);
    }

    @Override
    public final void onSuccess(T result) {
        try {
            onResult(result);
        } finally {
            unmask();
        }
    }

    public void onResult(T result) {
    }

    protected void mask() {
        if (component != null) {
            component.mask();
        }
    }

    protected void unmask() {
        if (component != null) {
            component.unmask();
        }
    }
}
