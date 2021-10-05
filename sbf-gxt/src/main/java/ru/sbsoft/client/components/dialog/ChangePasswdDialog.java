/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sbsoft.client.components.dialog;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;

import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.model.user.PasswordPolicy;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFPasswdChars;
import ru.sbsoft.client.utils.DefaultAsyncCallback;

/**
 * Диалог смены пароля
 *
 * @author sychugin
 */
public final class ChangePasswdDialog extends Window {

    public ChangePasswdDialog(final PasswordPolicy pPolicy) {
        this(pPolicy, true, null);

    }

    public ChangePasswdDialog(final PasswordPolicy pPolicy, final boolean isCanceled, final Runnable runner) {
        super();
        //setPixelSize(-1, -1);
        setWidth(350);
        setModal(true);
        setBlinkModal(true);
        setConstrain(false);
        setResizable(false);

        setClosable(false);

        setButtonAlign(BoxLayoutContainer.BoxLayoutPack.CENTER);

        getHeader().setIcon(SBFResources.GENERAL_ICONS.Password());
        getHeader().setText(I18n.get(SBFGeneralStr.labelChangePassword));

        final VerticalLayoutContainer vc = new VerticalLayoutContainer();
        vc.getElement().setPadding(new Padding(3, 4, 0, 4));
        final PasswordField newPassword = new PasswordField();
        final PasswordField repeatPassword = new PasswordField();
        final PasswordField oldPassword = new PasswordField();

        newPassword.setAutoValidate(true);
        newPassword.setAllowBlank(false);

        repeatPassword.setAutoValidate(true);

//проверка на мин. длину
        if (pPolicy.getMinLength() > 0) {
            newPassword.addValidator(new MinLengthValidator(pPolicy.getMinLength()));
        }

        //ToDo проверка на сложность
        if (pPolicy.hasCaseChar()) {
            newPassword.addValidator(new RegExValidator(SBFPasswdChars.CASE_SENSITIVE_CHARS.getRegExp(),
                    SBFPasswdChars.CASE_SENSITIVE_CHARS.getTitle()));
        }
        if (pPolicy.hasSpecChar()) {
            newPassword.addValidator(new RegExValidator(SBFPasswdChars.SPECIAL_CHARS.getRegExp(),
                    SBFPasswdChars.SPECIAL_CHARS.getTitle()));
        }

        //проверка на совпадение
        repeatPassword.addValidator(new Validator<String>() {
            @Override
            public List<EditorError> validate(Editor<String> editor, String value) {

                if (value == null || !value.equals(newPassword.getValue())) {
                    return Collections.<EditorError>singletonList(new DefaultEditorError(editor, "Пароли не совпадают", value));

                }
                return null;
            }
        });

        //вызов из меню -нужно подставить старый пароль
        if (isCanceled) {

            vc.add(new FieldLabel(oldPassword, I18n.get(SBFGeneralStr.labelOldPassword)));

            TextButton cancelButton = new TextButton(I18n.get(SBFGeneralStr.labelCancelOperation));
            addButton(cancelButton);
            cancelButton.addSelectHandler((SelectEvent event) -> {
                hide();
            });

        }
        vc.add(new FieldLabel(newPassword, I18n.get(SBFGeneralStr.labelNewPassword)));
        vc.add(new FieldLabel(repeatPassword, I18n.get(SBFGeneralStr.labelConfirmPassword)));

        add(vc);

        TextButton applyButton = new TextButton(I18n.get(SBFGeneralStr.labelApply));
        addButton(applyButton);
        applyButton.addSelectHandler((SelectEvent event) -> {
            if (newPassword.isValid() && repeatPassword.isValid()) {

                if (isCanceled) {
                    save(oldPassword.getValue(), newPassword.getValue());
                } else {
                    save(newPassword.getValue());
                }

                if (runner != null) {
                    runner.run();
                }
                hide();
            }
        });

    }

    private void save(final String oldPasswd, final String newPasswd) {
        SBFConst.SECURUTY_SERVICE.changePassword(oldPasswd, newPasswd, new DefaultAsyncCallback<Void>());
    }

    private void save(final String passwd) {
        SBFConst.SECURUTY_SERVICE.setNewPassword(passwd, new DefaultAsyncCallback<Void>());
    }

}
