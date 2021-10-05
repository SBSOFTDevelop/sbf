package ru.sbsoft.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import ru.sbsoft.shared.interfaces.GridType;

public abstract class AbstractTemplateManager implements ITemplateManager{

    private final HashMap<GridType, ITemplateFactory> templates = new HashMap<>();

    protected AbstractTemplateManager() {
    }

    protected final ParamsHolder reg(GridType type, Class<? extends AbstractTemplate> clazz, Object... params) {
        if (templates.containsKey(type)) {
            throw new RuntimeException("Duplicate template " + type);
        }
        return reg(type, new ParamsHolder(clazz, params));
    }

    protected final <F extends ITemplateFactory> F reg(GridType type, F factory) {
        templates.put(type, factory);
        return factory;
    }

    @Override
    public AbstractTemplate initTemplate(GridType type) throws Exception {
        final ITemplateFactory f = templates.get(type);
        if (f == null) {
            return null;
        }
        return f.createTemplate();
    }
    
    protected static interface ITemplateFactory<T extends AbstractTemplate> {

        T createTemplate() throws Exception;
    }

    protected static abstract class AbstractTemplateFactory<T extends AbstractTemplate, SelfType extends AbstractTemplateFactory<T, ?>> implements ITemplateFactory<T> {

        private Object[] params;
        private final Set<Enum> modes = new HashSet<>();

        @Override
        public T createTemplate() throws Exception {
            T t = createTemplateInstance();
            if ((t instanceof AbstractTemplate) && modes.size() > 0) {
                AbstractTemplate bt = (AbstractTemplate) t;
                for (Enum m : modes) {
                    bt.addSysMode(m);
                }
            }
            if (params != null && params.length > 0) {
                t.init(params);
            }
            return t;
        }

        public SelfType setValues(Object... params) {
            this.params = params;
            return (SelfType) this;
        }

        public SelfType addMode(Enum mode) {
            this.modes.add(mode);
            return (SelfType) this;
        }

        public SelfType addModes(Enum... modes) {
            if (modes != null && modes.length > 0) {
                this.modes.addAll(Arrays.asList(modes));
            }
            return (SelfType) this;
        }

        protected abstract T createTemplateInstance() throws Exception;

    }

    protected static class ParamsHolder extends AbstractTemplateFactory<AbstractTemplate, ParamsHolder> {

        private final Class<? extends AbstractTemplate> clazz;

        public ParamsHolder(Class<? extends AbstractTemplate> clazz, Object[] values) {
            this.clazz = clazz;
            setValues(values);
        }

        @Override
        protected AbstractTemplate createTemplateInstance() throws Exception {
            return clazz.newInstance();
        }

    }
}
