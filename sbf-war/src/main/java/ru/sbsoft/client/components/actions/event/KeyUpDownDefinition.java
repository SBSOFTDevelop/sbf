package ru.sbsoft.client.components.actions.event;

import com.google.gwt.dom.client.NativeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author vk
 */
public class KeyUpDownDefinition {
    
    private final int keyCode;
    private final int mod;

    public KeyUpDownDefinition(int keyCode, ModKey...modKeys) {
        Objects.requireNonNull(keyCode);
        this.keyCode = keyCode;
        int mods = 0;
        if(modKeys != null){
            for(ModKey mk : modKeys){
                mods |= getMask(mk);
            }
        }
        mod = mods;
    }
    
    public KeyUpDownDefinition withMod(ModKey...newModKeys){
        return new KeyUpDownDefinition(keyCode, newModKeys);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.keyCode;
        hash = 47 * hash + this.mod;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final KeyUpDownDefinition other = (KeyUpDownDefinition) obj;
        return this.keyCode == other.keyCode && this.mod == other.mod;
    }

    public static KeyUpDownDefinition create(NativeEvent ev) {
        if (isKeyEvent(ev)) {
            List<ModKey> mods = new ArrayList<>();
            addMod(mods, ev.getAltKey(), ModKey.ALT);
            addMod(mods, ev.getCtrlKey(), ModKey.CTRL);
            addMod(mods, ev.getShiftKey(), ModKey.SHIFT);
            addMod(mods, ev.getMetaKey(), ModKey.META);
            return new KeyUpDownDefinition(ev.getKeyCode(), mods.toArray(new ModKey[mods.size()]));
        }
        return null;
    }
    
    private static void addMod(List<ModKey> mks, boolean add, ModKey mk){
        if(add){
            mks.add(mk);
        }
    }
    
    private static int getMask(ModKey mk){
        return mk != null ? 0x1 << mk.ordinal() : 0;
    }

    private static boolean isKeyEvent(NativeEvent ev) {
        return ev.getType() != null && ev.getType().startsWith("key");
    }
}
