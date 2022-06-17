package ru.sbsoft.client.components.operation;

import ru.sbsoft.svc.widget.core.client.TabItemConfig;
import ru.sbsoft.svc.widget.core.client.TabPanel;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent.SelectHandler;
import ru.sbsoft.svc.widget.core.client.toolbar.ToolBar;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.BaseWindow;
import ru.sbsoft.client.components.form.SBFFormPanelHelper;
import ru.sbsoft.client.components.form.SimplePageFormContainer;
import ru.sbsoft.sbf.svc.components.VerticalFieldSet;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Базовая форма для задания параметров операции. Предоставляет общее для всех
 * форм поведение. Наследники реализуют требуемые параметры в методах
 * {@link #fillParameterPage(ru.sbsoft.sbf.svc.components.VerticalFieldSet)} и
 * {@link #getParams()}.
 */
public abstract class BaseOperationParamForm extends BaseWindow {

    private boolean needStart = false;
    private final TextButton runButton = new TextButton();
    private int labelWidth = 180;

    public BaseOperationParamForm(final String header) {
        setHeading(header);
        setModal(true);
        setClosable(false);
        setWidth(520);
    }

    public int getLabelWidth() {
        return labelWidth;
    }

    public final void setLabelWidth(int labelWidth) {
        this.labelWidth = labelWidth;
    }

    @Override
    protected void onShow() {
        needStart = false;
        super.onShow();
    }

    @Override
    protected void onAfterFirstAttach() {
        super.onAfterFirstAttach();
        fillViewForm();
    }

    private void fillViewForm() {
        final ToolBar mainToolBar = new ToolBar();
        mainToolBar.setSpacing(2);
        fillToolBar(mainToolBar);
        addRegion(mainToolBar, VLC.CONST);

        final TabPanel mainTab = new TabPanel();
        mainTab.setTabScroll(true);
        fillTabPanel(mainTab);
        addRegion(mainTab, VLC.FILL);
    }

    protected void fillToolBar(final ToolBar toolBar) {
        toolBar.add(createExitButton());
        toolBar.add(createRunButton());
    }

    private TextButton createExitButton() {
        final TextButton exitButton = new TextButton();
        exitButton.setIcon(SBFResources.GENERAL_ICONS.Exit());
        exitButton.setToolTip(I18n.get(SBFGeneralStr.labelCloseWindow));
        exitButton.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(final SelectEvent event) {
                needStart = false;
                hide();
            }
        });
        return exitButton;
    }

    private TextButton createRunButton() {
        runButton.setIcon(SBFResources.GENERAL_ICONS.Run());
        runButton.setToolTip(I18n.get(SBFGeneralStr.hintRun));
        runButton.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                run();
            }
        });
        return runButton;
    }

    protected void run() {
        if (SBFFormPanelHelper.validate(getWindow())) {
            needStart = true;
            hide();
        } else {
            ClientUtils.message(I18n.get(SBFGeneralStr.labelCheckData), getCustomValidateMessage());
        }

    }

    protected String getCustomValidateMessage() {
        return I18n.get(SBFGeneralStr.msgNotFilledFields);
    }

    protected SimplePageFormContainer fillTabPanel(TabPanel mainTab) {
        final SimplePageFormContainer containerMain = new SimplePageFormContainer(labelWidth);
        VerticalFieldSet fieldSet = new VerticalFieldSet(null);
        fillParameterPage(fieldSet);
        containerMain.addFieldSet(fieldSet);
        containerMain.updateLabels();
        addRegion(containerMain, VLC.FILL);
        mainTab.add(containerMain, new TabItemConfig(I18n.get(SBFGeneralStr.labelOperationParams), false));
        return containerMain;
    }

    protected abstract void fillParameterPage(final VerticalFieldSet fieldSet);

    public boolean isNeedStart() {
        return needStart;
    }

    public TextButton getRunButton() {
        return runButton;
    }

    public abstract List<ParamInfo> getParams();
}
