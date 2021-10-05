package ru.sbsoft.client.model;

import com.sencha.gxt.data.shared.ModelKeyProvider;
import ru.sbsoft.shared.model.MarkModel;

/**
 * Обеспечивает доступ к идентификатору в базовой модели данных для браузера.
 */
public class MarkProperties {

    public ModelKeyProvider<MarkModel> key() {
        return new ModelKeyProvider<MarkModel>() {
            public String getKey(MarkModel item) {
                return item.getRECORD_ID().toString();
            }
        };
    }
}
