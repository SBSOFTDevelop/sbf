package ru.sbsoft.client.components;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.client.components.form.BaseForm;

/**
 * Фабрика интерфейсных форм.
 * @see ru.sbsoft.client.components.form.LookupField#setFormMaker(ru.sbsoft.client.components.IFormMaker) 
 * @see ru.sbsoft.client.components.form.BaseForm
 * @author Sokoloff
 */
public interface IFormMaker {

    void createForm(AsyncCallback<BaseForm> callback);
}
