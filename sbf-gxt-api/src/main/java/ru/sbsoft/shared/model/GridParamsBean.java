package ru.sbsoft.shared.model;

import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.param.DTO;

/**
 * @author balandin
 * @since May 24, 2013 2:28:00 PM
 */
public class GridParamsBean implements DTO {

    private GridContext gridContext;
    private PageFilterInfo pageFilterInfo;
    private List<BigDecimal> marks;
    private BigDecimal selectedRecord;

    public GridParamsBean() {
    }

    public GridParamsBean(GridContext gridContext, PageFilterInfo pageFilterInfo, List<BigDecimal> marks, BigDecimal selectedRecord) {
        this.gridContext = gridContext;
        this.pageFilterInfo = pageFilterInfo;
        this.marks = marks;
        this.selectedRecord = selectedRecord;
    }

    public GridContext getGridContext() {
        return gridContext;
    }

    public void setGridContext(GridContext gridContext) {
        this.gridContext = gridContext;
    }

    public PageFilterInfo getPageFilterInfo() {
        return pageFilterInfo;
    }

    public void setPageFilterInfo(PageFilterInfo pageFilterInfo) {
        this.pageFilterInfo = pageFilterInfo;
    }

    public List<BigDecimal> getMarks() {
        return marks;
    }

    public void setMarks(List<BigDecimal> marks) {
        this.marks = marks;
    }

    public BigDecimal getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(BigDecimal selectedRecord) {
        this.selectedRecord = selectedRecord;
    }
}
