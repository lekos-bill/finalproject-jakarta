package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dto.*;

import java.util.List;
import java.util.Map;

public interface ITechnicianService {

    TechnicianReadOnlyDTO insertTechnician(TechnicianInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    TechnicianReadOnlyDTO updateTechnician(TechnicianUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException;

    void remove(Object id)
            throws EntityNotFoundException;



    List<TechnicianReadOnlyDTO> getAllTechnicians();



    List<TechnicianReadOnlyDTO> getTechniciansByCriteria(Map<String , Object> criteria);

//    List<PropertyReadOnlyDTO> getPropertiesByCriteriaPaginated(Map<String, Object> criteria, Integer page, Integer size);

    TechnicianReadOnlyDTO getTechnicianyByPhoneNumber(String phonenumber) throws
            EntityNotFoundException;

    TechnicianReadOnlyDTO insertTechnicianToDamage(Long technicianId, Long damageId) throws EntityAlreadyExistsException, EntityInvalidArgumentException;
}
