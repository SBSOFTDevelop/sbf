package ru.sbsoft.client.model;

import ru.sbsoft.svc.data.shared.ModelKeyProvider;
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
