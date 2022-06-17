package ru.sbsoft.client.components.form.handler;

import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.form.Validator;
import java.util.List;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.client.components.browser.filter.editor.FilterEditor;
/**
 *
 * @author Kiselev
 * @param <F>
 * @param <V>
 * @param <SelfType>
 */
public interface IFieldHandler<F extends Component, V, SelfType extends IFieldHandler<F, V, SelfType>> extends FilterEditor<F> {

    SelfType setReq();
    
    SelfType setReq(boolean required);

    SelfType setToolTip(String s);

    SelfType setRO();
    
    SelfType setRO(boolean readOnly);
    
    boolean isRO();

    SelfType addValidator(Validator<V> validator);
    
    List<Validator<V>> getValidators();

    Widget getWidget();

    boolean isEmpty();

    F getField();

    String getLabel();

    SelfType setVisible(boolean b);

    SelfType setVal(V val);

    V getVal();

    String getHumanVal();

    boolean setFilterValue(FilterInfo config);

    FilterInfo getFilter();

    ParamInfo getParam();
    
    void setEnabled(boolean enabled);
    
    boolean isParamGen();

    SelfType setParamGen(boolean b);
    
    String getName();
    
    SelfType setComparison(ComparisonEnum c);
    
}
