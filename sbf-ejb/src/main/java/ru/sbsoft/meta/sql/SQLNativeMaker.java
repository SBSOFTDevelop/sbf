/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sbsoft.meta.sql;

import javax.persistence.TemporalType;
import org.slf4j.LoggerFactory;
import ru.sbsoft.common.Strings;
import ru.sbsoft.common.jdbc.QueryContext;
import ru.sbsoft.common.jdbc.QueryParam;
import ru.sbsoft.common.jdbc.QueryParamImpl;
import ru.sbsoft.common.DBType;

/**
 * Формирует кадр пагинации в зависимости от sql диалекта сервера БД
 *
 * @author sychugin
 */
public class SQLNativeMaker {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SQLNativeMaker.class);
    

   
    public String getFrame(final String selectClause, final String sortClause) {

        switch (DBType.getCurrentType()) {
            case DBTYPE_POSTGRES:
                return new StringBuilder()
                        .append("SELECT yyy.* FROM (")
                        .append(selectClause)
                        .append(sortClause)
                        .append(") yyy\n")
                        .append("limit :rowLimit offset :rowOffset")
                        .toString();
            //for MSSQL 2011 and hi 
            /*
             select * from ass.ASS_CD_ASSEMBLY where price>100
             --order by (select 1)
             order by NAME_VALUE,CODE_VALUE desc
             offset @RowSkip Row
             fetch  Next @RowFetch rows Only;
                    
             */
            case DBTYPE_MSSQL:

                final String res;
                final String sortFrame = sortClause.equals(Strings.EMPTY) ? "order by (select 1)\n"
                        : sortClause;

                res = new StringBuilder()
                        .append(selectClause)
                        .append(sortFrame)
                        .append("offset :rowOffset Row fetch Next :rowLimit rows Only")
                        .toString();
                LOGGER.info(res);

                return res;
            case DBTYPE_ORACLE:

            default: //"oracle"
                return new StringBuilder()
                        .append("SELECT yyy.* FROM (SELECT xxx.*, ROWNUM rnum FROM (")
                        .append(selectClause)
                        .append(sortClause)
                        .append(") xxx) yyy\n")
                        .append("WHERE rnum > :rowOffset and rnum <= :rowLimit")
                        .toString();
        }

    }

    public void prepare(QueryContext context) {

        if (DBType.getCurrentType() == DBType.DBTYPE_MSSQL) {
            context.put("MS_SQL", new QueryParamImpl(Boolean.TRUE));

        }

        context.put("NVL", new QueryParam() {

            @Override
            public Object getValue() {
                return (DBType.getCurrentType() == DBType.DBTYPE_MSSQL || (DBType.getCurrentType() == DBType.DBTYPE_POSTGRES)) ? "coalesce" : "nvl";
            }

            @Override
            public TemporalType getTemporalType() {
                return null;
            }
        });

        context.put("concat", new QueryParam() {

            @Override
            public Object getValue() {
                return DBType.getCurrentType() == DBType.DBTYPE_MSSQL ? "+" : "||";
            }

            @Override
            public TemporalType getTemporalType() {
                return null;
            }
        });

    }
}
