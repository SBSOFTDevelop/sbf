package ru.sbsoft.system.common;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.sbsoft.shared.consts.CustomReportParamType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomRepparamEntity.class)
public abstract class CustomRepparamEntity_ extends ru.sbsoft.dao.entity.BaseEntity_ {

	public static volatile SingularAttribute<CustomRepparamEntity, CustomReportParamType> paramType;
	public static volatile SingularAttribute<CustomRepparamEntity, String> code;
	public static volatile SingularAttribute<CustomRepparamEntity, CustomReportEntity> report;
	public static volatile SingularAttribute<CustomRepparamEntity, String> name;
	public static volatile SingularAttribute<CustomRepparamEntity, BigDecimal> id;

	public static final String PARAM_TYPE = "paramType";
	public static final String CODE = "code";
	public static final String REPORT = "report";
	public static final String NAME = "name";
	public static final String ID = "id";

}

