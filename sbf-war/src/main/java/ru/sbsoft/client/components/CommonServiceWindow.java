package ru.sbsoft.client.components;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import ru.sbsoft.svc.core.client.dom.ScrollSupport;
import ru.sbsoft.svc.widget.core.client.Window;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.BoxLayoutContainer;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.grid.CustomToolBar;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author Kiselev
 */
public abstract class CommonServiceWindow<C extends CommonServiceContainer> extends Window {

    protected int DEFAULT_WINDOW_WIDTH = 925;
    protected int DEFAULT_WINDOW_HEIGHT = 360;

    protected final ActionManager actionManager = new ActionManager();
    protected final C functionalContainer;
    protected final CustomToolBar toolBar;
    private final AbstractAction applyAction;
    private final AbstractAction cancelAction;
    private final TextButton applyButton;
    private ActionUpdater actionUpdater = null;

    protected CommonServiceWindow(C functionalContainer) {
        setPixelSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
        setMaximizable(false);
        setResizable(true);
        setModal(true);
        setBlinkModal(true);
        setConstrain(false);
        setOnEsc(true);

        this.functionalContainer = functionalContainer;

        toolBar = new CustomToolBar(actionManager, false);

        final VerticalLayoutContainer mainContainer = new VerticalLayoutContainer();
        mainContainer.setScrollMode(ScrollSupport.ScrollMode.NONE);
        mainContainer.add(toolBar, VLC.CONST);
        mainContainer.add(this.functionalContainer, VLC.FILL);

        setWidget(mainContainer);

        cancelAction = new CancelAction();
        applyAction = new ApplyAction();

        toolBar.addButton(cancelAction);

        addButton(applyButton = toolBar.createModalButton(applyAction));
        addButton(toolBar.createModalButton(cancelAction));

        setButtonAlign(BoxLayoutContainer.BoxLayoutPack.END);
        setFocusWidget(getButtonBar().getWidget(0));
    }

    @Override
    protected void onShow() {
        super.onShow();
        if (actionUpdater != null) {
            actionUpdater.cancel();
        }
        actionUpdater = new ActionUpdater();
        actionUpdater.scheduleRepeating(500);
        actionManager.updateState();
    }

    @Override
    protected void onHide() {
        super.onHide();
        if (actionUpdater != null) {
            actionUpdater.cancel();
            actionUpdater = null;
        }
    }

    protected final void setHeaderIcon(ImageResource icon) {
        getHeader().setIcon(icon);
    }

    protected final void setHeaderText(I18nResourceInfo inf, String... parameters) {
        getHeader().setText(I18n.get(inf, parameters));
    }

    protected void cancel() {
        hide();
    }

    protected AbstractAction getApplyAction() {
        return applyAction;
    }

    protected AbstractAction getCancelAction() {
        return cancelAction;
    }

    protected abstract void apply();

    protected boolean validate() {
        return functionalContainer.validate();
    }

    protected boolean isApplyEnabled() {
        return validate();
    }

    protected final void setApplyText(I18nResourceInfo t) {
        applyButton.setText(I18n.get(t));
    }

    protected final void setApplyHint(I18nResourceInfo h) {
        applyButton.setToolTip(I18n.get(h));
    }

    private class ApplyAction extends AbstractAction {

        ApplyAction() {
            setCaption(SBFBrowserStr.menuApply);
            setToolTip(SBFBrowserStr.hintApply);
            setIcon16(SBFResources.GENERAL_ICONS.OK16());
            setIcon24(SBFResources.GENERAL_ICONS.OK());
        }

        @Override
        protected void onExecute() {
            if (isApplyEnabled()) {
                apply();
            }
        }

        @Override
        public boolean checkEnabled() {
            return isApplyEnabled();
        }
    }

    private class CancelAction extends AbstractAction {

        CancelAction() {
            setCaption(SBFBrowserStr.menuCancel);
            setToolTip(SBFBrowserStr.hintCancel);
            setIcon16(SBFResources.GENERAL_ICONS.Exit16());
            setIcon24(SBFResources.GENERAL_ICONS.Exit());
        }

        @Override
        protected void onExecute() {
            cancel();
        }
    }

    private class ActionUpdater extends Timer {

        @Override
        public void run() {
            actionManager.updateState();
        }
    }
}
