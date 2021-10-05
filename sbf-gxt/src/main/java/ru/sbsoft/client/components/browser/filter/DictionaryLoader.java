package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.dialog.ErrorDialog;
import ru.sbsoft.client.components.form.LookupItem;
import ru.sbsoft.client.components.form.LookupItemsList;
import ru.sbsoft.client.components.grid.SystemGrid;
import ru.sbsoft.shared.exceptions.ThrowableWrapper;
import ru.sbsoft.shared.meta.Column;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.services.FetchResult;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.meta.CodebaseField;
import ru.sbsoft.shared.meta.LookupGridInfo;

/**
 * Загрузка справочников, последовательная
 *
 * @author balandin
 * @since Jul 6, 2015
 */
public class DictionaryLoader {

    private final Map<Column, LookupItemsList> loadedDictionaries = new HashMap<Column, LookupItemsList>();
    private final Queue<DefaultLoader> queue = new LinkedList<DefaultLoader>();
    private final ComponentProvider maskComponentProvider;
    private final SystemGrid grid;
    private boolean loadFree = true;

    public DictionaryLoader(ComponentProvider maskComponentProvider, SystemGrid grid) {
        this.maskComponentProvider = maskComponentProvider;
        this.grid = grid;
    }

    public void load(Column column, LookupSuccessHandler handler, String columnName) {
        load(column, handler, new DistinctValuesLoader(handler, column, columnName));
    }

    public void load(Column column, LookupSuccessHandler handler, LookupGridInfo lookupInfo) {
        load(column, handler, new CodebaseDictionaryLoader(handler, column, lookupInfo));
    }

    private void load(Column column, LookupSuccessHandler handler, DefaultLoader l) {
        if (!checkCache(column, handler)) {
            if (loadFree) {
                load(l);
            } else {
                queue.add(l);
            }
        }
    }

    private void complete(Column column, LookupSuccessHandler handler, LookupItemsList list) {
        loadedDictionaries.put(column, list);
        loadFree = true;
        handler.onLookupLoad(list);
        checkUnmask();
        loadNext();
    }

    private void loadNext() {
        load(queue.poll());
    }

    private void load(DefaultLoader loader) {
        if (loader != null) {
            if (!checkCache(loader.getColumn(), loader.getSuccessHandler())) {
                loadFree = false;
                loader.load();
            } else {
                loadNext();
            }
        } else {
            checkUnmask();
        }
    }

    public boolean checkCache(Column column, LookupSuccessHandler handler) {
        LookupItemsList list = loadedDictionaries.get(column);
        if (list != null) {
            handler.onLookupLoad(list);
            checkUnmask();
            return true;
        }
        return false;
    }

    private void checkUnmask() {
        if (queue.isEmpty() && loadFree) {
            maskComponentProvider.getComponent().unmask();
        }
    }

    /**
     * Базовый загрузчик
     */
    public abstract class DefaultLoader implements AsyncCallback<FetchResult<MarkModel>> {

        protected final LookupSuccessHandler successHandler;
        protected final Column column;
        protected final String loadMessage;

        public DefaultLoader(LookupSuccessHandler successHandler, Column column, String loadMessage) {
            this.column = column;
            this.successHandler = successHandler;
            this.loadMessage = loadMessage;
        }

        public LookupSuccessHandler getSuccessHandler() {
            return successHandler;
        }

        public Column getColumn() {
            return column;
        }

        public final void load() {
            maskComponentProvider.getComponent().mask(loadMessage);

            Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {

                @Override
                public boolean execute() {
                    onLoad();
                    return false;
                }
            }, 100);
        }

        protected abstract void onLoad();

        @Override
        public void onFailure(Throwable caught) {
            new ErrorDialog().show(new ThrowableWrapper(caught));
        }

        @Override
        public void onSuccess(FetchResult<MarkModel> result) {
            complete(column, successHandler, fill(new LookupItemsList(), result.getData()));
        }

        protected abstract LookupItemsList fill(LookupItemsList list, List<MarkModel> data);
    }

    /**
     * Загрузка уникальных значений колонки
     */
    private class DistinctValuesLoader extends DefaultLoader {

        private final String columnName;

        public DistinctValuesLoader(LookupSuccessHandler handler, Column column, String columnName) {
            super(handler, column, I18n.get(SBFBrowserStr.msgAnalysisValues));
            this.columnName = columnName;
        }

        @Override
        public void onLoad() {
            grid.loadData(columnName, this);
        }

        @Override
        protected LookupItemsList fill(LookupItemsList list, List<MarkModel> data) {
            for (MarkModel model : data) {
                Row r = (Row) model;
                Object v = r.getValues().get(0);
                if (v != null) {
                    list.add(new LookupItem(v, String.valueOf(v)));
                }
            }
            return list;
        }
    }

    /**
     * Загрузка Codebase справочника
     */
    private class CodebaseDictionaryLoader extends DefaultLoader {

        private final LookupGridInfo lookupInfo;

        public CodebaseDictionaryLoader(LookupSuccessHandler handler, Column column, LookupGridInfo gridType) {
            super(handler, column, I18n.get(SBFBrowserStr.msgDownloadHandbook));
            this.lookupInfo = gridType;
        }

        @Override
        public void onLoad() {
            grid.loadData(lookupInfo.getGridType(), this);
        }

        @Override
        protected LookupItemsList fill(LookupItemsList list, List<MarkModel> data) {
            for (MarkModel model : data) {
                Row r = (Row) model;
                String n = r.getString(CodebaseField.NAME_VALUE.getCode());
                String c = r.getString(CodebaseField.CODE_VALUE.getCode());
                String a1;
                switch (lookupInfo.getKeyType()) {
                    case KEY_CODE:
                        a1 = c;
                        break;
                    case KEY_NAME:
                        a1 = n;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown LookupKeyType: " + lookupInfo.getKeyType());
                }
                list.add(new LookupItem(a1, createLookupVal(c, n)));
            }
            return list;
        }

        private String createLookupVal(String key, String name) {
            if (name == null || (name = name.trim()).isEmpty()) {
                return key != null ? key.trim() : "";
            }
            if (key == null || (key = key.trim()).isEmpty()) {
                return name != null ? name.trim() : "";
            }
            if (key.equalsIgnoreCase(name)) {
                return name;
            }
            return new StringBuilder().append(key).append('/').append(name).toString();
        }
    }
}
