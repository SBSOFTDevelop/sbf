package ru.sbsoft.system.common;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MultiOperationParameterEntity.class)
public abstract class MultiOperationParameterEntity_ extends ru.sbsoft.dao.entity.BaseEntity_ {

	public static volatile SingularAttribute<MultiOperationParameterEntity, String> PARAM_NAME;
	public static volatile SingularAttribute<MultiOperationParameterEntity, String> PARAM_CLASS;
	public static volatile SingularAttribute<MultiOperationParameterEntity, String> PARAM_VALUE;
	public static volatile SingularAttribute<MultiOperationParameterEntity, MultiOperationEntity> OPERATION;
	public static volatile SingularAttribute<MultiOperationParameterEntity, Long> RECORD_ID;

	public static final String P_AR_AM__NA_ME = "PARAM_NAME";
	public static final String P_AR_AM__CL_AS_S = "PARAM_CLASS";
	public static final String P_AR_AM__VA_LU_E = "PARAM_VALUE";
	public static final String O_PE_RA_TI_ON = "OPERATION";
	public static final String R_EC_OR_D__ID = "RECORD_ID";

}

