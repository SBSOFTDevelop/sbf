package ru.sbsoft.client.components.actions.event;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.util.KeyNav;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import ru.sbsoft.client.components.actions.Action;

/**
 *
 * @author vk
 */
public class KeyActionController {

    private final Widget handlerWidget;
    private final KeyNav keyNav;
    private final Map<KeyUpDownDefinition, KeyActionConfig> actions = new HashMap<>();

    public KeyActionController(Widget handlerWidget) {
        this.handlerWidget = handlerWidget;
        keyNav = new KeyNav() {
            @Override
            public void onKeyPress(NativeEvent evt) {
//                DebugUtils.printTarget(evt, ">>>>>Key Event target<<<<<");
                KeyUpDownDefinition pressedKey = KeyUpDownDefinition.create(evt);
                KeyActionConfig cfg = actions.get(pressedKey);
                if (cfg != null && cfg.getAction() != null) {
                    if (cfg.isPreventDefault()) {
                        evt.preventDefault();
                    }
                    if (cfg.isStopPropagation()) {
                        evt.stopPropagation();
                    }
                    cfg.getAction().exec();
                }
            }
        };
    }

    public void addAction(int keyCode, Action act) {
        addAction(new KeyUpDownDefinition(keyCode), act);
    }
    
    public void addAction(int keyCode, KeyAction act) {
        addAction(new KeyUpDownDefinition(keyCode), act);
    }

    public void addAction(int keyCode, KeyActionConfig act) {
        addAction(new KeyUpDownDefinition(keyCode), act);
    }

    public void addAction(KeyUpDownDefinition key, Action act) {
        addAction(key, act != null ? new ActionKeyAction(act) : null);
    }
    
    public void addAction(KeyUpDownDefinition key, KeyAction act) {
        addAction(key, act != null ? new KeyActionConfig(act) : null);
    }

    public void addAction(List<KeyUpDownDefinition> keys, Action act) {
        keys.forEach(k -> addAction(k, act));
    }

    public void addAction(List<KeyUpDownDefinition> keys, KeyAction act) {
        addAction(keys, act != null ? new KeyActionConfig(act) : null);
    }
    
    public void addAction(List<KeyUpDownDefinition> keys, KeyActionConfig act) {
        for(KeyUpDownDefinition key : keys){
            addAction(key, act);
        }
    }
    
    public void addAction(KeyUpDownDefinition key, KeyActionConfig act) {
        if (key != null) {
            if (actions.isEmpty()) {
                keyNav.bind(handlerWidget);
            }
            if (act != null) {
                actions.put(key, act);
            } else {
                actions.remove(key);
            }
            if (actions.isEmpty()) {
                keyNav.bind(null);
            }
        }
    }

    public void removeKey(KeyUpDownDefinition key) {
        addAction(key, (KeyActionConfig) null);
    }

    public void removeKey(int key) {
        removeKey(new KeyUpDownDefinition(key));
    }
    
    public void removeAction(Action act){
        removeAction(a -> (a.getAction() instanceof ActionKeyAction) && ((ActionKeyAction)a.getAction()).getAct() == act);
    }
    
    public void removeAction(KeyAction act){
        removeAction(a -> a.getAction() == act);
    }
    
    public void removeAction(KeyActionConfig act){
        removeAction(a -> a == act);
    }
    
    private void removeAction(Function<KeyActionConfig, Boolean> removeChecker){
        Iterator<Map.Entry<KeyUpDownDefinition, KeyActionConfig>> entryIt = actions.entrySet().iterator();
        while(entryIt.hasNext()){
            Map.Entry<KeyUpDownDefinition, KeyActionConfig> ent = entryIt.next();
            if(removeChecker.apply(ent.getValue())){
                entryIt.remove();
            }
        }
    }
    
    private static class ActionKeyAction implements KeyAction {
        private final Action act;

        public ActionKeyAction(Action act) {
            this.act = act;
        }

        public Action getAct() {
            return act;
        }

        @Override
        public void exec() {
            act.perform();
        }
    }
}
