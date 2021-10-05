package ru.sbsoft.system.common;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.dao.entity.CreateInfoFields;
import ru.sbsoft.dao.entity.ICreateInfoEntity;
import ru.sbsoft.dao.entity.IFormEntity;
import ru.sbsoft.dao.entity.IUpdateInfoEntity;
import ru.sbsoft.dao.entity.UpdateInfoFields;
import ru.sbsoft.shared.model.enums.GridTypeEnum;

/**
 * Класс сущности "Пользовательский отчет"
 *
 * @author sokolov
 */
@Entity
@Table(name = "SR_REPORT")
public class CustomReportEntity extends BaseEntity implements IFormEntity, ICreateInfoEntity, IUpdateInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREPORT_GEN")
    @SequenceGenerator(name = "CREPORT_GEN", sequenceName = "SYS_SEQUENCE", allocationSize = 1)
    private BigDecimal id;

    @Column(name = "grid_enum", nullable = false)
    private String gridEnum;

    @Column(name = "grid_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private GridTypeEnum gridType;

    @Column(name = "report_name", nullable = false)
    private String reportName;

    @Column(name = "report_path", nullable = false)
    private String reportPath;

    @Column(name = "include_idrow", nullable = false)
    private boolean includeIdRow;

    @Column(name = "header_sql", nullable = true)
    private String headerSQL;

    @Embedded
    private CreateInfoFields createInfoFields;
    @Embedded
    private UpdateInfoFields updateInfoFields;

    @OneToMany(mappedBy = "report", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CustomRepfilterEntity> filters;

    @OneToMany(mappedBy = "report", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CustomRepparamEntity> params;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getGridEnum() {
        return gridEnum;
    }

    public void setGridEnum(String gridEnum) {
        this.gridEnum = gridEnum;
    }

    public GridTypeEnum getGridType() {
        return gridType;
    }

    public void setGridType(GridTypeEnum gridType) {
        this.gridType = gridType;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getHeaderSQL() {
        return headerSQL;
    }

    public void setHeaderSQL(String headerSQL) {
        this.headerSQL = headerSQL;
    }

    @Override
    public UpdateInfoFields getUpdateInfoFields() {
        return updateInfoFields;
    }

    @Override
    public void setUpdateInfoFields(UpdateInfoFields updateInfoFields) {
        this.updateInfoFields = updateInfoFields;
    }

    @Override
    public CreateInfoFields getCreateInfoFields() {
        return createInfoFields;
    }

    @Override
    public void setCreateInfoFields(CreateInfoFields createInfoFields) {
        this.createInfoFields = createInfoFields;
    }

    public boolean isIncludeIdRow() {
        return includeIdRow;
    }

    public void setIncludeIdRow(boolean includeIdRow) {
        this.includeIdRow = includeIdRow;
    }

    public List<CustomRepfilterEntity> getFilters() {
        return filters;
    }

    public void setFilters(List<CustomRepfilterEntity> filters) {
        this.filters = filters;
    }

    public List<CustomRepparamEntity> getParams() {
        return params;
    }

    public void setParams(List<CustomRepparamEntity> params) {
        this.params = params;
    }

}
