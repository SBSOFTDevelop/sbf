package ru.sbsoft.client.components.form;

import ru.sbsoft.client.components.form.handler.IFieldHandler;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author sokolov
 * @param <M>
 */
public class FieldRuleModifyNew<M extends IFormModel> implements ModelChangeListener<M> {

        protected final IFieldHandler h;

        public FieldRuleModifyNew(IFieldHandler h) {
            this.h = h;
        }

        @Override
        public void modelChanged(M m) {
            h.setRO(m == null || m.getId() != null);
        }
    }