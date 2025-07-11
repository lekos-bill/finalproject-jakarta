package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.dao.IGenericDAO;
import gr.aueb.cf.managementapp.dao.IPropertyDAO;
import gr.aueb.cf.managementapp.dao.PropertyDAOImpl;
import gr.aueb.cf.managementapp.dto.DamageReadOnlyDTO;
import gr.aueb.cf.managementapp.dto.PropertyInsertDTO;
import gr.aueb.cf.managementapp.dto.PropertyReadOnlyDTO;
import gr.aueb.cf.managementapp.dto.PropertyUpdateDTO;
import gr.aueb.cf.managementapp.model.Property;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Map;

public interface IPropertyService {

    PropertyReadOnlyDTO insertProperty(PropertyInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    PropertyReadOnlyDTO updateProperty(PropertyUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException;

    void remove(Object id)
            throws EntityNotFoundException;

    List<PropertyReadOnlyDTO> getAllProperties();


    List<PropertyReadOnlyDTO> getPropertiesByCriteria(Map<String , Object> criteria);


    PropertyReadOnlyDTO getPropertyByAtak(String atak) throws
            EntityNotFoundException;
}
