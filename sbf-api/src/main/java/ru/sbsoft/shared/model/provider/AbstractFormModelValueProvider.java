package ru.sbsoft.shared.model.provider;

import java.math.BigDecimal;
import ru.sbsoft.sbf.app.model.FormModel;

public abstract class AbstractFormModelValueProvider<VALUE, MODEL extends FormModel> implements IFormModelValueProvider<VALUE, MODEL> {

    public final static AbstractFormModelValueProvider<BigDecimal, FormModel> ID
            = new AbstractFormModelValueProvider<BigDecimal, FormModel>() {

                @Override
                public BigDecimal getValue(FormModel model) {
                    return model.getId();
                }

                @Override
                public void setValue(BigDecimal value, FormModel model) {
                    model.setId(value);
                }
            };

}
