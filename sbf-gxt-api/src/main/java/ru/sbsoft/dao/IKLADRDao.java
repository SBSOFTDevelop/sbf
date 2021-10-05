package ru.sbsoft.dao;

import java.util.List;
import ru.sbsoft.shared.kladr.KLADRAddressDict;
import ru.sbsoft.shared.kladr.KLADRItem;
import ru.sbsoft.shared.kladr.SearchModel;

/**
 * Интерфейс для КЛАДР, реализуется классом {@link ru.sbsoft.system.dao.common.KLADRDaoBean}.
 * @author balandin
 * @since Mar 15, 2013 12:56:38 PM
 */
public interface IKLADRDao {

    /**
     * 
     * Поиск адресов по выражению
     * 
     * @param query
     * @param actualStrict
     * @return 
     */
    public List<SearchModel> search(String query, boolean actualStrict);

    /**
     * 
     * Поиск адреса
     * 
     * @param scoreDoc
     * @return 
     */
    public KLADRAddressDict address(int scoreDoc);

    /**
     * 
     * Выборка территориальных подразделений дла комбобоксов
     * 
     * @param codes
     * @param actualStrict
     * @return 
     */
    public List<KLADRItem> lookup(String[] codes, boolean actualStrict);

    /**
     * 
     * Уточнение почтового индекса
     * 
     * @param codes
     * @param house
     * @param building
     * @return 
     */
    public String postcode(String[] codes, String house, String building);
}
