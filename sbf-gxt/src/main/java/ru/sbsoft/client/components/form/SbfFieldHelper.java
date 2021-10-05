package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import java.util.Arrays;
import java.util.List;
import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.common.Strings;
import ru.sbsoft.sbf.gxt.utils.FieldUtils;

/**
 *
 * @author Kiselev
 */
public final class SbfFieldHelper {

    private SbfFieldHelper() {
    }
    
    public static void updateLabelsWidthAndAlign(int labelWidth, FieldLabel... labels){
        updateLabelWidth(labelWidth, labels);
        updateLabelsAlign(labels);
    }
    
    public static void updateLabelsWidth(HasWidgets container, int labelWidth) {
        updateLabelWidth(FormPanelHelper.getFieldLabels(container), labelWidth);
    }

    public static void updateLabelWidth(int labelWidth, FieldLabel... labels){
        if(labels != null && labels.length > 0){
            updateLabelWidth(Arrays.asList(labels), labelWidth);
        }
    }
    
    public static void updateLabelWidth(List<FieldLabel> labels, int labelWidth){
        if (labelWidth < 0) {
            return;
        }
        for (FieldLabel label : labels) {
            label.setLabelWidth(labelWidth);
        }
     }
    
    public static void updateLabelsAlign(HasWidgets container) {
        updateLabelsAlign(FormPanelHelper.getFieldLabels(container));
    }
    
    public static void updateLabelsAlign(FieldLabel... labels) {
        if(labels != null && labels.length > 0){
            updateLabelsAlign(Arrays.asList(labels));
        }
    }
    
    public static void updateLabelsAlign(List<FieldLabel> labels) {
        for (FieldLabel label : labels) {
            if (label.getText().equals(label.getLabelSeparator())) {
                label.setLabelSeparator(Strings.EMPTY);
                continue;
            }
            label.getElement().getFirstChildElement().getStyle().setProperty("textAlign", "right");
            if (!isAllowBlank(label.getWidget())) {
                String title = label.getText();
                if (title != null && !title.contains("*")) {
                    final String separator = label.getLabelSeparator();
                    if (separator != null && title.endsWith(separator)) {
                        title = title.substring(0, title.length() - separator.length());
                    }
                    label.setText(title + "*");
                }
            }
        }
    }

    private static boolean isAllowBlank(Widget widget) {
        if (widget instanceof AllowBlankControl) {
            return ((AllowBlankControl) widget).isAllowBlank();
        } else if (widget instanceof Field) {
            return !FieldUtils.hasEmptyValidator((Field) widget);
        }
        return true;
    }

}
