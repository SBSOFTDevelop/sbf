package ru.sbsoft.client.components.kladr;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.widget.core.client.ListView;
import ru.sbsoft.svc.widget.core.client.form.ComboBox;
import ru.sbsoft.svc.widget.core.client.form.PropertyEditor;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import ru.sbsoft.svc.widget.core.client.form.Validator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.components.kladr.helper.KLADRComboBoxes;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.kladr.KLADRItem;
import ru.sbsoft.shared.services.IKLADRServiceAsync;

/**
 * Выпадающий список для выбора значения для одного из составляющих КЛАДР.
 * @author balandin
 * @since Mar 15, 2013 7:11:34 PM
 */
public class KLADRItemComboBox extends ComboBox<KLADRItem> {

    private static final IKLADRServiceAsync RPC = SBFConst.KLADR_SERVICE;
    private final KLADRComboBoxes locator;
    private final String defaultCode;
    private final KLADRItemComboBox.Handler handler = new KLADRItemComboBox.Handler();

    private class Handler implements SelectionHandler<KLADRItem>, ValueChangeHandler<KLADRItem> {

        @Override
        public void onSelection(SelectionEvent<KLADRItem> event) {
            handle((KLADRItemComboBox) event.getSource());
        }

        @Override
        public void onValueChange(ValueChangeEvent<KLADRItem> event) {
            handle((KLADRItemComboBox) event.getSource());
        }

        private void handle(final KLADRItemComboBox field) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    field.finishEditing();
                    field.validate();
                }
            });
            KLADRItemComboBox f = field;
            while ((f = locator.getNext(f)) != null) {
                final KLADRItem value = f.getValue();
                if (value != null) {
                    value.setUserInput(true);
                    f.validate();
                }
            }
        }
    }
    //
    private static final Validator<KLADRItem> validator = new Validator<KLADRItem>() {
        @Override
        public List<EditorError> validate(Editor<KLADRItem> editor, KLADRItem value) {
            final Style style = ((KLADRItemComboBox) editor).getInputEl().getStyle();
            if (value != null && value.isUserInput()) {
                style.setColor("#a00");
                style.setFontStyle(Style.FontStyle.ITALIC);
            } else {
                style.clearColor();
                style.clearFontStyle();
            }
            return null;
        }
    };

    public String getCode() {
        final KLADRItem v = getValue();
        if (v == null) {
            return defaultCode;
        } else if (v.isUserInput()) {
            return null;
        } else {
            return v.getCode();
        }
    }

    public String[] getContext() {
        final List<String> tmp = new ArrayList<String>();
        KLADRItemComboBox f = locator.getPrev(this);
        if (f != null) {
            tmp.add(f.getCode());
            while ((f = locator.getPrev(f)) != null) {
                tmp.add(f.getCode());
            }
        }
        Collections.reverse(tmp);
        return tmp.toArray(new String[tmp.size()]);
    }

    public void reinit() {
        ((KLADRItemComboBox.LookupCell) getCell()).reinit();
    }

    private static class LookupCell extends ComboBoxCell<KLADRItem> {

        private KLADRItemComboBox field;
        private static final String[] NOT_INIT = new String[]{"NOT_INIT"};
        private String[] oldRequest = NOT_INIT;

        private void reinit() {
            oldRequest = NOT_INIT;
        }

        public class KLADRPropertyEditor extends PropertyEditor<KLADRItem> {

            @Override
            public String render(KLADRItem object) {
                return getLabelProvider().getLabel(object);
            }

            @Override
            public KLADRItem parse(CharSequence text) throws ParseException {
                String s = (text == null) ? Strings.EMPTY : text.toString().trim();
                if (s.length() > 128) {
                    s = s.substring(0, 128);
                }
                KLADRItem item = selectByValue(s);
                if (item == null) {
                    item = KLADRItem.createStub(s);
                }
                return item;
            }
        }

        public LookupCell(ListStore<KLADRItem> store, LabelProvider<? super KLADRItem> labelProvider, ListView<KLADRItem, ?> listView) {
            super(store, labelProvider, listView);
            setPropertyEditor(new KLADRItemComboBox.LookupCell.KLADRPropertyEditor());
        }

        @Override
        public void doQuery(final Context context, final XElement parent, final ValueUpdater<KLADRItem> updater, final KLADRItem value, String query, boolean force) {
            String[] request = field.getContext();
            if (ClientUtils.equals(request, oldRequest)) {
                super.doQuery(context, parent, updater, value, query, force);
                return;
            }

            oldRequest = request;
            RPC.lookup(request, field.locator.actualStrict(), new DefaultAsyncCallback<List<KLADRItem>>() {
                @Override
                public void onResult(List<KLADRItem> result) {
                    field.init(result);
                }
            });
        }

        private void setField(KLADRItemComboBox field) {
            this.field = field;
        }

        @Override
        public void expand(Context context, XElement parent, ValueUpdater<KLADRItem> updater, KLADRItem value) {
            super.expand(context, parent, updater, value);
        }
    }

    public static KLADRItemComboBox create(KLADRComboBoxes locator, String defValue) {
        ListStore<KLADRItem> store = new ListStore<KLADRItem>(new ModelKeyProvider<KLADRItem>() {

            @Override
            public String getKey(KLADRItem item) {
                return item.getCode();
            }
        });
        AbstractCell<KLADRItem> cell = new AbstractCell<KLADRItem>() {
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, KLADRItem value, SafeHtmlBuilder sb) {
                if (value.isActual()) {
                    sb.appendEscaped(value.toString());
                } else {
                    sb.appendHtmlConstant("<span style='color:#999'><i>");
                    sb.appendEscaped(value.toString());
                    sb.appendHtmlConstant("</i></span>");
                }
            }
        };
        ListView<KLADRItem, KLADRItem> view = new ListView<KLADRItem, KLADRItem>(store, new ValueProvider<KLADRItem, KLADRItem>() {
            @Override
            public KLADRItem getValue(KLADRItem object) {
                return object;
            }

            @Override
            public void setValue(KLADRItem object, KLADRItem value) {
            }

            @Override
            public String getPath() {
                return null;
            }
        }, cell);
        return new KLADRItemComboBox(store, new LabelProvider<KLADRItem>() {

            @Override
            public String getLabel(KLADRItem item) {
                return item.getFullName();
            }
        }, view, locator, defValue);
    }

    public KLADRItemComboBox(ListStore<KLADRItem> store, LabelProvider<? super KLADRItem> labelProvider,
        ListView<KLADRItem, KLADRItem> view, KLADRComboBoxes locator, String defaultCODE) {

        super(new KLADRItemComboBox.LookupCell(store, labelProvider, view));
        ((KLADRItemComboBox.LookupCell) getCell()).setField(this);

        this.locator = locator;
        this.defaultCode = defaultCODE;

        setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        setTypeAhead(false);
        setQueryDelay(0);

        addSelectionHandler(handler);
        addValueChangeHandler(handler);
        //
        setAutoValidate(true);
        setValidationDelay(50);
        addValidator(validator);
    }

    public void init(String[] context, List<KLADRItem> result) {
        ((KLADRItemComboBox.LookupCell) getCell()).oldRequest = context;
        init(result);
    }

    public void init(List<KLADRItem> result) {
        getStore().clear();
        getStore().replaceAll(result);
    }

    public boolean selectByValue(String name) {
        name = Strings.clean(name, true);
        if (name == null) {
            return false;
        }

        KLADRItem uniq = null;
        for (KLADRItem item : getStore().getAll()) {
            if (item.checkAddress(name)) {
                if (uniq != null) {
                    return false;
                }
                uniq = item;
            }
        }

        if (uniq != null) {
            setValue(uniq, false, true);
            return true;
        }

        return false;
    }

    public boolean selectByCode(String code) {
        code = Strings.clean(code, true);
        if (code == null) {
            return false;
        }

        if (isZeros(code) && Integer.valueOf(code) == 0) {
            setValue(null, false, true);
            return true;
        }

        for (KLADRItem item : getStore().getAll()) {
            if (Strings.equals(item.getCode(), code)) {
                setValue(item, false, true);
                return true;
            }
        }

        return false;
    }
    
    private static boolean isZeros(String code) {
        if (Strings.clean(code) == null) {
            return false;
        }
        final char[] chars = code.toCharArray();
        for (char c : chars) {
            if (c != '0') {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setValue(KLADRItem value, boolean fireEvents, boolean redraw) {
        super.setValue(value, fireEvents, redraw);
        TextField codeField = (TextField) getData("c");
        if (codeField != null) {
            codeField.setValue((value == null) ? defaultCode : value.getCode());
        }
    }
}
