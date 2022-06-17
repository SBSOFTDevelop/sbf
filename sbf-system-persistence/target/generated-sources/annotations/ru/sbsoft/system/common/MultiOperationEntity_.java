package ru.sbsoft.system.common;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MultiOperationEntity.class)
public abstract class MultiOperationEntity_ extends ru.sbsoft.dao.entity.BaseEntity_ {

	public static volatile SingularAttribute<MultiOperationEntity, Date> RUN_DATE;
	public static volatile SingularAttribute<MultiOperationEntity, String> CREATE_USER;
	public static volatile SingularAttribute<MultiOperationEntity, String> CLOSE_COMMENT;
	public static volatile SingularAttribute<MultiOperationEntity, Date> END_DATE;
	public static volatile SingularAttribute<MultiOperationEntity, Date> SCHEDULE_DATE;
	public static volatile SingularAttribute<MultiOperationEntity, Boolean> VISIBLE;
	public static volatile SingularAttribute<MultiOperationEntity, Date> CREATE_DATE;
	public static volatile SingularAttribute<MultiOperationEntity, MultiOperationStatus> STATUS;
	public static volatile SingularAttribute<MultiOperationEntity, String> OPERATION_CODE;
	public static volatile SingularAttribute<MultiOperationEntity, String> LOCALE;
	public static volatile SingularAttribute<MultiOperationEntity, Boolean> NEED_NOTIFY;
	public static volatile SingularAttribute<MultiOperationEntity, BigDecimal> PROGRESS;
	public static volatile SingularAttribute<MultiOperationEntity, String> APP_CODE;
	public static volatile SingularAttribute<MultiOperationEntity, String> MODULE_CODE;
	public static volatile SingularAttribute<MultiOperationEntity, Long> RECORD_ID;
	public static volatile SingularAttribute<MultiOperationEntity, String> TITLE;
	public static volatile SingularAttribute<MultiOperationEntity, Boolean> NOTIFIED;
	public static volatile SingularAttribute<MultiOperationEntity, String> PROGRESS_COMMENT;
	public static volatile ListAttribute<MultiOperationEntity, MultiOperationLogEntity> logs;
	public static volatile ListAttribute<MultiOperationEntity, MultiOperationParameterEntity> parameters;
	public static volatile SingularAttribute<MultiOperationEntity, String> CREATE_COMMENT;

	public static final String R_UN__DA_TE = "RUN_DATE";
	public static final String C_RE_AT_E__US_ER = "CREATE_USER";
	public static final String C_LO_SE__CO_MM_EN_T = "CLOSE_COMMENT";
	public static final String E_ND__DA_TE = "END_DATE";
	public static final String S_CH_ED_UL_E__DA_TE = "SCHEDULE_DATE";
	public static final String V_IS_IB_LE = "VISIBLE";
	public static final String C_RE_AT_E__DA_TE = "CREATE_DATE";
	public static final String S_TA_TU_S = "STATUS";
	public static final String O_PE_RA_TI_ON__CO_DE = "OPERATION_CODE";
	public static final String L_OC_AL_E = "LOCALE";
	public static final String N_EE_D__NO_TI_FY = "NEED_NOTIFY";
	public static final String P_RO_GR_ES_S = "PROGRESS";
	public static final String A_PP__CO_DE = "APP_CODE";
	public static final String M_OD_UL_E__CO_DE = "MODULE_CODE";
	public static final String R_EC_OR_D__ID = "RECORD_ID";
	public static final String T_IT_LE = "TITLE";
	public static final String N_OT_IF_IE_D = "NOTIFIED";
	public static final String P_RO_GR_ES_S__CO_MM_EN_T = "PROGRESS_COMMENT";
	public static final String LOGS = "logs";
	public static final String PARAMETERS = "parameters";
	public static final String C_RE_AT_E__CO_MM_EN_T = "CREATE_COMMENT";

}

