package ru.sbsoft.operation;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import ru.sbsoft.common.IO;
import ru.sbsoft.dao.BrowserDao;
import ru.sbsoft.dao.GridSupport;
import ru.sbsoft.dao.IRendererManagerDao;
import ru.sbsoft.dao.ISecurityDao;
import ru.sbsoft.dao.ResultSetHandler;
import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.GridOperationParamConst;
import ru.sbsoft.shared.renderer.RendererManager;
import ru.sbsoft.generator.api.Lookup;

public abstract class AbstractExportOperationRunner extends GridOperationRunner implements ResultSetHandler {

    private long index;
    private BrowserDao templateBuilder;
    private XLSBuilder builder;
    @Lookup
    private GridSupport gridSupport;
    @Lookup
    private ISecurityDao securityDao;
    @Lookup
    private IRendererManagerDao rendererDao;

    public BrowserDao getTemplateBuilder() {
        if (templateBuilder == null) {
            templateBuilder = gridSupport.getTemplateBuilder(getGridContext());
        }
        return templateBuilder;
    }

    @Override
    public void run() throws Exception {
        String locale = getStringParam(GridOperationParamConst.GRID_SELECT_LOCALE);
        if (null == locale) {
            locale = "en";
        }

        index = 0;

        File file = null;
        try {
            file = File.createTempFile("export-", null);
            file.deleteOnExit();

            RendererManager rendererManager = rendererDao.getRendererManager();
            rendererManager.init();
            ColumnsInfo columnsInfo = getTemplateBuilder().getColumnsInfo();
            builder = new XLSBuilder(rendererManager, file, getI18nResource());
            builder.header(columnsInfo.getItems());

            initProgress(100);
            updateProgress(0);

            List<BigDecimal> ids = getMarks();

            final PageFilterInfo pageFilterInfo = getOperationCommand().getGridContext().getPageFilterInfo();
            // обновляем парент фильтр в pageFilterInfo, т.к. может поменяться в темплейте 
            pageFilterInfo.setParentFilters(getGridContext().getParentFilters());

            final String user = getOperationUsername();

            getTemplateBuilder().processResultSet(user, securityDao.isAdmin(user), pageFilterInfo, ids, this);
            builder.close();

            updateProgress(100);

            final String fileName = getCode() + "_" + getGridType().getCode() + ".xlsx";
            saveReport(file, fileName);
        } finally {
            IO.delete(file);
        }
    }

    @Override
    public void process(ResultSet resultSet) throws InterruptedException, SQLException {
        checkInterruptedLazy();
        index++;

        builder.row(getTemplateBuilder().getColumnsInfo().getItems(), resultSet);

        if (index % 1000 == 0) {
            updateProgress(100 - getProgressValue(), String.valueOf(index));
        }
    }
}
