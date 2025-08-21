package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dao.IDamageDAO;
import gr.aueb.cf.managementapp.dao.IPropertyDAO;
import gr.aueb.cf.managementapp.dto.*;
import gr.aueb.cf.managementapp.mapper.Mapper;
import gr.aueb.cf.managementapp.model.Damage;
import gr.aueb.cf.managementapp.model.Property;
import gr.aueb.cf.managementapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class DamageServiceImpl implements IDamageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DamageServiceImpl.class);

    private final IDamageDAO damageDAO;
    private final  IPropertyDAO propertyDAO;

    @Inject
    public DamageServiceImpl(IDamageDAO damageDAO, IPropertyDAO propertyDAO) {
        this.damageDAO = damageDAO;
        this.propertyDAO= propertyDAO;
    }


    public DamageReadOnlyDTO insertDamageToProperty(DamageInsertDTO insertDTO, Long id)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Property property = propertyDAO.findByField("id", id).get();

            Damage damage = Mapper.mapDamageInsertDTOToDamage(insertDTO, property);
            property.addDamage(damage);
            if (damageDAO.findByField("description", damage.getDescription()).isPresent()) {
                throw new EntityAlreadyExistsException("Damage", "Damage already exists");
            }
            DamageReadOnlyDTO damageReadOnlyDTO = damageDAO.insert(damage)
                    .map(Mapper::mapDamagetoDamageReadOnlyDTO)
                    .orElseThrow(() ->new EntityInvalidArgumentException("Damage", "Damage not inserted"));//δεν καταλαβαινω πιο exception βλεπουμε εδω
            JPAHelper.commitTransaction();
            LOGGER.info("Property with atak={} inserted", property.getAtak());
            return damageReadOnlyDTO;
        } catch (EntityAlreadyExistsException | EntityInvalidArgumentException  e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Property with atak={} not inserted, Reason={}", insertDTO.description(), e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public DamageReadOnlyDTO updateDamage(DamageUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            Damage damage = Mapper.mapDamageUpdateDTOToDamage(updateDTO);
            damageDAO.getById(updateDTO.id()).orElseThrow(() -> new EntityNotFoundException("Damage","Damage not found"));
            Property property = damageDAO.getById(updateDTO.id()).get().getProperty();
            damage.setProperty(property);

            DamageReadOnlyDTO damageReadOnlyDTO = damageDAO.update(damage)
                    .map(Mapper::mapDamagetoDamageReadOnlyDTO)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Property", "Property not updated"));
            JPAHelper.commitTransaction();
            LOGGER.info("Damage with description={} updated", damage.getDescription());
            return damageReadOnlyDTO;
        } catch (EntityInvalidArgumentException | EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Damage with description={} not updated, Reason={}", updateDTO.description(), e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }



    public void remove(Long propertyId, Long damageId) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            Damage damage = damageDAO.getById(damageId).orElseThrow(() -> new EntityNotFoundException("Damage", "Damage not found"));

            Optional<Property> property = propertyDAO.getById(propertyId);
            if (property.isPresent()) {
                property.get().removeDamage(damage);
            }
            damageDAO.delete(damageId);
            JPAHelper.commitTransaction();
            LOGGER.info("Damage with id={} deleted", damageId);
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Damage with id:{} not deleted", damageId);
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public List<DamageReadOnlyDTO> getDamagesPerProperty(Long id) throws EntityNotFoundException {
        JPAHelper.beginTransaction();
        Property property = propertyDAO.getById(id).orElseThrow(() -> new EntityNotFoundException("Property", "Property not found"));
        List<DamageReadOnlyDTO> listOfDamages = property.getDamages().stream().map(Mapper::mapDamagetoDamageReadOnlyDTO).collect(Collectors.toList());
        JPAHelper.commitTransaction();
        return listOfDamages;
    }

    public List<DamageReadOnlyDTO> getAllDamages() {
        try {
            JPAHelper.beginTransaction();
            List<DamageReadOnlyDTO> listOfDamageReadOnlyDTOs = Mapper.mapDamagesToListOfDamageReadOnlyDTOs(damageDAO.getAll());
            JPAHelper.commitTransaction();
            return listOfDamageReadOnlyDTOs;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public List<DamageReadOnlyDTO> getDamagesByCriteria(Map<String, Object> criteria) {
        try {
            JPAHelper.beginTransaction();
            List<DamageReadOnlyDTO> readOnlyDTOS = damageDAO.getByCriteria(criteria)
                    .stream()
                    .map(Mapper::mapDamagetoDamageReadOnlyDTO)
                    .collect(Collectors.toList());
            JPAHelper.commitTransaction();
            return readOnlyDTOS;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }
}
