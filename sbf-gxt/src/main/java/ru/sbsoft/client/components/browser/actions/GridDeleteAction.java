package ru.sbsoft.client.components.browser.actions;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import static com.sencha.gxt.widget.core.client.box.MessageBox.ICONS;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Запускает операцию удаления выделенной записи (отмеченных записей) из таблицы.
 *
 * @author balandin
 * @since Oct 3, 2013 4:21:42 PM
 */
public class GridDeleteAction extends GridAction {

    public GridDeleteAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuOperRowDelete));
        setToolTip(I18n.get(SBFBrowserStr.hintOperRowDelete));
        setIcon16(SBFResources.BROWSER_ICONS.RowDelete16());
        setIcon24(SBFResources.BROWSER_ICONS.RowDelete());
    }

    @Override
    public boolean checkEnabled() {
        if (getGrid().isReadOnly(true)) {
            return false;
        }
        if (getGrid().isInitialized()) {
            if (getGrid().getTotalMarkedRecordsCount() > 0) {
                return true;
            }
            if (getGrid().getSelectedRecords().size() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onExecute() {
        Methods methods = new Methods();
        methods.add(I18n.get(SBFBrowserStr.msgDeleteCurrent), getGrid().getSelectedRecords().size());
        if (getGrid().isMultiplyDeleteSupported()) {
            methods.add(I18n.get(SBFBrowserStr.msgDeleteMarked), getGrid().getTotalMarkedRecordsCount());
        }
        // methods.add(ALL, getGrid().getTotalRecordsCount());

        if (methods.isEmpty()) {
            ClientUtils.message(I18n.get(SBFBrowserStr.msgDeleteData), I18n.get(SBFBrowserStr.msgDeleteNot));
            return;
        }

        final DeleteDialog dialog = new DeleteDialog(methods);
        dialog.show();
        dialog.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == PredefinedButton.OK) {
                    delete(dialog.getComboBox().getValue());
                }
            }
        });
    }

    private void delete(Method method) {
        if (method != null) {
            if (I18n.get(SBFBrowserStr.msgDeleteCurrent).equals(method.getName())) {
                final List<MarkModel> recs = getGrid().getSelectedRecords();
                for (MarkModel record : recs) {
                    getGrid().deleteRecord(record);
                }
            } else if (I18n.get(SBFBrowserStr.msgDeleteMarked).equals(method.getName())) {
                getGrid().mask(I18n.get(SBFBrowserStr.msgDelete));
                deleteMarkedRecords(getGrid().getMarkedRecords(), 0);
            } else if (I18n.get(SBFBrowserStr.msgDeleteAll).equals(method.getName())) {
//				final List<BigDecimal> recs = getGrid().getMarkedRecords();
            }
        }
    }

    private void deleteMarkedRecords(final ArrayList<BigDecimal> listIDs, final int counter) {
        getGrid().mask(I18n.get(SBFBrowserStr.msgDelete) + " " + String.valueOf(counter));
        getGrid().loadSingleRow(listIDs.remove(0), new AsyncCallback<MarkModel>() {

            @Override
            public void onFailure(Throwable caught) {
                next();
            }

            @Override
            public void onSuccess(MarkModel result) {
                getGrid().deleteRecord(result, new DefaultAsyncCallback() {

                    @Override
                    public void onResult(Object result) {
                        next();
                    }
                });
            }

            private void next() {
                if (listIDs.isEmpty()) {
                    getGrid().unmask();
                    getGrid().deleteAllMarks();
                    getGrid().tryReload();
                    return;
                }
                deleteMarkedRecords(listIDs, counter + 1);
            }
        });
    }

    private static class Methods extends ArrayList<Method> {

        public void add(String name, int count) {
            if (count > 0) {
                add(new Method(name, count));
            }
        }
    }

    private static class Method {

        private final int count;
        private final String name;

        public Method(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public String getName() {
            return name;
        }

        public String getLabel() {
            return name + " (" + count + ")";
        }
    }

    private static class DeleteDialog extends Dialog {

        private final HorizontalLayoutContainer mainContainer = new HorizontalLayoutContainer();
        private final VerticalLayoutContainer content = new VerticalLayoutContainer();
        private final SimpleComboBox<Method> comboBox = new SimpleComboBox<Method>(new LabelProvider<Method>() {

            @Override
            public String getLabel(Method item) {
                return item.getLabel();
            }
        });

        public DeleteDialog(Methods methods) {
            setClosable(false);
            setPixelSize(350, 150);
            //setPixelSize(-1, -1);
            //setMinHeight(0);
            //setMinWidth(0);
            //setWidth(350);
            setResizable(false);
            setModal(true);
            setButtonAlign(BoxLayoutContainer.BoxLayoutPack.CENTER);
            setHeading(I18n.get(SBFBrowserStr.msgDeleteData));
            setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
            setHideOnButtonClick(true);

            comboBox.add(methods);
            comboBox.setForceSelection(true);
            comboBox.setTypeAhead(true);
            comboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
            comboBox.setEditable(false);
            comboBox.setValue(comboBox.getStore().get(0));

            content.add(new HTML(I18n.get(SBFBrowserStr.msgSelectDesire) + ":"), new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));
            content.add(comboBox, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(5, 10, 10, 10)));

            mainContainer.add(new Image(ICONS.question()), new HorizontalLayoutContainer.HorizontalLayoutData(-1, -1, new Margins(10, 5, 5, 5)));
            mainContainer.add(content, new HorizontalLayoutContainer.HorizontalLayoutData(1, 1, new Margins(5)));

            ((TextButton) buttonBar.getWidget(0)).setText(I18n.get(SBFBrowserStr.msgToDelete));

            setWidget(mainContainer);
        }

        public SimpleComboBox<Method> getComboBox() {
            return comboBox;
        }
    }
}
