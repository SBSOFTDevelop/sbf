package ru.sbsoft.client.components.browser;

import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.IBrowserMaker;
import ru.sbsoft.client.components.ManagedBrowser;
import ru.sbsoft.client.components.WidgetBrowser;
import ru.sbsoft.client.components.form.FormGridView2;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.schedule.i18n.SBFi18nLocale;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.NamedFormContext;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.api.i18n.consts.SBFReportStr;
import ru.sbsoft.shared.consts.Dict;
import ru.sbsoft.shared.consts.SBFGridEnum;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.meta.Row;

/**
 * Браузер настройки пользовательских отчетов
 *
 * @author sokolov
 */
public class CustomReportBrowserMaker implements IBrowserMaker {
    
    private final NamedFormContext formContext;

    public CustomReportBrowserMaker(NamedFormContext formContext) {
        this.formContext = formContext;
    }

    @Override
    public String getIdBrowser() {
        return SBFGridEnum.SR_CUSTOMREPORT.getCode();
    }

    @Override
    public String getTitleBrowser() {
        return I18n.get(SBFReportStr.titleCustomReports);
    }

    @Override
    public ManagedBrowser createBrowser(String idBrowser, String titleBrowser) {
        final BorderLayoutContainer mainContainer = new BorderLayoutContainer();

        final BorderLayoutContainer brContainer = new BorderLayoutContainer();
        final BrowserFactory brFactory = new BrowserFactory(SBFGridEnum.SR_GRIDLIST);
        final BaseGrid brGrid = brFactory.createGrid();
        brGrid.setGridService(SBFConst.GRIDLIST_SERVICE);
        brGrid.setParentFilters(new StringFilterInfo(Dict.LOCALE, SBFi18nLocale.getLocaleName()));
        final FormGridView2 brView = new FormGridView2(I18n.get(SBFGeneralStr.titleBrowsers), brGrid, false, true, new GridMode[]{
            GridMode.HIDE_INSERT,
            GridMode.HIDE_DELETE,
            GridMode.HIDE_UPDATE,
            GridMode.SINGLE_SELECTION,
            GridMode.HIDE_MOVE,
            GridMode.HIDE_FILTER});
        brGrid.checkInitialized();
        brContainer.setCenterWidget(brView);
        mainContainer.setCenterWidget(brContainer);
        
        final BorderLayoutContainer repContainer = new BorderLayoutContainer();
        final BaseGrid repGrid = new CustomReportGrid(formContext);
        brGrid.getGrid().getSelectionModel().addSelectionChangedHandler(event -> {
            List<Row> sels = event.getSelection();
            if (sels != null && !sels.isEmpty()) {
                Row row = sels.get(0);
                List<FilterInfo> filters = new ArrayList<>();
                filters.add(new StringFilterInfo(Dict.CGRID_CODE, ComparisonEnum.eq, (String)row.getValue(Dict.CGRID_CODE)));
                filters.add(new StringFilterInfo(Dict.CGRID_TYPE, ComparisonEnum.eq, (String)row.getValue(Dict.CGRID_TYPE)));
                repGrid.setParentFilters(filters);
            }
        });
        final FormGridView2 repView = new FormGridView2(I18n.get(SBFReportStr.titleCustomReports),repGrid);
        repGrid.checkInitialized();
        repContainer.setCenterWidget(repView);
        BorderLayoutContainer.BorderLayoutData repLayout = new BorderLayoutContainer.BorderLayoutData(.4);
        repLayout.setSplit(true);        
        mainContainer.setSouthWidget(repContainer, repLayout);

        ManagedBrowser b = new WidgetBrowser(mainContainer);
        b.setIdBrowser(idBrowser);
        b.setCaption(titleBrowser);
        return b;
    }

    @Override
    public String getSecurityId() {
        return SBFGridEnum.SR_CUSTOMREPORT.getSecurityId();
    }

}
