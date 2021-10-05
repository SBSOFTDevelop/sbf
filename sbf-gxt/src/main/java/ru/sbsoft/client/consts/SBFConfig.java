package ru.sbsoft.client.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс предназначен для сохранения глобальных настроек библиотеки.
 * @author Sokoloff
 */
public class SBFConfig {
    
    private static SBFConfig instance;
    private final Map<String, Object> cacheMap = new HashMap<String, Object>();
    
    public static SBFConfig getInstance() {
        if (null == instance) {
            instance = new SBFConfig();
            instance.init();
        }
        return instance;
    }
    
    /**
     * Служит для инициализации по умолчанию величин.
     */
    protected void init() {
        writeBool(SBFVariable.GRID_CONFIG, true);
        writeBool(SBFVariable.CODE_KLADR, false);
    }
    
    public static Object readObject(SBFConfigKey key) {
        return getInstance().cacheMap.get(key.getKey());
    }
    
    public static void writeObject(SBFConfigKey key, Object value) {
        getInstance().cacheMap.put(key.getKey(), value);
    }
    
    public static String readString(SBFConfigKey key) {
        return (String) getInstance().cacheMap.get(key.getKey());
    }
    
    public static void writeString(SBFConfigKey key, String value) {
        getInstance().cacheMap.put(key.getKey(), value);
    }
    
    public static boolean readBool(SBFConfigKey key) {
        Object value = getInstance().cacheMap.get(key.getKey());
        if (null == value) {
            return false;
        }
        return (Boolean) value;
    }
    
    public static void writeBool(SBFConfigKey key, boolean value) {
        getInstance().cacheMap.put(key.getKey(), value);
    }
    
    public static int readInteger(SBFConfigKey key) {
        Object value = getInstance().cacheMap.get(key.getKey());
        if (null == value) {
            return 0;
        }
        return (Integer) value;
    }
    
    public void writeInteger(SBFConfigKey key, int value) {
        cacheMap.put(key.getKey(), value);
    }
    
}
