/**
 * Not auto complete password field
 * Затычка для дюже умного Хрома Хром автоподставляет пароль и логин в окно
 * ввода нового пользователя берет его с сохраненной ранее пары при логине в
 * приложения
 *
 * @author sychugin
 */
package ru.sbsoft.client.components.field;

import com.google.gwt.user.client.Event;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.form.PasswordField;

/**
 *
 * @author sychugin
 */
public class PasswordNAField extends PasswordField {

    public PasswordNAField() {
        super();

    }

    @Override
    protected void onRedraw() {
        super.onRedraw();
        //getInputEl().setAttribute("autocomplete", "off");
        XElement input = getElement().selectNode("input");
        if (input != null) {
            input.setAttribute("readonly", "true");

            //getInputEl().setAttribute("readonly", "true");
            input.setAttribute("onfocus", "this.removeAttribute('readonly')");
        }
    }

    @Override
    protected void onFocus(Event event) {

        super.onFocus(event);
    }

}
