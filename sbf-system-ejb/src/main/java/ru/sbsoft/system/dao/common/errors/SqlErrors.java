package ru.sbsoft.system.dao.common.errors;

import ru.sbsoft.model.SQLRetCode;
import ru.sbsoft.dao.errors.ISqlErrors;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ru.sbsoft.common.DBType;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.system.dao.common.DatabaseInfoHelper;

/**
 * Инструменты для замены ошибок SQL на читаемые.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
@Remote(ISqlErrors.class)
@Singleton
@LocalBean

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)

@PermitAll
public class SqlErrors implements ISqlErrors {

    //private static final Logger LOGGER = LoggerFactory.getLogger(SqlErrors.class);
    private Map<SQLRetCode, DBError> CODE_MESSAGES;

    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager em;

    /*static {

// --------
        //DEFAULT_CODE_MESSAGES = Collections.unmodifiableMap(defCodeMsg);
        DBType.setCurrentType(DatabaseInfoHelper.getDatabaseName());
        CODE_MESSAGES = new HashMap<>();
        switch (DBType.getCurrentType()) {

            case DBTYPE_MSSQL:
                // oracle

                CODE_MESSAGES.put(new SQLRetCode("23000", 2627), DBError.ERR_CODE_IS_USE);
                CODE_MESSAGES.put(new SQLRetCode("23000", 547), DBError.ERR_FOUND_DEPENDING);
                break;

            case DBTYPE_POSTGRES:
                // oracle

                CODE_MESSAGES.put(new SQLRetCode("23503", 0), DBError.ERR_FOUND_DEPENDING);
                CODE_MESSAGES.put(new SQLRetCode("23505", 0), DBError.ERR_CODE_IS_USE);
                break;

            case DBTYPE_DB2:

                CODE_MESSAGES.put(new SQLRetCode("23504", -532), DBError.ERR_FOUND_DEPENDING);
                CODE_MESSAGES.put(new SQLRetCode("23505", -803), DBError.ERR_CODE_IS_USE);
                break;

            case DBTYPE_UNKNOWN:
            case DBTYPE_ORACLE:
            // oracle

            default:
                CODE_MESSAGES.put(new SQLRetCode("23000", 1), DBError.ERR_CODE_IS_USE);
                CODE_MESSAGES.put(new SQLRetCode("23000", 2292), DBError.ERR_FOUND_DEPENDING);

        }

    }
     */
    //private static final DBType dbType;
    private ICodeBasedExceptionProcessor processor = new CodeBasedExceptionProcessor();

    @PostConstruct
    private void init() {
        DBType.setCurrentType(DatabaseInfoHelper.getDatabaseName(em));

        if (DBType.getCurrentType() == null) {
            throw new ApplicationException("No dataBase assigned");
        }

        CODE_MESSAGES = new HashMap<>();
        switch (DBType.getCurrentType()) {

            case DBTYPE_MSSQL:
                // oracle

                CODE_MESSAGES.put(new SQLRetCode("23000", 2627), DBError.ERR_CODE_IS_USE);
                CODE_MESSAGES.put(new SQLRetCode("23000", 547), DBError.ERR_FOUND_DEPENDING);
                break;

            case DBTYPE_POSTGRES:
                // postgress

                CODE_MESSAGES.put(new SQLRetCode("23503", 0), DBError.ERR_FOUND_DEPENDING);
                CODE_MESSAGES.put(new SQLRetCode("23505", 0), DBError.ERR_CODE_IS_USE);
                break;

            case DBTYPE_DB2:

                CODE_MESSAGES.put(new SQLRetCode("23504", -532), DBError.ERR_FOUND_DEPENDING);
                CODE_MESSAGES.put(new SQLRetCode("23505", -803), DBError.ERR_CODE_IS_USE);
                break;

            case DBTYPE_ORACLE:
                // oracle
                CODE_MESSAGES.put(new SQLRetCode("23000", 1), DBError.ERR_CODE_IS_USE);
                CODE_MESSAGES.put(new SQLRetCode("23000", 2292), DBError.ERR_FOUND_DEPENDING);

        }

    }

    //@Lock(LockType.READ)
    @Override
    public String getCurrentDatabase() {
        return DBType.getCurrentType().getDbName();
    }

    @Override
    public ILocalizedString getMessage(SQLRetCode rc, String message) {

        if (processor == null) {
            throw new ApplicationException("No exception processor assigned");
        }

        return processor.getMessage(rc, message);

    }



    @Lock(LockType.WRITE)

    public void setDatabaseExceptionProcessor(ICodeBasedExceptionProcessor processor) {
        this.setDatabaseExceptionProcessor(processor, true);
    }

    @Lock(LockType.WRITE)
    private void setDatabaseExceptionProcessor(ICodeBasedExceptionProcessor processor, boolean addDefault) {
        this.processor = processor;
        if (processor == null) {
            throw new ApplicationException("No exception processor assigned");
        }

        if (addDefault) {
            Map<SQLRetCode, DBError> defMsg = CODE_MESSAGES;
            if (defMsg != null && !defMsg.isEmpty()) {
                for (Map.Entry<SQLRetCode, DBError> e : defMsg.entrySet()) {
                    ILocalizedString msg = processor.getMessage(e.getKey(), null);
                    if (msg == null) {
                        processor.addMessageForCode(e.getKey(), e.getValue().getErrMsg());
                    }
                }
            }
        }
    }

}
