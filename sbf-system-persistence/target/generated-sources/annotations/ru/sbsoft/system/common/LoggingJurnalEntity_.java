package ru.sbsoft.system.common;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.sbsoft.dao.entity.CreateInfoFields;
import ru.sbsoft.shared.consts.LoggingEventType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LoggingJurnalEntity.class)
public abstract class LoggingJurnalEntity_ extends ru.sbsoft.dao.entity.BaseEntity_ {

	public static volatile SingularAttribute<LoggingJurnalEntity, String> code;
	public static volatile SingularAttribute<LoggingJurnalEntity, String> objectJson;
	public static volatile SingularAttribute<LoggingJurnalEntity, String> modelClass;
	public static volatile SingularAttribute<LoggingJurnalEntity, BigDecimal> id;
	public static volatile SingularAttribute<LoggingJurnalEntity, LoggingEventType> eventType;
	public static volatile SingularAttribute<LoggingJurnalEntity, BigDecimal> objectId;
	public static volatile SingularAttribute<LoggingJurnalEntity, CreateInfoFields> createInfoFields;

	public static final String CODE = "code";
	public static final String OBJECT_JSON = "objectJson";
	public static final String MODEL_CLASS = "modelClass";
	public static final String ID = "id";
	public static final String EVENT_TYPE = "eventType";
	public static final String OBJECT_ID = "objectId";
	public static final String CREATE_INFO_FIELDS = "createInfoFields";

}

