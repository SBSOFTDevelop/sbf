package ru.sbsoft.system.dao.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ru.sbsoft.dao.report.ICustomReportDao;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.interfaces.DynamicGridType;
import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.model.CustomReportInfo;
import ru.sbsoft.shared.model.CustomReportParamModel;
import ru.sbsoft.shared.param.StringParamInfo;
import ru.sbsoft.system.common.CustomRepfilterEntity;
import ru.sbsoft.system.common.CustomReportEntity;
import ru.sbsoft.system.common.CustomReportEntity_;
import ru.sbsoft.system.common.CustomRepparamEntity;

/**
 *
 * @author sokolov
 */
@Stateless
@Remote(ICustomReportDao.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@PermitAll
public class CustomReportDaoBean implements ICustomReportDao {

    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager em;

    @Override
    public List<CustomReportInfo> getCustomReports(GridContext context, List<StringParamInfo> filters) {
        List<CustomReportInfo> result = new ArrayList<>();
        getReports(context).forEach(re -> {
            if (checkFilters(re, filters)) {
                result.add(createModel(re));
            }
        });
        return result;
    }

    @Override
    public CustomReportInfo getCustomReport(BigDecimal reportId) {
        CustomReportEntity entity = em.find(CustomReportEntity.class, reportId);
        return entity != null ? createModel(entity) : null;
    }

    private List<CustomReportEntity> getReports(GridContext context) {
        CriteriaBuilder b = em.getCriteriaBuilder();
        CriteriaQuery<CustomReportEntity> q = b.createQuery(CustomReportEntity.class);
        Root<CustomReportEntity> r = q.from(CustomReportEntity.class);
        q.where(b.equal(r.get(CustomReportEntity_.gridEnum), getCode(context)));
        return em.createQuery(q).getResultList();
    }

    private boolean checkFilters(CustomReportEntity reportEntity, List<StringParamInfo> filters) {
        if (reportEntity.getFilters() == null || reportEntity.getFilters().isEmpty()) {
            return true;
        }
        if (filters == null || filters.isEmpty()) {
            return false;
        }
        for (CustomRepfilterEntity re : reportEntity.getFilters()) {
            if (!filters.stream().filter(f -> re.getCode().equals(f.getName()) && re.getValue().equals(f.getValue())).findFirst().isPresent()) {
                return false;
            }
        };
        return true;
    }

    private CustomReportInfo createModel(CustomReportEntity entity) {
        CustomReportInfo reportInfo = new CustomReportInfo();
        reportInfo.setReportId(entity.getId());
        reportInfo.setTitle(entity.getReportName());
        reportInfo.setReportPath(entity.getReportPath());
        reportInfo.setIncudeIdRow(entity.isIncludeIdRow());
        reportInfo.setHeaderSQL(entity.getHeaderSQL());
        if (entity.getParams() != null) {
            reportInfo.setParams(entity.getParams().stream().map(pe -> paramToModel(pe)).collect(Collectors.toList()));
        }
        return reportInfo;
    }

    private String getCode(GridContext context) {
        GridType gridType = context.getGridType();
        if (gridType instanceof DynamicGridType) {
            return ((DynamicGridType) gridType).getGroupCode();
        }
        return gridType.getCode();
    }

    private CustomReportParamModel paramToModel(CustomRepparamEntity pe) {
        CustomReportParamModel pm = new CustomReportParamModel();
        pm.setId(pe.getId());
        pm.setCode(pe.getCode());
        pm.setParamType(pe.getParamType());
        pm.setName(pe.getName());
        return pm;
    }

}
