package ru.sbsoft.form;

import java.math.BigDecimal;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.dao.IJdbcWorkExecutor;
import ru.sbsoft.dao.JdbcWorkExecutor;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.interfaces.IDynamicFormType;

/**
 *
 * @author vk
 */
public abstract class AbstractDynFormProcessor<M extends IFormModel> extends FormProcessorBase<M> implements IDynFormProcessor<M> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private IDynamicFormType formType;
    
    @Override
    public IDynamicFormType getFormType() {
        nn(formType, "formType");
        return formType;
    }

    @Override
    public void setFormType(IDynamicFormType formType) {
        this.formType = formType;
    }

    @Override
    public AbstractDynFormProcessor<M> init(EntityManager em, SessionContext scontext) {
        super.init(em, scontext);
        return this;
    }

    protected BigDecimal getDynId() {
        return getFormType().getId();
    }
    
    protected ILocalizedString getDynName(){
        return getFormType().getItemName();
    }
    
    protected IJdbcWorkExecutor getJdbcExecutor(){
        return new JdbcWorkExecutor(getEm());
    }

    protected <E extends Enum<E>> E getEnum(Class<E> cl, String val) {
        return val != null && !(val = val.trim()).isEmpty() ? Enum.valueOf(cl, val) : null;
    }
    
    private void nn(Object o, String objName){
        if (null == o) {
            String msg = objName + " is not initialized";
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }
    
    protected static void checkNotInitialized(Object... oo) {
        if (oo != null) {
            for (Object o : oo) {
                if (o != null) {
                    throw new IllegalStateException(o.getClass().getSimpleName() + " is already set");
                }
            }
        }
    }
}
