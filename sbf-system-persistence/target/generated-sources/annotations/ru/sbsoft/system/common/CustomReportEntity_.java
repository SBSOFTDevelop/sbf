package ru.sbsoft.system.common;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.sbsoft.dao.entity.CreateInfoFields;
import ru.sbsoft.dao.entity.UpdateInfoFields;
import ru.sbsoft.shared.model.enums.GridTypeEnum;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomReportEntity.class)
public abstract class CustomReportEntity_ extends ru.sbsoft.dao.entity.BaseEntity_ {

	public static volatile SingularAttribute<CustomReportEntity, String> reportName;
	public static volatile SingularAttribute<CustomReportEntity, GridTypeEnum> gridType;
	public static volatile SingularAttribute<CustomReportEntity, String> reportPath;
	public static volatile SingularAttribute<CustomReportEntity, String> headerSQL;
	public static volatile SingularAttribute<CustomReportEntity, String> gridEnum;
	public static volatile SingularAttribute<CustomReportEntity, UpdateInfoFields> updateInfoFields;
	public static volatile SingularAttribute<CustomReportEntity, BigDecimal> id;
	public static volatile SingularAttribute<CustomReportEntity, Boolean> includeIdRow;
	public static volatile ListAttribute<CustomReportEntity, CustomRepfilterEntity> filters;
	public static volatile ListAttribute<CustomReportEntity, CustomRepparamEntity> params;
	public static volatile SingularAttribute<CustomReportEntity, CreateInfoFields> createInfoFields;

	public static final String REPORT_NAME = "reportName";
	public static final String GRID_TYPE = "gridType";
	public static final String REPORT_PATH = "reportPath";
	public static final String HEADER_SQ_L = "headerSQL";
	public static final String GRID_ENUM = "gridEnum";
	public static final String UPDATE_INFO_FIELDS = "updateInfoFields";
	public static final String ID = "id";
	public static final String INCLUDE_ID_ROW = "includeIdRow";
	public static final String FILTERS = "filters";
	public static final String PARAMS = "params";
	public static final String CREATE_INFO_FIELDS = "createInfoFields";

}

