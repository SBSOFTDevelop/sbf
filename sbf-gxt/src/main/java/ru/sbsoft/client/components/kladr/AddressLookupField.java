package ru.sbsoft.client.components.kladr;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.kladr.SearchModel;
import ru.sbsoft.shared.services.IKLADRServiceAsync;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Поле выбора адреса в {@link AddressEditForm}
 * @author balandin
 * @since Mar 15, 2013 7:10:32 PM
 */
public class AddressLookupField extends ComboBox<SearchModel> {

    private static final IKLADRServiceAsync RPC = SBFConst.KLADR_SERVICE;
    private static final int MIN_CHARS = 3;

    private static class LookupCell extends ComboBoxCell<SearchModel> {

        private SearchCallback callback = new SearchCallback();
        private String oldquery;
        private boolean onlyActual = false;

        private class SearchCallback extends DefaultAsyncCallback<List<SearchModel>> {

            @Override
            public void onResult(List<SearchModel> result) {
                store.replaceAll(result);
            }
        };

        public LookupCell(ListStore<SearchModel> store, LabelProvider<? super SearchModel> labelProvider, ListView<SearchModel, ?> listView) {
            super(store, labelProvider, listView);
        }

        @Override
        public void doQuery(Context context, XElement parent, ValueUpdater<SearchModel> updater, SearchModel value, String query, boolean force) {
            query = Strings.clean(query);
            if (Strings.equals(query, oldquery)) {
                return;
            }

            if (query == null) {
                getStore().clear();
            } else if (query.length() >= MIN_CHARS) {
                query = query.toLowerCase();
                oldquery = query;
                RPC.search(query, onlyActual, callback);
            }
        }

        @Override
        protected void restrict(XElement parent) {
            try {
                super.restrict(parent);
            } catch (JavaScriptException ignore) {
            }
        }

        @Override
        public void finishEditing(Element parent, SearchModel value, Object key, ValueUpdater<SearchModel> valueUpdater) {
            super.finishEditing(parent, value, key, null);
        }

        public void setOnlyActual(boolean onlyActual) {
            this.onlyActual = onlyActual;
        }
    }

    public static AddressLookupField createInstance() {
        ListStore<SearchModel> store = new ListStore<SearchModel>(new ModelKeyProvider<SearchModel>() {

            @Override
            public String getKey(SearchModel item) {
                return String.valueOf(item.getScoreDoc());
            }
        });
        AbstractCell<SearchModel> cell = new AbstractCell<SearchModel>() {
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, SearchModel value, SafeHtmlBuilder sb) {
                if (value.getErrors() != 0) {
                    sb.appendHtmlConstant("<span style='color:#999'><i>");
                    sb.appendEscaped(value.getAddress());
                    sb.appendHtmlConstant("</i></span>");
                } else {
                    sb.appendEscaped(value.getAddress());
                }
            }
        };
        ListView<SearchModel, SearchModel> view = new ListView<SearchModel, SearchModel>(store, new ValueProvider<SearchModel, SearchModel>() {
            @Override
            public SearchModel getValue(SearchModel object) {
                return object;
            }

            @Override
            public void setValue(SearchModel object, SearchModel value) {
            }

            @Override
            public String getPath() {
                return null;
            }
        }, cell);
        return new AddressLookupField(store, new LabelProvider<SearchModel>() {

            @Override
            public String getLabel(SearchModel item) {
                return item.getAddress();
            }
        }, view);
    }

    public AddressLookupField(final ListStore<SearchModel> store, LabelProvider<? super SearchModel> labelProvider, final ListView<SearchModel, SearchModel> view) {
        super(new LookupCell(store, labelProvider, view));
        setTypeAhead(false);
        setQueryDelay(250);
        setEmptyText(I18n.get(SBFEditorStr.msgFirstLetterNames, String.valueOf(MIN_CHARS)));
    }

    public void setOnlyActual(boolean onlyActual) {
        ((LookupCell) getCell()).setOnlyActual(onlyActual);
    }
}
