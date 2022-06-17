package ru.sbsoft.client.utils;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import ru.sbsoft.client.components.browser.ICloseController;

/**
 *
 * @author vk
 */
public class WidgetCloseValidalidator {
    
    public static void confirmClose(Widget w, AsyncCallback<Boolean> answer){
        WidgetCloseValidalidator wcv = new WidgetCloseValidalidator(w);
        wcv.confirmClose(answer);
    }
    
    private final List<ICloseController> closeControllers;
    
    public WidgetCloseValidalidator(Widget w){
        this.closeControllers = getCloseControllers(w);
    }

    public boolean isCloseSafe() {
        for(ICloseController c : closeControllers){
            if(!c.isCloseSafe()){
                return false;
            }
        }
        return true;
    }

    public void confirmClose(AsyncCallback<Boolean> answer) {
        CloseConfirmator confirmator = new CloseConfirmator(closeControllers, answer);
        confirmator.onSuccess(Boolean.TRUE);
    }

    private static class CloseConfirmator implements AsyncCallback<Boolean> {

        private final Deque<ICloseController> stack;
        private final AsyncCallback<Boolean> answer;

        public CloseConfirmator(List<ICloseController> closeControllers, AsyncCallback<Boolean> answer) {
            this.stack = new ArrayDeque<>(closeControllers);
            this.answer = answer;
        }

        @Override
        public void onFailure(Throwable caught) {
            answer.onFailure(caught);
        }

        @Override
        public void onSuccess(Boolean result) {
            if (Boolean.FALSE.equals(result)) {
                answer.onSuccess(false);
            } else if (stack.isEmpty()) {
                answer.onSuccess(true);
            } else {
                ICloseController cnt = stack.pop();
                cnt.confirmClose(this);
            }
        }
    }

    private static List<ICloseController> getCloseControllers(Widget initWidget) {
        if(initWidget == null){
            return Collections.emptyList();
        }
        List<ICloseController> res = new ArrayList<>();
        List<Widget> cache = new ArrayList<>();
        cache.add(initWidget);
        while (!cache.isEmpty()) {
            Widget w = cache.remove(cache.size() - 1);
            if (w instanceof ICloseController) {
                res.add((ICloseController) w);
            }
            if (w instanceof HasWidgets) {
                HasWidgets cnt = (HasWidgets) w;
                Iterator<Widget> iter = cnt.iterator();
                while (iter.hasNext()) {
                    cache.add(iter.next());
                }
            }
        }
        return res;
    }
}
