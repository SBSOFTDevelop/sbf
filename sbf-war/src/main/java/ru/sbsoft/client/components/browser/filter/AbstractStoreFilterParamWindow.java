package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.HTML;
import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.core.client.dom.ScrollSupport;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import ru.sbsoft.svc.widget.core.client.form.SimpleComboBox;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.CommonServiceContainer;
import ru.sbsoft.client.components.CommonServiceWindow;
import ru.sbsoft.client.components.form.SimplePageFormContainer;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.RoleCheker;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.api.i18n.consts.SBFFormStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.shared.model.user.Group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Kiselev
 */
public abstract class AbstractStoreFilterParamWindow extends CommonServiceWindow {

    private static final int FIELD_WIDTH = 300;
    protected final SimpleComboBox<Group> group;
    protected final TextField name;
    protected final HTML status;

    public AbstractStoreFilterParamWindow() {
        super(new CommonServiceContainer());
        setHeaderIcon(SBFResources.BROWSER_ICONS.FilterSaved16());
        setHeaderText(SBFBrowserStr.menuFilterSave);
        setPixelSize(-1, -1);
        setMinWidth(0);
        setMinHeight(0);
        setResizable(false);
        toolBar.removeFromParent();
        setApplyText(SBFGeneralStr.labelSave);
        setApplyHint(SBFBrowserStr.hintFilterSave);

        group = new SimpleComboBox<>(new ComboBoxCell<>(new ListStore<Group>(Group::getCode), Group::getName));
        List<Group> groups = new ArrayList<Group>(RoleCheker.getInstance().getGroups());
        Collections.sort(groups, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        group.add(groups);
        group.setToolTip(I18n.get(SBFBrowserStr.hintFilterSaveGroup));
        group.setWidth(FIELD_WIDTH);

        name = new TextField();
        name.setToolTip(I18n.get(SBFBrowserStr.hintFilterSaveName));
        name.setWidth(FIELD_WIDTH);
        name.addKeyDownHandler(event -> {
             if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
                 getApplyAction().perform();
             }
         });
        
        status = new HTML();

        VerticalLayoutContainer fieldContainer = new VerticalLayoutContainer();
        fieldContainer.add(new FieldLabel(group, I18n.get(SBFBrowserStr.labelFilterSaveGroup)));
        fieldContainer.add(new FieldLabel(name, I18n.get(SBFBrowserStr.labelFilterSaveName) + "*"));
        fieldContainer.add(status);
        fieldContainer.getElement().getStyle().setBackgroundColor(functionalContainer.getElement().getStyle().getBackgroundColor());
        fieldContainer.getElement().getStyle().setMargin(10, Style.Unit.PX);

        final SimplePageFormContainer pageFormContainer = new SimplePageFormContainer(70, false);
        pageFormContainer.addFieldSet(fieldContainer);
        pageFormContainer.updateLabels();
        functionalContainer.setScrollMode(ScrollSupport.ScrollMode.NONE);
        functionalContainer.add(pageFormContainer, VLC.FILL);
    }

    @Override
    protected boolean validate() {
        String n = name.getCurrentValue();
        if (n == null || n.trim().isEmpty()) {
            StringBuilder msg = new StringBuilder();
            msg.append("<span style=\"color:red;\">").append(I18n.get(SBFFormStr.msgNotFilledFields)).append("</span>");
            status.setHTML(msg.toString());
            return false;
        }else{
            status.setText("");
        }
        return super.validate();
    }

    private Group findGroup(String code) {
        return group.getStore().findModelWithKey(code);
    }

    public void show(GridContext context, StoredFilterPath path) {
        if(path != null){
            group.setValue(findGroup(path.getIdentityName()));
            name.setValue(path.getFilterName());
        }else{
            group.setValue(null);
            name.setValue(null);
        }
        super.show();
    }

}
