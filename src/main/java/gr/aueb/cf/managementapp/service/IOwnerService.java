package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dto.*;

import java.util.List;
import java.util.Map;

public interface IOwnerService {

    OwnerReadOnlyDTO insertOwnerToProperty(OwnerInsertDTO insertDTO, Long id)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    OwnerReadOnlyDTO insertOwner(OwnerInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    OwnerReadOnlyDTO updateOwner(OwnerUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException;

    void remove(Long ownerId) throws EntityNotFoundException;

//    List<OwnerReadOnlyDTO> getOwnersPerProperty(Long id) throws EntityNotFoundException;

    List<OwnerReadOnlyDTO> getAllOwners();

    List<OwnerReadOnlyDTO> getOwnersByCriteria(Map<String, Object> criteria);
}
