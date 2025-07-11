package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dto.*;
import gr.aueb.cf.managementapp.model.Renovation;

import java.util.List;
import java.util.Map;

public interface IRenovationService {
    RenovationReadOnlyDTO insertRenovationToProperty(RenovationInsertDTO insertDTO, Long id)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    RenovationReadOnlyDTO updateRenovation(RenovationUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException;

    void remove(Long propertyId, Long renovationId) throws EntityNotFoundException;

    List<RenovationReadOnlyDTO> getRenovationsPerProperty(Long id) throws EntityNotFoundException;

    List<RenovationReadOnlyDTO> getAllRenovations();

    List<RenovationReadOnlyDTO> getRenovationsByCriteria(Map<String, Object> criteria);
}
