package ru.sbsoft.dao.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.sbsoft.sbf.app.model.YearMonthDay;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ActRangeFields.class)
public abstract class ActRangeFields_ {

	public static volatile SingularAttribute<ActRangeFields, YearMonthDay> begDate;
	public static volatile SingularAttribute<ActRangeFields, YearMonthDay> endDate;

	public static final String BEG_DATE = "begDate";
	public static final String END_DATE = "endDate";

}

