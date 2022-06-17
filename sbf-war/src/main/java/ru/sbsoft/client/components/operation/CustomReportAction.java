package ru.sbsoft.client.components.operation;

import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.shared.CommonOperationEnum;
import ru.sbsoft.shared.model.CustomReportInfo;

/**
 *
 * @author sokolov
 */
public class CustomReportAction extends PrintGridOperationAction {
    
    public CustomReportAction(BaseGrid grid, CustomReportInfo info) {
        super(grid, new SimpleOperationMaker(CommonOperationEnum.CUSTOM_REPORT, grid, () -> new CustomReportParamForm(grid, info), false), TYPE_USE_MODE.STANDALONE);// info.getIncudeIdRow() ? TYPE_USE_MODE.ON_SELECTED_RECORD : TYPE_USE_MODE.STANDALONE);
        setToolTip(info.getTitle());
        setCaption(info.getTitle());
    }
    
}
