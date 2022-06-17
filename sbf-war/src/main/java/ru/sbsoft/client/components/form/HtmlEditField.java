package ru.sbsoft.client.components.form;

import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.SVCLogConfiguration;
import ru.sbsoft.svc.widget.core.client.form.HtmlEditor;
import ru.sbsoft.svc.widget.core.client.form.error.DefaultEditorError;
import java.util.Collections;
import java.util.logging.Logger;
import ru.sbsoft.client.I18n;

import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 *
 * @author sychugin
 */
public class HtmlEditField extends HtmlEditor implements AllowBlankControl, ReadOnlyControl {

    private boolean allwBlank = true;
    private static final Logger log = Logger.getLogger(HtmlEditField.class.getName());

    private String oldValue;

    public HtmlEditField() {

        oldValue = getValue();
        addValidator((Editor<String> editor, String value) -> {
            if (!allwBlank && getValue() == null) {
                return Collections.<EditorError>singletonList(new DefaultEditorError(editor, I18n.get(SBFGeneralStr.msgNeedFill), null));
            }

            clearInvalid();
            return null;
        });

        addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                GWTlog("value changed");
            }
        });
        this.textArea.addAttachHandler((event) -> {
            if (event.isAttached()) {
                setListener(HtmlEditField.this.textArea.getElement());
            }
        });
    }

    private static void GWTlog(String msg) {

        if (SVCLogConfiguration.loggingIsEnabled()) {
            log.finest(msg);
        }
    }

    private native void setListener(Element elem) /*-{
           var _this = this;
           var wnd=elem.contentWindow;
            
//           wnd.addEventListener('DOMSubtreeModified', 
//            function(Event){@ru.sbsoft.client.components.form.HtmlEditField::fireValueChanged(Lru/sbsoft/client/components/form/HtmlEditField;)(_this);}
//            ,            
//            false); 
   wnd.addEventListener('DOMSubtreeModified', 
            function(Event){_this.@ru.sbsoft.client.components.form.HtmlEditField::fireValueChanged()();}
            ,            
            false);
            
            
            
    }-*/;

    private boolean isMozilla() {

        return SVC.isGecko() || SVC.isGecko1_8() || SVC.isGecko1_9();
    }

    @Override
    public void setValue(String value) {

        if (isMozilla() && (value != null) && value.equals("<br>")) {
            value = null;
        }

        oldValue = value;
        super.setValue(value);
    }

    private void fireValueChanged() {
        if (textArea.isVisible() && textArea.isAttached() && textArea.isEnabled()) {

            if (isMozilla() && oldValue == null && textArea.getHTML() != null && textArea.getHTML().equals("<br>")) {

                return;

            }

            GWTlog("valueChange event");
            ValueChangeEvent.fireIfNotEqual(this, oldValue, getValue());
            oldValue = getValue();
        }
    }

    @Override
    public void setAllowBlank(boolean value) {
        allwBlank = value;
    }

    @Override
    public boolean isAllowBlank() {
        return allwBlank;
    }

    @Override
    public boolean isReadOnly() {
        return !isEnabled();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        setEnabled(!readOnly);
    }

}
