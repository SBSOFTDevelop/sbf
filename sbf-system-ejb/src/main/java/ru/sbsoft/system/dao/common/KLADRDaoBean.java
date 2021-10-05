package ru.sbsoft.system.dao.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.PrefixFilter;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import ru.sbsoft.common.Strings;
import ru.sbsoft.dao.IKLADRDao;
import ru.sbsoft.operation.kladr.KDHouseUtils;
import ru.sbsoft.operation.kladr.KLADRManager;
import ru.sbsoft.operation.kladr.KLADRStore;
import ru.sbsoft.shared.kladr.KLADRAddressDict;
import ru.sbsoft.shared.kladr.KLADRItem;
import ru.sbsoft.shared.kladr.SearchModel;

/**
 * Класс предоставляет DAO слой для работы с сущностями КЛАДР (<i>KD_DOMAIN, KD_STREET, KD_HOUSE</i>).
 * @author balandin
 * @since Mar 15, 2013 12:49:17 PM
 */
@Stateless
@Remote(IKLADRDao.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@PermitAll
public class KLADRDaoBean implements IKLADRDao {

    public static final String FIELD_REGION_ID = "REGION_ID";
    public static final String FIELD_AREA_ID = "AREA_ID";
    public static final String FIELD_CITY_ID = "CITY_ID";
    public static final String FIELD_SETTLEMENT_ID = "SETTLEMENT_ID";
    public static final String FIELD_STREET_ID = "STREET_ID";
    public static final String FIELD_ERRORS = "ERRORS";
    public static final String FIELD_INDEX_KEY = "INDEX_KEY";
    public static final String FIELD_ADDRESS = "ADDRESS";

    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager entityManager;

    public KLADRAddressDict getKladrAddress(KLADRStore store, int regionID, int areaID, int cityID, int settlementID, int streetID) {
        KLADRAddressDict address = new KLADRAddressDict();
        address.setRegion(findDomain(store, COLUMNS[0][0], regionID));
        address.setArea(findDomain(store, COLUMNS[1][0], areaID));
        address.setCity(findDomain(store, COLUMNS[2][0], cityID));
        address.setSettlement(findDomain(store, COLUMNS[3][0], settlementID));
        address.setStreet(findStreet(store, COLUMNS[4][0], streetID));
        return address;
    }

    private KLADRItem findDomain(KLADRStore store, String codeColumn, int domainID) {
        if (domainID <= 0) {
            return null;
        }
        final Query q = entityManager.createNativeQuery(getSelectSQL(store, codeColumn, "KD_DOMAIN") + " where ID = " + domainID);
        return read((Object[]) q.getResultList().get(0));
    }

    private KLADRItem findStreet(KLADRStore store, String codeColumn, int streetID) {
        if (streetID <= 0) {
            return null;
        }

        final Query q = entityManager.createNativeQuery(getSelectSQL(store, codeColumn, "KD_STREET") + " where ID = " + streetID);
        return read((Object[]) q.getResultList().get(0));
    }

    private List<KLADRItem> findHouses(KLADRStore store, String[] codes) {
        final List<KLADRItem> result = new ArrayList<KLADRItem>();
        for (Object[] v : (List<Object[]>) (entityManager.createNativeQuery(getSQL(store, codes, true, false))).getResultList()) {
            result.add(read(v));
        }
        return result;
    }

    @Override
    public List<KLADRItem> lookup(String[] codes, boolean actualStrict) {
        try {
            KLADRManager.get().getSearcher(entityManager);
        } catch (IOException ignore) {
        }
        final KLADRStore store = KLADRManager.get().getActiveStore();
        if (store == null) {
            return Arrays.asList(new KLADRItem("", "Ошибка сервера", ""));
        }

        final List<KLADRItem> result = new ArrayList<KLADRItem>();
        for (Object[] v : (List<Object[]>) (entityManager.createNativeQuery(getSQL(store, codes, false, actualStrict) + " order by NAME")).getResultList()) {
            result.add(read(v));
        }
        return result;
    }
    //
    private static String[][] COLUMNS = new String[][]{
        {"REGION_CODE", "00", "KD_DOMAIN"},
        {"AREA_CODE", "000", "KD_DOMAIN"},
        {"CITY_CODE", "000", "KD_DOMAIN"},
        {"SETTLEMENT_CODE", "000", "KD_DOMAIN"},
        {"STREET_CODE", "0000", "KD_STREET"},
        {"HOUSE_CODE", "0000", "KD_HOUSE"}
    };

    private String getSQL(KLADRStore store, String[] codes, boolean house, boolean onlyActual) {
        String column = COLUMNS[codes.length][0];
        String table = COLUMNS[codes.length][2];

        StringBuilder s = new StringBuilder();
        s.append(getSelectSQL(store, column, table));
        s.append(" where 1 = 1 ");
        int maxParamcount = house ? 5 : 4;
        for (int i = 0; i < maxParamcount; i++) {
            String[] CDEF = COLUMNS[i];
            s.append(" and ").append(CDEF[0]).append(CDEF[0].equals(column) ? " != " : " = ");
            s.append("'").append(Strings.coalesce(get(codes, i), CDEF[1])).append("' ");
        }
        if (onlyActual) {
            s.append(" and ACTUAL_CODE = '00'");
        }
        return s.toString();
    }

    private String get(String[] values, int index) {
        return index < values.length ? values[index] : null;
    }

    private String getSelectSQL(KLADRStore store, String column, String table) {
        boolean actualCode = !"KD_HOUSE".equals(table);
        boolean status = "KD_DOMAIN".equals(table);
        return " select PREFIX, NAME, POSTCODE, trim(" + column + ")"
                + ", " + (actualCode ? "trim(ACTUAL_CODE)" : "'00'")
                + ", " + (status ? "trim(STATUS)" : "'0'")
                + " from " + store.wrap(table);
    }

    private KLADRItem read(Object[] v) {
        KLADRItem item = new KLADRItem((String) v[0], (String) v[1], (String) v[2]);
        item.setCode((String) v[3]);
        item.setActual("00".equals(v[4]));
        item.setStatus((String) v[3]);
        return item;
    }

    @Override
    public String postcode(String[] codes, String house, String building) {
        final KLADRStore store = KLADRManager.get().getActiveStore();
        if (store == null) {
            return "Server error";
        }

        if (codes.length != 5 || Strings.isEmpty(house)) {
            return null;
        }
        List<KLADRItem> listHouse = findHouses(store, codes);
        for (KLADRItem item : listHouse) {
            if (KDHouseUtils.checkEqHouse(item.getName(), house, building)) {
                return item.getPostcode();
            }
        }
        for (KLADRItem item : listHouse) {
            if (KDHouseUtils.checkEnHouse(item.getName(), house)) {
                return item.getPostcode();
            }
        }
        return null;
    }

    @Override
    public List<SearchModel> search(String queryString, boolean actualStrict) {
        String[] vals = queryString.split("\\s");
        org.apache.lucene.search.Query query = new PrefixQuery(new Term(FIELD_INDEX_KEY, vals[0]));
        if (vals.length > 0) {
            for (int i = 1; i < vals.length; i++) {
                query = new FilteredQuery(query, new PrefixFilter(new Term(FIELD_INDEX_KEY, vals[i])));
            }
        }
        if (actualStrict) {
            query = new FilteredQuery(query, NumericRangeFilter.newIntRange(FIELD_ERRORS, 0, 0, true, true));
        }

        try {
            final IndexSearcher searcherInst = KLADRManager.get().getSearcher(entityManager);
            if (searcherInst == null) {
                return Arrays.asList(SearchModel.NOT_READY);
            }

            int RESULT_LENGTH = 10;

            TopDocs search = searcherInst.search(query, RESULT_LENGTH + 1);
            int docsCount = search.scoreDocs.length;
            int resultSize = Math.min(docsCount, RESULT_LENGTH);
            ArrayList<SearchModel> result = new ArrayList<SearchModel>();
            for (int i = 0; i < resultSize; i++) {
                ScoreDoc scoreDoc = search.scoreDocs[i];
                final SearchModel searchModel = new SearchModel(scoreDoc.doc, searcherInst.doc(scoreDoc.doc).get(FIELD_ADDRESS));
                searchModel.setErrors(Integer.parseInt(searcherInst.doc(scoreDoc.doc).get(FIELD_ERRORS)));
                result.add(searchModel);
            }
            if (docsCount > RESULT_LENGTH) {
                result.add(SearchModel.TERMINATOR);
            }
            return result;
        } catch (Exception ex) {
            return Arrays.asList(SearchModel.ERROR);
        }
    }

    @Override
    public KLADRAddressDict address(int scoreDoc) {
        try {

            final IndexSearcher searcherInst = KLADRManager.get().getSearcher();
            final KLADRStore store = KLADRManager.get().getActiveStore();
            if (searcherInst == null || store == null) {
                final KLADRAddressDict errRes = new KLADRAddressDict();
                errRes.setRegion(new KLADRItem("", "Server error", ""));
                return errRes;
            }

            Document doc = searcherInst.doc(scoreDoc);
            return getKladrAddress(
                    store,
                    parse(doc.getField(FIELD_REGION_ID).stringValue()),
                    parse(doc.getField(FIELD_AREA_ID).stringValue()),
                    parse(doc.getField(FIELD_CITY_ID).stringValue()),
                    parse(doc.getField(FIELD_SETTLEMENT_ID).stringValue()),
                    parse(doc.getField(FIELD_STREET_ID).stringValue()));
        } catch (Exception ex) {
            return null;
        }
    }

    private int parse(String value) {
        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value);
    }
}
