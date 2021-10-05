package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import java.util.function.Function;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.BaseWindow;
import static ru.sbsoft.client.components.form.BaseForm.callback;
import ru.sbsoft.client.components.form.fields.Adapter;
import ru.sbsoft.client.components.form.handler.form.builder.FSet;
import ru.sbsoft.client.components.form.handler.form.builder.IFaceBuilder;
import ru.sbsoft.client.components.form.model.ITehnologyModelAccessor;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.NamedFormType;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author sychugin
 */
public abstract class WizardFormFactory<M extends IFormModel> extends BaseFormFactory<M, Row> {

    public WizardFormFactory(NamedFormType formType) {
        super(formType);
    }

    @Override
    protected final WizardFactoryForm createFormInstance() {
        return new WizardFactoryForm();
    }

    protected void createHandlers(final WizardFactoryForm form) {
    }

    protected void configForm(final WizardFactoryForm form) {
    }

    protected final <A extends ITehnologyModelAccessor> FSet<M> createTehnologySet(A a) {
        return BaseValidatedForm.createTehnologySet(a);
    }

    protected abstract void buildFace(final IFaceBuilder<M> b, final WizardFactoryForm form);

    protected boolean isCompletedStep(final int Step) {

        return true;
    }

    protected void postBuildFace(final IFaceBuilder<M> b, final WizardFactoryForm form) {
    }

    private void constructFace(final IFaceBuilder<M> b, final WizardFactoryForm form) {
        createHandlers(form);
        buildFace(b, form);
        postBuildFace(b, form);
    }

    public enum WizardButton {

        PREV("Назад"),
        NEXT("Далее"),
        CANCEL("Выход"),
        OK("Завершить");
        private final ILocalizedString title;

        private WizardButton(String title) {
            this.title = new NonLocalizedString(title);

        }

        public ILocalizedString getTitle() {
            return title;
        }
    }

    protected final class WizardFactoryForm extends BaseFactoryForm {

        private class NextUpdater extends Timer {

            @Override
            public void run() {
                updateNextButtons();
            }
        }

        private final class StepToolBar extends ToolBar {

            private final Label lstep = new Label("0");

            private final Label lstepTitle = new Label("");

            StepToolBar() {

                setSpacing(2);
                add(new Label("Шаг:"));
                add(new SeparatorToolItem());
                add(lstep);

                add(new SeparatorToolItem());
                add(lstepTitle);

                setVisible(true);
            }

            public void updateStepInfo() {

                String title = getMainTab().getConfig(getTabs().getWidget(step)).getText();

                lstep.setText("" + (step + 1) + " из " + count+":");
                lstepTitle.setText(title);
            }
        }

        private int count;
        private int step = 0;
        private boolean isSave;
        private boolean forceClose = false;

        private Function<Integer, Boolean> completed;

        private StepToolBar stepTb;

        private NextUpdater nextUpdater = null;

        private final SelectEvent.SelectHandler handler = (SelectEvent event) -> {
            onButtonPressed((TextButton) event.getSource());
        };

        public WizardFactoryForm() {

            getMainTab().getAppearance().getBar(getMainTab().getElement()).setHeight(0);

            setModal(true);
            final String title = I18n.get(SBFEditorStr.captionWizard) + ": "
                    + getCaption();

            setModcaption(title + "+");
            getWindow().addShowHandler((event) -> {
                if (nextUpdater != null) {
                    nextUpdater.cancel();
                }
                nextUpdater = new NextUpdater();
                nextUpdater.scheduleRepeating(500);

            });

            getWindow().addBeforeHideHandler((event) -> {
                if (nextUpdater != null) {
                    nextUpdater.cancel();
                    nextUpdater = null;
                }

            });

        }

        private void updateNextButtons() {

            getButton(WizardButton.NEXT).setEnabled(isCompletedTab(step));
            getButton(WizardButton.OK).setEnabled(isCompletedTab(count - 1));

        }

        @Override
        protected FormToolBar createFormToolBar() {
            return null;
        }

        @Override
        protected void createEditors(TabPanel tabEditors) {
            super.createEditors(tabEditors);
            count = getTabs().getWidgetCount();
            createButtons();
            updateButtonsState();

        }

        private boolean isOnTab(Widget tab, Widget w) {
            while ((w = w.getParent()) != null) {
                if (w.getParent() == tab) {

                    return true;
                }
            }

            return false;
        }

        public void setCompleted(Function<Integer, Boolean> completed) {
            this.completed = completed;
        }

        private boolean isCompletedTab(final int step) {

            Widget parent = getTabs().getWidget(step);

            if (parent == null) {
                return true;
            }
            for (Adapter adapter : getFields().values()) {

                if (!isOnTab(parent, adapter.getWidget())) {
                    continue;
                }

                adapter.finishEditing();

                if (!adapter.isValid()) {
                    alert(adapter.getWidget());
                    return false;
                } else {
                    adapter.clearInvalid();
                }
            }

            return completed == null ? true : completed.apply(step);
        }

        @Override
        protected void initBars(BaseWindow window) {

            window.addRegion(stepTb = new StepToolBar(), VLC.CONST);

        }

        protected void createButtons() {

            Window window = getWindow();

            // window.addShowHandler((event) -> {
            //  });
            Widget focusWidget = window.getFocusWidget();
            boolean focus = focusWidget == null || (focusWidget != null && window.getButtonBar().getWidgetIndex(focusWidget) != -1);
            window.getButtonBar().clear();

            for (WizardButton wb : WizardButton.values()) {

                TextButton tb = new TextButton(wb.title.getDefaultName());
                tb.setItemId(wb.name());
                tb.addSelectHandler(handler);
                // if (i == 0 && focus) {
                //      window.setFocusWidget(tb);
                //  }
                window.addButton(tb);
            }

        }

        public TextButton getButton(WizardButton wizardButton) {
            return (TextButton) getWindow().getButtonBar().getItemByItemId(wizardButton.name());
        }

        private boolean isFirstTab() {

            return getTabs().
                    getActiveWidget() == getTabs().getWidget(0);
        }

        private boolean isLastTab() {

            return getTabs()
                    .getActiveWidget() == getTabs()
                            .getWidget(count - 1);
        }

        @Override
        protected boolean validate() {

            for (Adapter adapter : getFields().values()) {
                if (adapter.getWidget() instanceof StrStrComboBox) {
                    adapter.clearInvalid();
                }
            }

            return super.validate();

        }

        protected void updateButtonsState() {

            getButton(WizardButton.OK).setVisible(isLastTab());

            getButton(WizardButton.NEXT).setVisible(!isLastTab());

            getButton(WizardButton.PREV).setVisible(!isFirstTab());

            if (getWindow().getButtonBar().isAttached()) {
                getWindow().getButtonBar().forceLayout();
            }
            stepTb.updateStepInfo();
        }

        private CardLayoutContainer getTabs() {

            return getMainTab().getContainer();
        }

        private void nextTab() {

            if (step < count && isCompletedTab(step)) {

                getTabs().setActiveWidget(getTabs().getWidget(++step));
                updateButtonsState();

            }
        }

        private void prevTab() {

            if (step > 0) {

                getTabs().setActiveWidget(getTabs().getWidget(--step));
                updateButtonsState();

            }

        }

        protected void onButtonPressed(TextButton textButton) {
            if (textButton.getItemId().equals(WizardButton.CANCEL.name())) {

                hide();

            } else if (textButton.getItemId().equals(WizardButton.OK.name())) {

                isSave = true;
                hide();

            }

            if (textButton.getItemId().equals(WizardButton.PREV.name())) {

                prevTab();

            } else if (textButton.getItemId().equals(WizardButton.NEXT.name())) {

                nextTab();
            }

            updateButtonsState();
        }

        @Override
        public boolean checkSaveChanges(final Runnable callback) {
            if (!isChanged() || forceClose) {
                forceClose = false;
                return true;
            }
            if (isSave) {
                isSave = false;
                save(callback);
                return false;
            } else {
                ConfirmMessageBox box = new ConfirmMessageBox(I18n.get(SBFGeneralStr.captQuery),
                        "Выйти из мастера без продолжения ?");
                box.setPredefinedButtons(Dialog.PredefinedButton.YES, Dialog.PredefinedButton.CANCEL);
                box.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            forceClose = true;
                            callback(callback);

                        }

                    }
                });
                box.show();
                return false;
            }
        }

        @Override
        protected FormMenu createFormMenu() {
            return null;
        }

        @Override
        protected void buildFace(IFaceBuilder<M> b) {
            WizardFormFactory.this.constructFace(b, this);
        }

    }
}
