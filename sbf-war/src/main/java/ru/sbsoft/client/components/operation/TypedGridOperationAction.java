package ru.sbsoft.client.components.operation;

import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.actions.GridOperationAction;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.shared.model.MarkModel;

/**
 *
 * @author Sokoloff
 */
public class TypedGridOperationAction extends GridOperationAction {

    public enum TYPE_USE_MODE {

        /**
         * Будут обрабатываться только строки, выделенные мышью в таблице.
         */
        ON_SELECTED_RECORD,
        /**
         * Будут обрабатываться только омеченные макркером строки.
         */
        ON_PINS,
        /**
         * Никакие строки не будут отправлены на обработку.
         */
        STANDALONE
    }

    protected final TYPE_USE_MODE typeUseMode;

    public TypedGridOperationAction(BaseGrid grid, OperationMaker operationMaker, TYPE_USE_MODE typeUseMode) {
        super(grid, operationMaker);
        this.typeUseMode = typeUseMode;
        if (operationMaker != null) {
            String cap = I18n.get(operationMaker.getType().getTitle());
            super.setCaption(cap);
            super.setToolTip(cap);
        }
    }

    @Override
    public boolean checkEnabled() {
        if (!getGrid().isInitialized()) {
            return false;
        }
        switch (typeUseMode) {
            case ON_SELECTED_RECORD: {
                return null != getSingleSelectedRecordUq();
            }
            case ON_PINS: {
                final List<BigDecimal> pins = getGrid().getMarkedRecords();
                return null != pins && !pins.isEmpty();
            }
            default: {
                return true;
            }
        }

    }

    protected BigDecimal getSingleSelectedRecordUq() {
        final List<MarkModel> selection = getGrid().getSelectedRecords();
        if (selection != null && !selection.isEmpty()) {
            return selection.get(0).getRECORD_ID();
        } else {
            return null;
        }
    }

}
