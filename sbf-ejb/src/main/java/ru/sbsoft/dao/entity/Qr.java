package ru.sbsoft.dao.entity;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author vlki
 */
public final class Qr {

    public static <T> List<T> getAll(EntityManager em, Class<T> entityClass) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> variableRoot = query.from(entityClass);
        query.select(variableRoot);
        return em.createQuery(query).getResultList();
    }

    private Qr() {
    }
}
