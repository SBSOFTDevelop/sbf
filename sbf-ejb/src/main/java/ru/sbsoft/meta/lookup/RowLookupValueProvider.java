package ru.sbsoft.meta.lookup;

import javax.persistence.EntityManager;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 * Класс представляет поставщика данных для {@link ru.sbsoft.shared.model.LookupInfoModel} на основе данных получаемых из экземпляров
 * {@link ru.sbsoft.shared.meta.Row@author balandin
 * @since Feb 24, 2015 12:48:26 PM
 */
public class RowLookupValueProvider extends LookupValueProvider {

    /**
     * Перегружаемый конструктор.
     * @param codeColumn имя колонки (поля таблицы), содержащей ключ (код lookup значения). 
     * @param nameColumn имя колонки (поля таблицы), содержащей наименование (расшифровку кода).
     */
    public RowLookupValueProvider(String codeColumn, String nameColumn) {
        this(codeColumn, nameColumn, null);
    }

    /**
     * Перегружаемый конструктор.
     * @param codeColumn имя колонки (поля таблицы), содержащей ключ (код lookup значения).
     * @param nameColumn имя колонки (поля таблицы), содержащей наименование (расшифровку кода).
     * @param semanticKeyColumn имя колонки (поля таблицы), содержащей семантический (смысловой) код, например код ОКВЭД.
     */
    public RowLookupValueProvider(String codeColumn, String nameColumn, String semanticKeyColumn) {
        super(codeColumn, nameColumn, semanticKeyColumn);
    }

    @Override
    public LookupInfoModel createLookupModel(Row row, EntityManager entityManager) {
        LookupInfoModel result = createLookupModelInstance();
        result.setID(row.getPrimaryKeyValue());
        if (semanticKeyColumn != null) {
            result.setSemanticID(row.getBigDecimal(semanticKeyColumn));
        }
        fillLookupModel(result, row);
        result.initialize(row);
        return result;
    }

    protected void fillLookupModel(LookupInfoModel model, Row row) {
        model.setSemanticKey(convertKey(row.getValue(codeColumn)));
        model.setSemanticName(convertName(row.getValue(nameColumn)));
    }
}
