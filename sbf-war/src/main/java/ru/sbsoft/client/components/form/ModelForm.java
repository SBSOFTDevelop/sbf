package ru.sbsoft.client.components.form;

import ru.sbsoft.svc.widget.core.client.Dialog.PredefinedButton;
import ru.sbsoft.svc.widget.core.client.box.AlertMessageBox;
import ru.sbsoft.svc.widget.core.client.box.ConfirmMessageBox;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.event.DialogHideEvent;
import ru.sbsoft.svc.widget.core.client.form.Field;
import ru.sbsoft.svc.widget.core.client.menu.Item;
import ru.sbsoft.svc.widget.core.client.toolbar.ToolBar;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.BaseWindow;
import ru.sbsoft.client.components.SBSelectHandler;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

import java.io.Serializable;
import java.util.List;

/**
 * Форма для отображения модели данных. 
 * В настоящее время используется только для построения {@link ru.sbsoft.client.components.kladr.AddressEditForm}.
 * @deprecated используйте {@link BaseForm}
 * @see BaseForm
 * @author Sokoloff
 */
public abstract class ModelForm<T extends Serializable> extends BaseWindow {
	
    private T dataModel;
    private TextButton btnSaveClose;
    private boolean readOnly;
    private final FormChangesControl changesControl = new FormChangesControl();
    private List<Field> listEditable;

    public ModelForm() {
        super();

        setWidth(500);
        setModal(true);

        final ToolBar mainToolBar = new ToolBar();
        mainToolBar.setSpacing(2);
        addRegion(mainToolBar, VLC.CONST);
        fillToolBar(mainToolBar);

        createEditors();

        listEditable = getFieldList();
		for (Field field : listEditable) {
			changesControl.registr(field);
		}
    }

    protected void fillToolBar(final ToolBar toolBar) {
        TextButton btnExit = new TextButton();
        btnExit.setIcon(SBFResources.GENERAL_ICONS.Exit());
        btnExit.setToolTip(I18n.get(SBFBrowserStr.hintFileExit));
        btnExit.addSelectHandler(handlerExit);
        toolBar.add(btnExit);

        btnSaveClose = new TextButton();
        btnSaveClose.setIcon(SBFResources.EDITOR_ICONS.Save());
        btnSaveClose.setToolTip(I18n.get(SBFEditorStr.hintSave));
        btnSaveClose.addSelectHandler(handlerSaveClose);
        toolBar.add(btnSaveClose);
    }
    private SBSelectHandler<Item> handlerExit = new SBSelectHandler<Item>() {
        @Override
        public void onSelectEvent() {
            ModelForm.this.hide();
        }
    };
    private SBSelectHandler<Item> handlerSaveClose = new SBSelectHandler<Item>() {
        @Override
        public void onSelectEvent() {
            saveClose();
        }
    };

    public boolean isChanged() {
        finishEditing();
        return changesControl.isChanged();
    }

    protected void setChanged(boolean changed) {
        changesControl.setChanged(changed);
    }

    protected void finishEditing() {
        btnSaveClose.focus();
        for (Field item : listEditable) {
            item.finishEditing();
        }
    }

    public void show(T model) {
        setDataModel(model);
        super.show();
    }

    @Override
    public void show() {
        show(null);
    }

    private void close() {
        super.hide();
    }

    protected void saveClose() {
        if (isChanged() && !checkValidData()) {
            return;
        }
        formToData(dataModel);
        setChanged(false);
        onSave();
        close();
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        for (Field item : listEditable) {
            item.setReadOnly(readOnly);
        }
        btnSaveClose.setEnabled(!readOnly);
        this.readOnly = readOnly;
    }

    private boolean checkValidData() {
        finishEditing();
        for (Field item : listEditable) {
            if (!item.isValid()) {
                String name = null == item.getName() || item.getName().isEmpty() ? item.getToolTip().getToolTipConfig().getBody().asString() : item.getName();
                AlertMessageBox d = new AlertMessageBox(I18n.get(SBFGeneralStr.captQuery), 
                        I18n.get(SBFGeneralStr.msgFieldNotProperlyData, name));
                d.show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void hide() {
        if (!isChanged()) {
            close();
        } else {
            ConfirmMessageBox box = new ConfirmMessageBox(I18n.get(SBFGeneralStr.captQuery), 
                    I18n.get(SBFGeneralStr.msgModify));
            box.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
            box.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == PredefinedButton.CANCEL) {
                        //
                    } else if (event.getHideButton() == PredefinedButton.YES) {
                        saveClose();
                    } else {
                        close();
                    }
                }
            });
            box.show();
        }
    }

    private void setDataModel(T dataModel) {
        this.dataModel = dataModel;
        dataToForm(dataModel);
        setChanged(false);
    }

    public T getDataModel() {
        return dataModel;
    }

    protected abstract void createEditors();

    protected abstract void dataToForm(final T dataModel);

    protected abstract void formToData(final T dataModel);

    protected abstract void onSave();
}
