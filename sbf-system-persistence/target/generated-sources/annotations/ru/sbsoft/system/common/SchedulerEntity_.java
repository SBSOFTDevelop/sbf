package ru.sbsoft.system.common;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.sbsoft.shared.model.enums.SchedulerStatus;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SchedulerEntity.class)
public abstract class SchedulerEntity_ extends ru.sbsoft.dao.entity.BaseEntity_ {

	public static volatile SingularAttribute<SchedulerEntity, Date> ENABLE_FROM;
	public static volatile SingularAttribute<SchedulerEntity, String> CRON_EXPRESSION;
	public static volatile SingularAttribute<SchedulerEntity, Date> ENABLE_TO;
	public static volatile SingularAttribute<SchedulerEntity, SchedulerStatus> STATUS;
	public static volatile SingularAttribute<SchedulerEntity, String> OPERATION_CODE;
	public static volatile SingularAttribute<SchedulerEntity, Boolean> NOTIFY;
	public static volatile SetAttribute<SchedulerEntity, MultiOperationEntity> operations;
	public static volatile SingularAttribute<SchedulerEntity, Date> NEXT_SCHEDULE;
	public static volatile SingularAttribute<SchedulerEntity, Boolean> IGNORE_BACK;
	public static volatile SingularAttribute<SchedulerEntity, String> APP_CODE;
	public static volatile SingularAttribute<SchedulerEntity, String> MODULE_CODE;
	public static volatile SingularAttribute<SchedulerEntity, BigDecimal> RECORD_ID;
	public static volatile SingularAttribute<SchedulerEntity, Date> PREV_SCHEDULE;
	public static volatile SingularAttribute<SchedulerEntity, String> USERNAME;
	public static volatile SingularAttribute<SchedulerEntity, Boolean> ENABLED;
	public static volatile SingularAttribute<SchedulerEntity, Integer> COUNTER;
	public static volatile SingularAttribute<SchedulerEntity, Date> LAST_RUN;

	public static final String E_NA_BL_E__FR_OM = "ENABLE_FROM";
	public static final String C_RO_N__EX_PR_ES_SI_ON = "CRON_EXPRESSION";
	public static final String E_NA_BL_E__TO = "ENABLE_TO";
	public static final String S_TA_TU_S = "STATUS";
	public static final String O_PE_RA_TI_ON__CO_DE = "OPERATION_CODE";
	public static final String N_OT_IF_Y = "NOTIFY";
	public static final String OPERATIONS = "operations";
	public static final String N_EX_T__SC_HE_DU_LE = "NEXT_SCHEDULE";
	public static final String I_GN_OR_E__BA_CK = "IGNORE_BACK";
	public static final String A_PP__CO_DE = "APP_CODE";
	public static final String M_OD_UL_E__CO_DE = "MODULE_CODE";
	public static final String R_EC_OR_D__ID = "RECORD_ID";
	public static final String P_RE_V__SC_HE_DU_LE = "PREV_SCHEDULE";
	public static final String U_SE_RN_AM_E = "USERNAME";
	public static final String E_NA_BL_ED = "ENABLED";
	public static final String C_OU_NT_ER = "COUNTER";
	public static final String L_AS_T__RU_N = "LAST_RUN";

}

