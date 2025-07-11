package gr.aueb.cf.managementapp.dao;

import gr.aueb.cf.managementapp.core.enums.ChangeType;
import gr.aueb.cf.managementapp.model.CostPerChange;
import gr.aueb.cf.managementapp.model.IdentifiableEntity;
import gr.aueb.cf.managementapp.service.util.JPAHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.*;

//2)Αν θέλω να χρησιμοποιήσω Generics σε άλλα entities που δεν έχουν οριστεί
public abstract class AbstractDAO<T extends IdentifiableEntity> implements IGenericDAO<T> {

    private Class<T> persistenceClass;

    public AbstractDAO() {

    }

    public Class<T> getPersistenceClass() {
        return persistenceClass;
    }

    public void setPersistenceClass(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    public Optional<T> insert(T t) {
        EntityManager em = getEntityManager();
        em.persist(t);
        return Optional.of(t);
    }

    public Optional<T> update(T t) {
        EntityManager em = getEntityManager();
        em.merge(t);
        return Optional.of(t);
    }

    public void delete(Object id) {
        EntityManager em = getEntityManager();
        Optional<T> toDelete = getById(id);
        toDelete.ifPresent(em::remove);
    }

    public Optional<T> getById(Object id) {
        EntityManager em = getEntityManager();
        return Optional.ofNullable(em.find(this.getPersistenceClass(), id));
    }

    public Optional<T> findByField(String fieldName, Object value) {
        String queryString = "SELECT e FROM "+ getPersistenceClass().getSimpleName() + " e WHERE "+ fieldName +" = :value";
        TypedQuery<T> query = getEntityManager().createQuery(queryString, persistenceClass);
        query.setParameter("value", value);
        return query.getResultList().stream().findFirst();
    }

    public Long count() {
        String queryString = "SELECT COUNT(t) from " + getPersistenceClass().getSimpleName() + " t";
        TypedQuery<Long> query = getEntityManager().createQuery(queryString, Long.class);
        return query.getSingleResult();
    }

    public EntityManager getEntityManager() {
        return JPAHelper.getEntityManager();
    }

    public Long getCountByCriteria(Map<String, Object> criteria) {
        EntityManager em = getEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> selectQuery = builder.createQuery(Long.class);
        Root<T> entityRoot = selectQuery.from(persistenceClass);

        List<Predicate> predicates = getPredicatesList(builder, entityRoot, criteria);
        selectQuery.select(builder.count(entityRoot)).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Long> query = em.createQuery(selectQuery);
        return query.getSingleResult();
    }


    public List<T> getByCriteria(Map<String, Object> criteria) {
        EntityManager em = getEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> selectQuery = builder.createQuery(persistenceClass);
        Root<T> entityRoot = selectQuery.from(persistenceClass);

        List<Predicate> predicates = getPredicatesList(builder, entityRoot, criteria);
        selectQuery.select(entityRoot).where(predicates.toArray(new Predicate[0]));
        TypedQuery<T> query = em.createQuery(selectQuery);
        return query.getResultList();
    }


    public List<T> getAll() {

        return getByCriteria(Collections.emptyMap());
    }

    @SuppressWarnings("unchecked")
    protected List<Predicate> getPredicatesList(CriteriaBuilder builder, Root<T> entityRoot, Map<String, Object> criteria) {
    List<Predicate> predicates = new ArrayList<>();

    for (Map.Entry<String, Object> entry : criteria.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();

        // Handling the cases where the value is a List, Map or a "isNull" condition
        if (value instanceof List && !((List<?>) value).isEmpty()) {
            Path<?> path = resolvePath(entityRoot, key);
            CriteriaBuilder.In<Object> inClause = builder.in(path);
            for (Object v : (List<?>) value) {
                inClause.value(v);
            }
            predicates.add( inClause);
        } else if (value instanceof List && ((List<?>) value).isEmpty()){
            continue;
        } else if (value instanceof Map) {
            // For 'BETWEEN' condition
            Map<String, Object> mapValue = (Map<String, Object>) value;
            if (mapValue.containsKey("from") && mapValue.containsKey("to")) {
                Object from = mapValue.get("from");
                Object to = mapValue.get("to");

                if (from instanceof Comparable && to instanceof Comparable) {
                    Expression<? extends Comparable<Object>> path =
                           (Expression<? extends Comparable<Object>>) resolvePath(entityRoot, key);

                    predicates.add(builder.between( path, (Comparable<Object>) from, (Comparable<Object>) to));
                }
            }
        } else if ("isNull".equals(value)) {
            // For 'IS NULL' condition
            predicates.add( builder.isNull(resolvePath(entityRoot, key)));
        } else if ("isNotNull".equals(value)) {
            // For 'IS NOT NULL' condition
            predicates.add( builder.isNotNull(resolvePath(entityRoot, key)));
        } else if (value instanceof String && ((String) value).contains("%")) {
            // Treat as LIKE pattern (e.g., "Jo%")
            predicates.add(
                     builder.like(
                            builder.lower((Expression<String>) resolvePath(entityRoot, key)), ((String) value).toLowerCase()));
        } else {
            // For '=' condition (default case)
            predicates.add(builder.equal(resolvePath(entityRoot, key), value));
        }
    }
        return predicates;
    }


    protected Path<?> resolvePath(Root<T> root, String expression) {
        String[] fields = expression.split("\\.");
        Path<?> path = root.get(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            path = path.get(fields[i]);
        }
        return path;
    }

    public List<Object[]> getCostOfDamagesPerProperty() {

        EntityManager em = getEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> selectQuery = builder.createQuery(Object[].class);
        Root<T> entityRoot = selectQuery.from(getPersistenceClass());

        selectQuery.groupBy(entityRoot.get("property").get("id"), entityRoot.get("changeType"));
        selectQuery.multiselect(entityRoot.get("property").get("id"), entityRoot.get("property")
                .get("road"), entityRoot.get("changeType"), builder.sum((Expression<Number>) resolvePath(entityRoot, "costForWork")),
                        builder.sum((Expression<Number>) resolvePath(entityRoot, "costForMaterials")));

        //builder.sum((Expression<Number>) resolvePath(entityRoot, "costForMaterials")

        return em.createQuery(selectQuery).getResultList();
    }
}
