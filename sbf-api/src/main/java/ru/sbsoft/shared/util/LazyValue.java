package ru.sbsoft.shared.util;

import java.util.function.Supplier;

/**
 *
 * @author vk
 */
public class LazyValue<T> {
    private final Supplier<T> supplier;
    private T val;
    
    public LazyValue(Supplier<T> supplier){
        this.supplier = supplier;
    }

    public T getVal() {
        if(val == null){
            val = supplier.get();
        }
        return val;
    }
}
