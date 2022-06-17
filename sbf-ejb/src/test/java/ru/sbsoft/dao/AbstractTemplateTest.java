package ru.sbsoft.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.meta.columns.ColumnKind;
import ru.sbsoft.meta.columns.style.condition.CEq;
import ru.sbsoft.meta.context.DefaultGlobalQueryContextFactory;
import ru.sbsoft.model.ApplicationMenuModel;
import ru.sbsoft.model.IOperationSettings;
import ru.sbsoft.shared.grid.style.CStyle;
import ru.sbsoft.shared.grid.style.ColorConst;
import ru.sbsoft.shared.meta.IColumns;

/**
 *
 * @author Kiselev
 */
public class AbstractTemplateTest {

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        try (Connection dbc = getConnection()) {
            Statement st = dbc.createStatement();

            st.execute("CREATE TABLE test123 ( col1 VARCHAR(512), col2 INT)");
            st.execute("INSERT INTO test123(col1, col2) VALUES('test1', 1)");
            st.execute("INSERT INTO test123(col1, col2) VALUES('test2', 2)");
            st.execute("INSERT INTO test123(col1, col2) VALUES('test3', 3)");
            st.execute("INSERT INTO test123(col1, col2) VALUES('test4', 4)");
            st.execute("INSERT INTO test123(col1, col2) VALUES('test5', 5)");
        }
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DbTestConfig.getDriver());
        return DriverManager.getConnection(DbTestConfig.getUrl());
    }

    @Test
    public void testGridStyles() {
        TestTemplate templ = new TestTemplate();
        TemplateBuilder b = new TemplateBuilder(templ);
        IColumns c = b.getMeta();
        Assert.assertNotNull(c);
        Assert.assertEquals(c.getColumns().size(), templ.getColumnsInfo().getItems().size());
    }

    @After
    public void tearDown() throws ClassNotFoundException, SQLException {
        try (Connection dbc = getConnection()) {
            Statement st = dbc.createStatement();
            st.execute("DROP TABLE test123");
        }
    }

    private class TestTemplate extends AbstractTemplate {

        @Override
        public ColumnsInfo createColumns() {
            ColumnsInfo c = new ColumnsInfo();
            c.add(KEY, "col2");
            c.add(ColumnKind.VCHAR, 100, "Test", "col1", "col1").addStyle(new CStyle().setColor(ColorConst.Red), new CEq<>("col2"));
            return c;
        }

        @Override
        public void buildFromClause(StringBuilder sb) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    public class TemplateBuilder extends DefaultTemplateBuilder {

        public TemplateBuilder(AbstractTemplate template) {
            super(template, new ITemplateContext() {
                @Override
                public EntityManager getEntityManager() {
                    return SbsEntityManagerSingleton.getEM();
                }

                @Override
                public IJdbcWorkExecutor getJdbcWorkExecutor() {
                    return new IJdbcWorkExecutor() {
                        @Override
                        public <T> T executeJdbcWork(IJdbcWork<T> work) throws SQLException {
                            return work.execute(getEntityManager().unwrap(Connection.class));
                        }
                    };
                }

                @Override
                public SessionContext getSessionContext() {
                    return new SessionContextMock("test");
                }
            }, new DefaultGlobalQueryContextFactory(new IApplicationDao() {

                @Override
                public List<ApplicationMenuModel> getApplicationList(List<String> apps) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public String getAppCode() {
                    return "test";
                }

                @Override
                public IOperationSettings getOperationSettings() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            }));
        }
    }
}
