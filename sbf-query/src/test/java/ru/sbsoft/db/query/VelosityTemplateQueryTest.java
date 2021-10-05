package ru.sbsoft.db.query;

import ru.sbsoft.db.query.VelosityTemplateQuery;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author panarin
 */
public class VelosityTemplateQueryTest {

    public final static String PARAM_tableUQ = "tableUQ";
    public final static String PARAM_order = "order";
    public final static String SQL_GET_FIELDS =
            "   SELECT RECORD_UQ               \n"
            + "        , FIELD_ID                \n"
            + "     FROM PRODD.PD_FIELD          \n"
            + "    WHERE 1=1 \n";
    public final static String SQL_GET_DYNAMIC_FIELDS_CLAUSE =
            " #if ($tableUQ) AND RECORD_UQ_TABLE     = :tableUQ \n #end ";
    public final static String SQL_GET_STATIC_FIELDS_CLAUSE =
            " AND RECORD_UQ_TABLE     = :" + PARAM_tableUQ + " \n"
            + " AND FIELD_ORDER     = :" + PARAM_order + "\n"
            + " AND RECORD_UQ      = :" + PARAM_tableUQ;
    public final static String EXPECTED_SQL_GET_STATIC_FIELDS_CLAUSE =
            " AND RECORD_UQ_TABLE     = ? \n"
            + " AND FIELD_ORDER     = ?\n"
            + " AND RECORD_UQ      = ?";

    /**
     * Test of getQueryString method, of class VelosityTemplateQuery.
     */
    @Test
    public void testGetQueryString() throws Exception {
        System.out.println("getQueryString");
        final VelosityTemplateQuery instance =
                new VelosityTemplateQuery(SQL_GET_FIELDS + SQL_GET_DYNAMIC_FIELDS_CLAUSE, null);
        final String expResult = SQL_GET_FIELDS + "  ";
        final String result = instance.getQueryString(true);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetNativeQueryString() throws Exception {
        System.out.println("getNativeQueryString");
        final VelosityTemplateQuery instance =
                new VelosityTemplateQuery(SQL_GET_FIELDS + SQL_GET_STATIC_FIELDS_CLAUSE, null);
        instance.addParam(PARAM_order, 1);
        instance.addParam(PARAM_tableUQ, 2L);
        final String expResult = SQL_GET_FIELDS + EXPECTED_SQL_GET_STATIC_FIELDS_CLAUSE;
        final String result = instance.getNativeQueryString(true);
        assertEquals(expResult, result);
        assertEquals(3, instance.getNativeParamKeys().size());
        assertEquals(PARAM_tableUQ, instance.getNativeParamKeys().get(0));
        assertEquals(PARAM_order, instance.getNativeParamKeys().get(1));
        assertEquals(PARAM_tableUQ, instance.getNativeParamKeys().get(2));
    }
}
