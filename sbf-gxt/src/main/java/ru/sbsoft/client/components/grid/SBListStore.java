package ru.sbsoft.client.components.grid;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import java.util.Collection;

/**
 * Буффер для сущностей представленных в строках таблицы на клиенте.
 * @author balandin
 * @since Mar 6, 2013 5:08:12 PM
 */
public class SBListStore<M> extends ListStore<M> {

    public SBListStore(ModelKeyProvider<? super M> keyProvider) {
        super(keyProvider);
    }

    @Override
    public boolean addAll(Collection<? extends M> items) {
        if (!items.isEmpty()) {
            super.addAll(items);
        }
        return true;
    }
}
