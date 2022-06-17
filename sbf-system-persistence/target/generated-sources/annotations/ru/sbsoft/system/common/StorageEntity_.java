package ru.sbsoft.system.common;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StorageEntity.class)
public abstract class StorageEntity_ {

	public static volatile SingularAttribute<StorageEntity, Date> CREATE_DATE;
	public static volatile SingularAttribute<StorageEntity, String> DESCRIPTION;
	public static volatile SingularAttribute<StorageEntity, String> CREATE_USER;
	public static volatile SingularAttribute<StorageEntity, String> ALIAS;
	public static volatile SingularAttribute<StorageEntity, Long> STORAGE_ID;
	public static volatile SingularAttribute<StorageEntity, byte[]> CONTENT;
	public static volatile SingularAttribute<StorageEntity, String> FILENAME;

	public static final String C_RE_AT_E__DA_TE = "CREATE_DATE";
	public static final String D_ES_CR_IP_TI_ON = "DESCRIPTION";
	public static final String C_RE_AT_E__US_ER = "CREATE_USER";
	public static final String A_LI_AS = "ALIAS";
	public static final String S_TO_RA_GE__ID = "STORAGE_ID";
	public static final String C_ON_TE_NT = "CONTENT";
	public static final String F_IL_EN_AM_E = "FILENAME";

}

