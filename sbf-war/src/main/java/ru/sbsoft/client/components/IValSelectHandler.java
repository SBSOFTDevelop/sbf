package ru.sbsoft.client.components;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import ru.sbsoft.shared.FilterInfo;

/**
 *
 * @author Kiselev
 */
public interface IValSelectHandler<T> {

    String toString(T val);

    void selectVal(T initVal, AsyncCallback<T> callback);
    
    void setFilter(List<FilterInfo> filters, Callback<Void, Throwable> callback);
    
    boolean isValExists(T val);
}
