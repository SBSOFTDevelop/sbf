package ru.sbsoft.dao.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UpdateInfoFields.class)
public abstract class UpdateInfoFields_ {

	public static volatile SingularAttribute<UpdateInfoFields, Date> updateDate;
	public static volatile SingularAttribute<UpdateInfoFields, String> updateUser;

	public static final String UPDATE_DATE = "updateDate";
	public static final String UPDATE_USER = "updateUser";

}

