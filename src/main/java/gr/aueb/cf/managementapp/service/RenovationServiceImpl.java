package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dao.IDamageDAO;
import gr.aueb.cf.managementapp.dao.IPropertyDAO;
import gr.aueb.cf.managementapp.dao.IRenovationDAO;
import gr.aueb.cf.managementapp.dto.*;
import gr.aueb.cf.managementapp.mapper.Mapper;
import gr.aueb.cf.managementapp.model.Damage;
import gr.aueb.cf.managementapp.model.Property;
import gr.aueb.cf.managementapp.model.Renovation;
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
public class RenovationServiceImpl implements IRenovationService{
    private static final Logger LOGGER = LoggerFactory.getLogger(DamageServiceImpl.class);

    private final IRenovationDAO renovationDAO;
    private final IPropertyDAO propertyDAO;

    @Inject
    public RenovationServiceImpl(IRenovationDAO renovationDAO, IPropertyDAO propertyDAO) {
        this.renovationDAO = renovationDAO;
        this.propertyDAO= propertyDAO;
    }


    public RenovationReadOnlyDTO insertRenovationToProperty(RenovationInsertDTO insertDTO, Long id)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Property property = propertyDAO.findByField("id", id).get();

            Renovation renovation = Mapper.mapRenovationInsertDTOToRenovation(insertDTO, property);
            property.addRenovation(renovation);
            if (renovationDAO.findByField("description", renovation.getDescription()).isPresent()) {
                throw new EntityAlreadyExistsException("Renovation", "Renovation already exists");
            }
            RenovationReadOnlyDTO renovationReadOnlyDTO = renovationDAO.insert(renovation)
                    .map(Mapper::mapRenovationToRenovationReadOnlyDTO)
                    .orElseThrow(() ->new EntityInvalidArgumentException("Damage", "Damage not inserted"));//δεν καταλαβαινω πιο exception βλεπουμε εδω
            JPAHelper.commitTransaction();
            LOGGER.info("Property with atak={} inserted", property.getAtak());
            return renovationReadOnlyDTO;
        } catch (EntityAlreadyExistsException | EntityInvalidArgumentException  e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Property with atak={} not inserted, Reason={}", insertDTO.description(), e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public RenovationReadOnlyDTO updateRenovation(RenovationUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            Renovation renovation = Mapper.mapRenovationUpdateDTOToRenovation(updateDTO);
            renovationDAO.getById(updateDTO.id()).orElseThrow(() -> new EntityNotFoundException("Renovation"," not found"));
            Property property = renovationDAO.getById(updateDTO.id()).get().getProperty();
            renovation.setProperty(property);

            RenovationReadOnlyDTO damageReadOnlyDTO = renovationDAO.update(renovation)
                    .map(Mapper::mapRenovationToRenovationReadOnlyDTO)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Property", "Property not updated"));
            JPAHelper.commitTransaction();
            LOGGER.info("Renovation with description={} updated", renovation.getDescription());
            return damageReadOnlyDTO;
        } catch (EntityInvalidArgumentException | EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Renovation with description={} not updated, Reason={}", updateDTO.description(), e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public void remove(Long propertyId, Long renovationId) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            Renovation renovation = renovationDAO.getById(renovationId).orElseThrow(() -> new EntityNotFoundException("Renovation", "Renovation not found"));

            Optional<Property> property = propertyDAO.getById(propertyId);
            if (property.isPresent()) {
                property.get().removeRenovation(renovation);
            }
            renovationDAO.delete(renovationId);
            JPAHelper.commitTransaction();
            LOGGER.info("Renovation with id={} deleted", renovationId);
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Renovation with id:{} not deleted", renovationId);
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public List<RenovationReadOnlyDTO> getRenovationsPerProperty(Long id) throws EntityNotFoundException {
        JPAHelper.beginTransaction();
        Property property = propertyDAO.getById(id).orElseThrow(() -> new EntityNotFoundException("Property", "Property not found"));
        List<RenovationReadOnlyDTO> listOfRenovations = property.getRenovations().stream().map(Mapper::mapRenovationToRenovationReadOnlyDTO).collect(Collectors.toList());
        JPAHelper.commitTransaction();
        return listOfRenovations;
    }

    public List<RenovationReadOnlyDTO> getAllRenovations() {
        try {
            JPAHelper.beginTransaction();
            List<RenovationReadOnlyDTO> listOfRenovationReadOnlyDTOs = Mapper.mapRenovationsToListOfRenovationReadOnlyDTOs(renovationDAO.getAll());
            JPAHelper.commitTransaction();
            return listOfRenovationReadOnlyDTOs;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public List<RenovationReadOnlyDTO> getRenovationsByCriteria(Map<String, Object> criteria) {
        try {
            JPAHelper.beginTransaction();
            List<RenovationReadOnlyDTO> readOnlyDTOS = renovationDAO.getByCriteria(criteria)
                    .stream()
                    .map(Mapper::mapRenovationToRenovationReadOnlyDTO)
                    .collect(Collectors.toList());
            JPAHelper.commitTransaction();
            return readOnlyDTOS;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }
}
