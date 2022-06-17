package ru.sbsoft.client.components.actions.event;

import com.google.gwt.event.dom.client.KeyCodes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author vk
 */
public final class StdActKey {

    public static final List<KeyUpDownDefinition> NEW = finalList(defKey(KeyCodes.KEY_N), key(KeyCodes.KEY_INSERT));
    public static final List<KeyUpDownDefinition> CLONE = finalList(defKey(KeyCodes.KEY_C), key(KeyCodes.KEY_INSERT).withMod(ModKey.ALT));
    public static final List<KeyUpDownDefinition> OPEN = finalList(defKey(KeyCodes.KEY_O), key(KeyCodes.KEY_ENTER));
    public static final List<KeyUpDownDefinition> DELETE = finalList(defKey(KeyCodes.KEY_D), key(KeyCodes.KEY_DELETE));
    public static final List<KeyUpDownDefinition> SAVE = finalList(defKey(KeyCodes.KEY_S));
    public static final List<KeyUpDownDefinition> QUIT = finalList(defKey(KeyCodes.KEY_Q));
    public static final List<KeyUpDownDefinition> APPLY = finalList(defKey(KeyCodes.KEY_A));
    public static final List<KeyUpDownDefinition> PRINT = finalList(defKey(KeyCodes.KEY_P));
    public static final List<KeyUpDownDefinition> EXPORT = finalList(defKey(KeyCodes.KEY_E));
    public static final List<KeyUpDownDefinition> REFRESH = finalList(defKey(KeyCodes.KEY_R));
    public static final List<KeyUpDownDefinition> SWITCH_TAB = finalList(key(KeyCodes.KEY_TAB).withMod(ModKey.SHIFT));
    public static final List<KeyUpDownDefinition> FILTER = finalList(defKey(KeyCodes.KEY_I));
    public static final List<KeyUpDownDefinition> MARK = finalList(defKey(KeyCodes.KEY_M));
    public static final List<KeyUpDownDefinition> YES = finalList(defKey(KeyCodes.KEY_Y));
    public static final List<KeyUpDownDefinition> NO = finalList(defKey(KeyCodes.KEY_N));
    public static final List<KeyUpDownDefinition> COPY = finalList(key(KeyCodes.KEY_C).withMod(ModKey.CTRL));
    public static final List<KeyUpDownDefinition> PASTE = finalList(key(KeyCodes.KEY_V).withMod(ModKey.CTRL));

    private static List<KeyUpDownDefinition> finalList(KeyUpDownDefinition... defs) {
        if (defs == null || defs.length == 0) {
            throw new AssertionError("No key definitions for list creation");
        }
        return Collections.unmodifiableList(Arrays.asList(defs));
    }

    private static KeyUpDownDefinition defKey(int key) {
        return new KeyUpDownDefinition(key, ModKey.ALT, ModKey.CTRL);
    }

    private static KeyUpDownDefinition key(int key) {
        return new KeyUpDownDefinition(key);
    }
    
    public static KeyUpDownDefinition create(int key) {
        return defKey(key);
    }

    private StdActKey() {
    }
}
