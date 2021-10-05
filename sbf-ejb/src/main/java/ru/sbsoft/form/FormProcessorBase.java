package ru.sbsoft.form;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import ru.sbsoft.dao.FilterHelper;
import ru.sbsoft.generator.api.Lookup;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.session.SessionUtils;
import ru.sbsoft.shared.FilterInfo;

/**
 *
 * @author vk
 */
abstract class FormProcessorBase<M extends IFormModel> implements IFormProcessor<M> {

    @Lookup
    private EntityManager em; // do not use directly. only by getEm. getEm can be overriden
    @Lookup
    private SessionContext scontext; // do not use directly. only by getScontext. getScontext can be overriden

    private FilterHelper parentHelper = null;
    
    private List<FilterInfo> parentFilters = Collections.emptyList();

    protected EntityManager getEm() {
        return em;
    }
    
    protected SessionContext getScontext(){
        return scontext;
    }

    protected final String getUser() {
        return SessionUtils.getCurrentUserName(getScontext());
    }

    protected final FilterHelper getParentHelper() {
        if (parentHelper == null) {
            parentHelper = new FilterHelper(em);
        }
        return parentHelper;
    }

    protected final List<FilterInfo> getParentFilters() {
        return parentFilters;
    }

    @Override
    public final FormProcessorBase<M> setParentFilters(List<FilterInfo> parentFilters) {
        this.parentFilters = parentFilters != null ? parentFilters : Collections.emptyList();
        return this;
    }

    FormProcessorBase<M> init(EntityManager em, SessionContext scontext) {
        checkSingle(this.em, this.scontext);
        this.em = em;
        this.scontext = scontext;
        return this;
    }

    protected Date getCurrentDate() {
        return new Date();
    }

    protected static <T> T notNull(final T obj, final String msg, final Object... args) {
        if (obj == null) {
            throw new NullPointerException(args == null || args.length == 0 ? msg : String.format(msg, args));
        }
        return obj;
    }

    private static void checkSingle(Object... oo) {
        if (oo != null) {
            for (Object o : oo) {
                if (o != null) {
                    throw new IllegalStateException(o.getClass().getSimpleName() + " is already set");
                }
            }
        }
    }
}
