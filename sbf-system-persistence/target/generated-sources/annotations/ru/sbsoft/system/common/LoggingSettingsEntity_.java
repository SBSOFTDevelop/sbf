package ru.sbsoft.system.common;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.sbsoft.dao.entity.UpdateInfoFields;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LoggingSettingsEntity.class)
public abstract class LoggingSettingsEntity_ extends ru.sbsoft.dao.entity.BaseEntity_ {

	public static volatile SingularAttribute<LoggingSettingsEntity, String> code;
	public static volatile SingularAttribute<LoggingSettingsEntity, Boolean> active;
	public static volatile SingularAttribute<LoggingSettingsEntity, UpdateInfoFields> updateInfoFields;
	public static volatile SingularAttribute<LoggingSettingsEntity, BigDecimal> id;

	public static final String CODE = "code";
	public static final String ACTIVE = "active";
	public static final String UPDATE_INFO_FIELDS = "updateInfoFields";
	public static final String ID = "id";

}

