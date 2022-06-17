package ru.sbsoft.client.components;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vk
 */
public class InitManager {

    private final List<Initializable> inits = new ArrayList<>();

    public void addInitializable(Initializable ini) {
        if (!inits.contains(ini)) {
            inits.add(ini);
        }
    }

    public void removeInitializable(Initializable ini) {
        for (int i = inits.size() - 1; i >= 0; i--) {
            if (inits.get(i) == ini) {
                inits.remove(i);
            }
        }
    }

    public void doInit(AsyncCallback<Void> allDoneSignal) {
        if (!inits.isEmpty()) {
            Downcounter dc = new Downcounter(inits.size(), allDoneSignal);
            for (Initializable ini : inits) {
                ini.init(dc);
            }
        } else {
            allDoneSignal.onSuccess(null);
        }
    }

    private static class Downcounter implements AsyncCallback<Void> {

        private int count;
        private final AsyncCallback<Void> callback;
        private boolean noError = true;

        public Downcounter(int count, AsyncCallback<Void> callback) {
            this.count = count;
            this.callback = callback;
        }

        @Override
        public void onFailure(Throwable caught) {
            count--;
            noError = false;
            callback.onFailure(caught);
        }

        @Override
        public void onSuccess(Void result) {
            count--;
            if (count == 0 && noError) {
                callback.onSuccess(result);
            }
        }
    }
}
