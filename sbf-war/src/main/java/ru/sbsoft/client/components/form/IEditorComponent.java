package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import ru.sbsoft.svc.widget.core.client.Dialog;
import ru.sbsoft.svc.widget.core.client.box.ConfirmMessageBox;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author vk
 */
public interface IEditorComponent extends ReadOnlyControl, HasChangeState, ISavable, IsWidget {

    public enum ConfirmOption {
        HIDE_CANCEL, HIDE_NO
    }

    void finishEditing();

    default void confirmSave(final AsyncCallback<Boolean> answer) {
        confirmSave(answer, null);
    }

    default void requireUnchanged(AsyncCallback<Void> callback) {
        confirmSave(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(Boolean result) {
                callback.onSuccess(null);
            }
        }, ConfirmOption.HIDE_CANCEL);
    }

    default void ensureSaved(AsyncCallback<Void> callback) {
        confirmSave(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (Boolean.TRUE.equals(result)) {
                    callback.onSuccess(null);
                }
            }
        }, ConfirmOption.HIDE_NO);
    }

    default void confirmSave(final AsyncCallback<Boolean> answer, final ConfirmOption option) {
        finishEditing();
        if (isChanged()) {
            final boolean force = ConfirmOption.HIDE_CANCEL == option;
            final ConfirmMessageBox box = new ConfirmMessageBox(I18n.get(SBFBrowserStr.labelConfirmation), I18n.get(SBFBrowserStr.msgSaveConfirmation));
            if (force) {
                box.setPredefinedButtons(Dialog.PredefinedButton.YES, Dialog.PredefinedButton.NO);
            } else if (ConfirmOption.HIDE_NO == option) {
                box.setPredefinedButtons(Dialog.PredefinedButton.YES, Dialog.PredefinedButton.CANCEL);
            } else {
                box.setPredefinedButtons(Dialog.PredefinedButton.YES, Dialog.PredefinedButton.NO, Dialog.PredefinedButton.CANCEL);
            }
            box.addDialogHideHandler(event -> {
                if (null == event.getHideButton()) {
                    answer.onSuccess(false);
                } else {
                    switch (event.getHideButton()) {
                        case NO:
                            clearChanges();
                            answer.onSuccess(true);
                            break;
                        case YES:
                            save(new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    answer.onFailure(caught);
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    answer.onSuccess(true);
                                }
                            });
                            break;
                        default:
                            if (force) {
                                clearChanges();
                                answer.onSuccess(true);
                            } else {
                                answer.onSuccess(false);
                            }
                            break;
                    }
                }
            });
            box.show();
            ClientUtils.center(box, this);
        } else {
            answer.onSuccess(true);
        }
    }
}
