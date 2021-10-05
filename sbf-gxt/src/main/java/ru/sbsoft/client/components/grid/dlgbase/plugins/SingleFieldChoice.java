package ru.sbsoft.client.components.grid.dlgbase.plugins;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.browser.filter.FieldsComboBox;
import ru.sbsoft.client.components.browser.filter.fields.CustomHolder;
import ru.sbsoft.client.components.browser.filter.fields.GroupHolder;
import ru.sbsoft.client.components.browser.filter.fields.ItemHolder;
import ru.sbsoft.client.components.grid.dlgbase.Item;
import ru.sbsoft.client.components.grid.dlgbase.ItemPlugin;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitDeleteEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitDeleteListener;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.ComboBoxes;

/**
 *
 * @author Kiselev
 */
public class SingleFieldChoice implements ItemPlugin {

    private final List<BoxReg> boxes = new ArrayList<BoxReg>();
    private final Filter filter = new Filter();
    private final Unplugger unplugger = new Unplugger();

    @Override
    public void apply(Item item) {
        final FieldsComboBox fields = item.getFields();
        fields.getStore().setEnableFilters(true);
        fields.getStore().addFilter(filter);
        BoxReg boxReg = new BoxReg(fields);
        boxes.add(boxReg);
        item.addUnitDeleteListener(unplugger);
        boxReg.addHandlerReg(fields.addExpandHandler(new ExpandEvent.ExpandHandler() {
            @Override
            public void onExpand(ExpandEvent event) {
                ComboBoxes.applyFilters(fields);
            }
        }));
    }

    private ComboBox<CustomHolder> findBoxByStore(Store<CustomHolder> store) {
        for (BoxReg box : boxes) {
            Store<CustomHolder> boxStore = box.getBox().getStore();
            if (boxStore != null && boxStore.equals(store)) {
                return box.getBox();
            }
        }
        return null;
    }

    private class Filter implements Store.StoreFilter<CustomHolder> {

        private ComboBox<CustomHolder> storeBox = null;

        @Override
        public boolean select(Store<CustomHolder> store, CustomHolder parent, CustomHolder item) {
            storeBox = findBoxByStore(store);
            if (item != null) {
                if (isItem(item)) {
                    return isSelectable((ItemHolder) item);
                } else if (isGroup(item)) {
                    return isSelectable((GroupHolder) item);
                }
            }
            return false;
        }

        private boolean isSelectable(GroupHolder h) {
            for (CustomHolder hh : h.getChilds()) {
                if (isItem(hh)) {
                    if (isSelectable((ItemHolder) hh)) {
                        return true;
                    }
                } else if (isGroup(hh)) {
                    if (isSelectable((GroupHolder) hh)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean isSelectable(ItemHolder h) {
            for (BoxReg box : boxes) {
                if (box.getBox() != storeBox && box.getBox().getValue() == h) {
                    return false;
                }
            }
            return true;
        }

        private boolean isItem(CustomHolder h) {
            return h != null ? ClientUtils.isAssignableFrom(ItemHolder.class, h.getClass()) : false;
        }

        private boolean isGroup(CustomHolder h) {
            return h != null ? ClientUtils.isAssignableFrom(GroupHolder.class, h.getClass()) : false;
        }

    }

    private class Unplugger implements UnitDeleteListener {

        @Override
        public void onUnitDelete(UnitDeleteEvent e) {
            Item item = (Item) e.getUnit();
            FieldsComboBox fields = item.getFields();
            fields.getStore().removeFilter(filter);
            remove(fields);
            item.removeUnitDeleteListener(this);
        }

        private void remove(FieldsComboBox fields) {
            if (fields != null) {
                for (int i = 0; i < boxes.size(); i++) {
                    BoxReg r = boxes.get(i);
                    if (fields.equals(r.getBox())) {
                        r.unregisterHandlers();
                        boxes.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private class BoxReg {

        private final ComboBox<CustomHolder> box;
        private final List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

        public BoxReg(ComboBox<CustomHolder> box) {
            this.box = box;
        }

        public ComboBox<CustomHolder> getBox() {
            return box;
        }

        public void addHandlerReg(HandlerRegistration reg) {
            handlers.add(reg);
        }

        public void unregisterHandlers() {
            for (HandlerRegistration r : handlers) {
                r.removeHandler();
            }
        }
    }
}
