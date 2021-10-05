package ru.sbsoft.client.components.grid;

import ru.sbsoft.sbf.app.utils.RegistrationUtils;
import ru.sbsoft.sbf.app.Registration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import java.util.*;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.EditFormFactoryInfo.FormFactory;
import ru.sbsoft.client.utils.VLD;
import ru.sbsoft.sbf.app.utils.CollectionUtils;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.exceptions.ApplicationException;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class DefaultSelectForm implements ISelectForm {

    private Window window = new Window();
    private boolean formFound = false;
    private final static int LINE_HEIGHT = 40;

    private List<SelectFormHandler> selectFormHandlers;

    public DefaultSelectForm(Collection<FormInfo> formInfoList) {
        window.setHeading(I18n.get(SBFGeneralStr.labelSelectTypeRec));
        final VerticalLayoutContainer buttonPanel = new VerticalLayoutContainer();

        for (final FormInfo editFormInfo : formInfoList) {
            final TextButton button = new TextButton(editFormInfo.getName());
            button.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    window.hide();
                    DefaultSelectForm.this.onSelect(editFormInfo.getFormFactory());
                }
            });
            buttonPanel.add(button, new VLD(1, LINE_HEIGHT));
            formFound = true;
        }

        window.add(buttonPanel, new MarginData(5));
        window.setHeight(formInfoList.size() * LINE_HEIGHT + 50);
        window.setWidth(300);
    }

    @Override
    public void show() {
        if (!formFound) {
            throw new ApplicationException(SBFExceptionStr.noInsertForm);
        }
        window.show();
    }

    private List<SelectFormHandler> getSelectFormHandlers() {
        if (selectFormHandlers == null) {
            selectFormHandlers = new ArrayList<SelectFormHandler>();
        }
        return selectFormHandlers;
    }

    public Registration addSelectFormHandler(SelectFormHandler handler) {
        return RegistrationUtils.register(getSelectFormHandlers(), handler);
    }

    private void onSelect(FormFactory formFactory) {
        for (SelectFormHandler handler : CollectionUtils.wrapList(getSelectFormHandlers())) {
            handler.onSelect(formFactory);
        }
    }

    public static class FormInfo {

        private String name;
        private FormFactory formFactory;

        public FormInfo() {
        }

        public FormInfo(String name, FormFactory formFactory) {
            this.name = name;
            this.formFactory = formFactory;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public FormFactory getFormFactory() {
            return formFactory;
        }

        public void setFormFactory(FormFactory formFactory) {
            this.formFactory = formFactory;
        }

    }

}
