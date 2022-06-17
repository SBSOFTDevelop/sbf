package ru.sbsoft.system.common;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.sbsoft.shared.model.OperationEventType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CommonOperationLogEntity.class)
public abstract class CommonOperationLogEntity_ extends ru.sbsoft.dao.entity.BaseEntity_ {

	public static volatile SingularAttribute<CommonOperationLogEntity, String> TRACE;
	public static volatile SingularAttribute<CommonOperationLogEntity, String> MESSAGE;
	public static volatile SingularAttribute<CommonOperationLogEntity, Date> CREATE_DATE;
	public static volatile SingularAttribute<CommonOperationLogEntity, String> CREATE_USER;
	public static volatile SingularAttribute<CommonOperationLogEntity, BigDecimal> RECORD_ID;
	public static volatile SingularAttribute<CommonOperationLogEntity, OperationEventType> TYPE_VALUE;

	public static final String T_RA_CE = "TRACE";
	public static final String M_ES_SA_GE = "MESSAGE";
	public static final String C_RE_AT_E__DA_TE = "CREATE_DATE";
	public static final String C_RE_AT_E__US_ER = "CREATE_USER";
	public static final String R_EC_OR_D__ID = "RECORD_ID";
	public static final String T_YP_E__VA_LU_E = "TYPE_VALUE";

}

