package ru.sbsoft.system.common;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ApplicationInfoEntity.class)
public abstract class ApplicationInfoEntity_ extends ru.sbsoft.dao.entity.BaseEntity_ {

	public static volatile SingularAttribute<ApplicationInfoEntity, String> appName;
	public static volatile SingularAttribute<ApplicationInfoEntity, String> logo;
	public static volatile SingularAttribute<ApplicationInfoEntity, BigDecimal> id;

	public static final String APP_NAME = "appName";
	public static final String LOGO = "logo";
	public static final String ID = "id";

}

