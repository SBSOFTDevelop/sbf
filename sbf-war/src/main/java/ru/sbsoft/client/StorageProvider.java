package ru.sbsoft.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.storage.client.Storage;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.state.client.CookieProvider;
import ru.sbsoft.svc.state.client.Provider;

/**
 * Провайдер для StateManager Взависимости от поддержки браузером сохраняет
 * настройки интерфейса сначало в локальное хранилище браузера либо в куки если
 * такой поддержки нет.
 *
 * @author sychugin
 */
public class StorageProvider extends Provider {

    public enum StorageType {
        LOCAL_STORAGE("localStorage"),
        SESSION_STORAGE("sessionStorage");

        private final String storage;

        StorageType(String storage) {
            this.storage = storage;
        }

        public String getName() {
            return storage;
        }
    }

    private static Storage storage;

    //private static String NOT_SUPPORT = "Browser not supported local storage";

    public static Provider getProvider(StorageType storageType) {
        switch (storageType) {
            case LOCAL_STORAGE:
                storage = Storage.getLocalStorageIfSupported();
                break;
            case SESSION_STORAGE:
                storage = Storage.getSessionStorageIfSupported();
                break;

            default:
                throw new AssertionError();
        }
        if (storage == null) {
            return new CookieProvider("/", null, null, SVC.isSecure());
        }
        return new StorageProvider();

    }

    private StorageProvider() {
    }

    @Override
    public void getValue(String name, Callback<String, Throwable> callback) {
        callback.onSuccess(storage.getItem(name));
    }

    @Override
    public void setValue(String name, String value) {
        storage.setItem(name, value);
    }

    @Override
    public void clear(String name) {
        storage.removeItem(name);

    }

}
