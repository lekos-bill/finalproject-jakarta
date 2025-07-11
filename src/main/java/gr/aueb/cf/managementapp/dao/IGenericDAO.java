package gr.aueb.cf.managementapp.dao;


import gr.aueb.cf.managementapp.model.CostPerChange;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IGenericDAO<T> {
    Optional<T> insert(T t);//

    Optional<T> update(T t);//

    void delete(Object id);//

    Long count();//

    Long getCountByCriteria(Map<String, Object> criteria);//

    Optional<T> getById(Object id);//

    Optional<T> findByField(String fieldName, Object value);//

    List<T> getAll();

    List<T> getByCriteria(Map<String, Object> criteria);

    List<Object[]> getCostOfDamagesPerProperty();


}
