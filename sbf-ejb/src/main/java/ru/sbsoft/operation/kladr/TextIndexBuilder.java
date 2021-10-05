package ru.sbsoft.operation.kladr;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import ru.sbsoft.operation.ProgressCallback;
import ru.sbsoft.shared.kladr.KLADRItem;

/**
 * @author balandin
 * @since Feb 8, 2013 11:39:10 AM
 */
public class TextIndexBuilder {

    public static final String FIELD_REGION_ID = "REGION_ID";
    public static final String FIELD_AREA_ID = "AREA_ID";
    public static final String FIELD_CITY_ID = "CITY_ID";
    public static final String FIELD_SETTLEMENT_ID = "SETTLEMENT_ID";
    public static final String FIELD_STREET_ID = "STREET_ID";
    public static final String FIELD_ERRORS = "ERRORS";
    public static final String[] INTEGER_FIELDS = new String[]{FIELD_REGION_ID, FIELD_AREA_ID, FIELD_CITY_ID, FIELD_SETTLEMENT_ID, FIELD_STREET_ID, FIELD_ERRORS};
    //
    private static String FIELD_INDEX_KEY = "INDEX_KEY";
    private static String FIELD_ADDRESS = "ADDRESS";
    //
    private static final String REGIONS_SQL =
            " select REGION_CODE, PREFIX, NAME "
            + " from KD_DOMAIN where AREA_CODE = '000' and CITY_CODE = '000' and SETTLEMENT_CODE = '000' "
            + " order by REGION_CODE ";
    private static final String INSERT =
            " insert into KD_TMP (REGION_ID, AREA_ID, CITY_ID, SETTLEMENT_ID, STREET_ID, ERRORS, ADDRESS, INDEX_KEY, KEYLENGTH) \n";
    private static final String BASE_SQL =
            " select XXX.*, LENGTH(XXX.INDEX_KEY) from "
            + "  (select \n "
            + "  region.ID as REGION_ID, area.ID as AREA_ID, city.ID as CITY_ID, settlement.ID as SETTLEMENT_ID, street.ID as STREET_ID, \n "
            + "    case when region.ACTUAL_CODE != '00' then 1 else 0 end \n "
            + "  + case when area.ACTUAL_CODE != '00' then 2 else 0 end \n "
            + "  + case when city.ACTUAL_CODE != '00' then 4 else 0 end \n "
            + "  + case when settlement.ACTUAL_CODE != '00' then 8 else 0 end \n "
            + "  + case when street.ACTUAL_CODE != '00' then 16 else 0 end \n "
            + "  + case when city.STATUS != settlement.STATUS and city.STATUS != '0' and settlement.STATUS != '0' then 32 else 0 end \n "
            + "  + case when region.ID is null then 64 else 0 end \n "
            + "  as ERRORS, \n "
            //
            + "  ltrim(rtrim("
            + "  coalesce(street.POSTCODE, settlement.POSTCODE, city.POSTCODE, area.POSTCODE, region.POSTCODE) || ', ' || \n "
            + "  case when region.ID is null or coalesce(city.STATUS, '0') in ('2', '3') or coalesce(settlement.STATUS, '0') in ('2', '3') then '' else region.NAME || ' ' || region.PREFIX || ', ' end || \n "
            + "  case when area.ID is null or coalesce(city.STATUS, '0') in ('1', '2', '3') or coalesce(settlement.STATUS, '0') in ('1', '2', '3') then '' else area.NAME || ' ' || area.PREFIX || ', ' end || \n "
            + "  case when city.ID is null then '' else city.PREFIX || ' ' || city.NAME || ', ' end || \n "
            + "  case when settlement.ID is null then '' else settlement.PREFIX || ' ' || settlement.NAME || ', ' end || \n "
            + "  case when street.ID is null then '' else street.PREFIX || ' ' || street.NAME end \n "
            + "  , ', '), ', ') as ADDRESS, \n "
            //
            + "  lower(trim( \n "
            + "  case when region.ID is null or coalesce(city.STATUS, '0') in ('2', '3') or coalesce(settlement.STATUS, '0') in ('2', '3') then '' else region.NAME || ' ' end || \n "
            + "  case when area.ID is null or coalesce(city.STATUS, '0') in ('1', '2', '3') or coalesce(settlement.STATUS, '0') in ('1', '2', '3') then '' else area.NAME || ' ' end || \n "
            + "  case when city.ID is null then '' else city.NAME || ' ' end || \n "
            + "  case when settlement.ID is null then '' else settlement.NAME || ' ' end || \n "
            + "  case when street.ID is null then '' else street.NAME  || ' ' end \n "
            + "  )) as INDEX_KEY \n "
            //
            //             + "    from KD_DOMAIN where REGION_CODE = ? and (CITY_CODE != '000' or SETTLEMENT_CODE != '000') \n "
            //
            + "from ( \n "
            + "    select distinct REGION_CODE, AREA_CODE, CITY_CODE, SETTLEMENT_CODE \n "
            + "    from KD_DOMAIN where REGION_CODE = ? \n "
            + "    ) base \n"
            //
            + "left join (select 1 as part from dual union all select 0 as part from dual) part on 1 = 1 \n "
            //
            + "left join KD_STREET street on 1 = part.part \n "
            + "  and street.REGION_CODE = base.REGION_CODE \n "
            + "  and street.AREA_CODE = base.AREA_CODE \n "
            + "  and street.CITY_CODE = base.CITY_CODE \n "
            + "  and street.SETTLEMENT_CODE = base.SETTLEMENT_CODE \n "
            //
            + "left join KD_DOMAIN settlement on settlement.REGION_CODE = base.REGION_CODE \n "
            + "  and settlement.AREA_CODE = base.AREA_CODE \n "
            + "  and settlement.CITY_CODE = base.CITY_CODE \n "
            + "  and settlement.SETTLEMENT_CODE = base.SETTLEMENT_CODE and settlement.SETTLEMENT_CODE != '000' \n "
            //
            + "left join KD_DOMAIN city on city.REGION_CODE = base.REGION_CODE \n "
            + "  and city.AREA_CODE = base.AREA_CODE \n "
            + "  and city.CITY_CODE = base.CITY_CODE and city.CITY_CODE != '000' \n "
            + "  and city.SETTLEMENT_CODE = '000' \n "
            //
            + "left join KD_DOMAIN area on area.REGION_CODE = base.REGION_CODE \n "
            + "  and area.AREA_CODE = base.AREA_CODE and area.AREA_CODE != '000' \n "
            + "  and area.CITY_CODE = '000' and area.SETTLEMENT_CODE = '000' \n "
            //
            + "left join KD_DOMAIN region on region.REGION_CODE = base.REGION_CODE \n "
            + "  and region.AREA_CODE = '000' and region.CITY_CODE = '000' and region.SETTLEMENT_CODE = '000' \n "
            //
            + "where part.part = 0 or (part.part = 1 and street.ID is not null) \n "
            //
            + " ) XXX";
    //
    private static final String SELECT_COUNT_SQL = "select count(*) from KD_TMP";
    private static final String SELECT_SQL = "select * from KD_TMP where KEYLENGTH = ? order by INDEX_KEY";
    private final EntityManager entityManager;
    private final KLADRStore store;

    public TextIndexBuilder(EntityManager entityManager, KLADRStore store) {
        this.entityManager = entityManager;
        this.store = store;
    }

    public List<KLADRItem> getRegions() {
        ArrayList<KLADRItem> result = new ArrayList<KLADRItem>(100);
        HashSet<String> uniq = new HashSet<String>(100);
        for (Object[] record : (List<Object[]>) entityManager.createNativeQuery(store.wrap(REGIONS_SQL)).getResultList()) {
            String code = (String) record[0];
            if (!uniq.contains(code)) {
                uniq.add(code);

                final KLADRItem kladrItem = new KLADRItem((String) record[1], (String) record[2], null);
                kladrItem.setCode(code);
                result.add(kladrItem);
            }
        }
        return result;
    }

    public void prepare(String regionCode) throws SQLException {
        final Query q = entityManager.createNativeQuery(INSERT + store.wrap(BASE_SQL));
        q.setParameter(1, regionCode);
        q.executeUpdate();
    }

    public long total() throws ClassNotFoundException, SQLException, IOException {
        Object tmp = entityManager.createNativeQuery(SELECT_COUNT_SQL).getSingleResult();
        return ((Number) tmp).longValue();
    }

    public void build(File indexDirectory, ProgressCallback callback) throws ClassNotFoundException, SQLException, IOException, InterruptedException {
        Directory directory = FSDirectory.open(indexDirectory);
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);

        IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);
        writerConfig.setOpenMode(OpenMode.CREATE);
        writerConfig.setRAMBufferSizeMB(128);

        IndexWriter writer = new IndexWriter(directory, writerConfig);
        try {
            for (int blockIndex = 1; blockIndex <= 200; blockIndex++) {
                final Query query = entityManager.createNativeQuery(SELECT_SQL);
                query.setParameter(1, blockIndex);
                for (Object[] record : (List<Object[]>) query.getResultList()) {
                    Document document = new Document();
                    for (int i = 0; i < INTEGER_FIELDS.length; i++) {
                        final Integer aInt = getInt(record[i]);
                        final IntField intField = new IntField(INTEGER_FIELDS[i], aInt, Field.Store.YES);
                        document.add(intField);
                    }
                    document.add(new StringField(FIELD_ADDRESS, (String) record[6], Field.Store.YES));
                    document.add(new TextField(FIELD_INDEX_KEY, (String) record[7], Field.Store.NO));
                    writer.addDocument(document);
                    callback.work(writer.numDocs());
                }
            }
        } finally {
            writer.close(true);
        }
    }

    private static Integer getInt(Object val) {
        return val == null ? 0 : ((Number) val).intValue();
    }
}
