package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dto.DamageInsertDTO;
import gr.aueb.cf.managementapp.dto.DamageReadOnlyDTO;
import gr.aueb.cf.managementapp.dto.DamageUpdateDTO;

import java.util.List;
import java.util.Map;

public interface IDamageService {

    DamageReadOnlyDTO insertDamageToProperty(DamageInsertDTO insertDTO, Long id)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    DamageReadOnlyDTO updateDamage(DamageUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException;

    void remove(Long propertyId, Long damageId) throws EntityNotFoundException;

    List<DamageReadOnlyDTO> getDamagesPerProperty(Long id) throws EntityNotFoundException;

    List<DamageReadOnlyDTO> getAllDamages();

    List<DamageReadOnlyDTO> getDamagesByCriteria(Map<String, Object> criteria);
}
