package ru.sbsoft.system.common;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomRepfilterEntity.class)
public abstract class CustomRepfilterEntity_ extends ru.sbsoft.dao.entity.BaseEntity_ {

	public static volatile SingularAttribute<CustomRepfilterEntity, String> code;
	public static volatile SingularAttribute<CustomRepfilterEntity, CustomReportEntity> report;
	public static volatile SingularAttribute<CustomRepfilterEntity, BigDecimal> id;
	public static volatile SingularAttribute<CustomRepfilterEntity, String> value;

	public static final String CODE = "code";
	public static final String REPORT = "report";
	public static final String ID = "id";
	public static final String VALUE = "value";

}

