package ru.sbsoft.dao.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CreateInfoFields.class)
public abstract class CreateInfoFields_ {

	public static volatile SingularAttribute<CreateInfoFields, String> createUser;
	public static volatile SingularAttribute<CreateInfoFields, Date> createDate;

	public static final String CREATE_USER = "createUser";
	public static final String CREATE_DATE = "createDate";

}

