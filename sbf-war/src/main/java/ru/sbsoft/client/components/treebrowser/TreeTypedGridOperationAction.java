package ru.sbsoft.client.components.treebrowser;

import java.math.BigDecimal;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.operation.OperationMaker;
import ru.sbsoft.shared.model.MarkModel;

/**
 *
 * @author sokolov
 */
public class TreeTypedGridOperationAction extends TreeGridOperationAction {
        public enum TYPE_USE_MODE {

        /**
         * Будут обрабатываться только строки, выделенные мышью в таблице.
         */
        ON_SELECTED_RECORD,
        /**
         * Никакие строки не будут отправлены на обработку.
         */
        STANDALONE
    }

    protected final TYPE_USE_MODE typeUseMode;

    public TreeTypedGridOperationAction(AbstractTreeGrid grid, OperationMaker operationMaker, TYPE_USE_MODE typeUseMode) {
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
        if (typeUseMode == TYPE_USE_MODE.ON_SELECTED_RECORD) {
            return null != getSingleSelectedRecordUq();
        }
        return true;

    }

    protected BigDecimal getSingleSelectedRecordUq() {
        MarkModel selection = getGrid().getSelectedModel();
        if (selection != null) {
            return selection.getRECORD_ID();
        } else {
            return null;
        }
    }

}
