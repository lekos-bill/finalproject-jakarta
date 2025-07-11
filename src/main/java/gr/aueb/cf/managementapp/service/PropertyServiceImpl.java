package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;

import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dao.IPropertyDAO;
import gr.aueb.cf.managementapp.dto.*;
import gr.aueb.cf.managementapp.model.CostPerChange;
import gr.aueb.cf.managementapp.model.Property;
import gr.aueb.cf.managementapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gr.aueb.cf.managementapp.mapper.Mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ApplicationScoped
public class PropertyServiceImpl implements IPropertyService{
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyServiceImpl.class);

    private final IPropertyDAO propertyDAO;

    @Inject
    public PropertyServiceImpl(IPropertyDAO propertyDAO) {
        this.propertyDAO = propertyDAO;
    }

//ερωτηση γιατι χρειάζομαι και εδω throw entity invalid argument ,αφου έχουν περάσει το validation στον controller
//και μάλιστα γιατι γίνεται throw στην διαδικασία απο το persistence context στο dto αφου οτι ειναι περασμενο στην βαση έχει ελεχθει ηδη
    public PropertyReadOnlyDTO insertProperty(PropertyInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Property property = Mapper.mapPropertyInsertDTOToProperty(insertDTO);
            if (propertyDAO.findByField("atak", property.getAtak()).isPresent()) {
                throw new EntityAlreadyExistsException("Property", "Property already exists");
            }
            PropertyReadOnlyDTO propertyReadOnlyDTO = propertyDAO.insert(property)
                    .map(Mapper::mapPropertyToPropertyReadOnlyDTO)
                    .orElseThrow(() ->new EntityInvalidArgumentException("Property", "Property not inserted"));//δεν καταλαβαινω πιο exception βλεπουμε εδω
            JPAHelper.commitTransaction();
            LOGGER.info("Property with atak={} inserted", property.getAtak());
            return propertyReadOnlyDTO;
        } catch (EntityAlreadyExistsException | EntityInvalidArgumentException  e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Property with atak={} not inserted, Reason={}", insertDTO.atak(), e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public PropertyReadOnlyDTO updateProperty(PropertyUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            Property property = Mapper.mapPropertyUpdateDTOtoProperty(updateDTO);
            propertyDAO.getById(updateDTO.id()).orElseThrow(() -> new EntityNotFoundException("Property","Property not found"));

            PropertyReadOnlyDTO propertyReadOnlyDTO = propertyDAO.update(property)
                    .map(Mapper::mapPropertyToPropertyReadOnlyDTO)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Property", "Property not updated"));
            JPAHelper.commitTransaction();
            LOGGER.info("Property with atak={} updated", property.getAtak());
            return propertyReadOnlyDTO;
        } catch (EntityInvalidArgumentException | EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Property with atak={} not updated, Reason={}", updateDTO.atak(), e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public void remove(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            propertyDAO.getById(id).orElseThrow(() -> new EntityNotFoundException("Property", "Property not found"));
            propertyDAO.delete(id);
            JPAHelper.commitTransaction();
            LOGGER.info("Property with id={} deleted", id);
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Property with id:{} not deleted", id);
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public PropertyReadOnlyDTO getPropertyByAtak(String atak) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            PropertyReadOnlyDTO propertyReadOnlyDTO = propertyDAO.findByField("atak", atak)
                    .map(Mapper::mapPropertyToPropertyReadOnlyDTO)
                    .orElseThrow(() -> new EntityNotFoundException("Property", "Property not found"));

            JPAHelper.commitTransaction();
            LOGGER.info("Property with atak={} returned to controller", atak);
            return propertyReadOnlyDTO;
        } catch (EntityNotFoundException e) {
            LOGGER.error("Error. Property with atak={} not returned", atak);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }
// Αυτο θα μπει στο service του owner και θα καλειτε με εναν controller
    //απο το property
//    public Optional<List<OwnerReadOnlyDTO>> getOwnersByPropertyId(Long id) {
//        JPAHelper.beginTransaction();
//        Optional<Property> property= propertyDAO.findByField("atak", id);
//        Owner owner = property.get().getOwner();
//        List<Owner> listOfOwners =  ;
//
////        Optional.of(Mapper.mapOwnerToOwnerReadOnlyDTO(owner))
//        JPAHelper.commitTransaction();
//        LOGGER.info("Owner");
//        return ownerReadOnlyDTO;
//
//
//
//
//        JPAHelper.commitTransaction();
//    }

    public List<PropertyReadOnlyDTO> getAllProperties() {
        try {
            JPAHelper.beginTransaction();
            List<PropertyReadOnlyDTO> listOfPropertyReadOnlyDTOs = Mapper.mapPropertiesToListOfPropertyReadOnlyDTOs(propertyDAO.getAll());
            JPAHelper.commitTransaction();
            return listOfPropertyReadOnlyDTOs;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public List<PropertyReadOnlyDTO> getPropertiesByCriteria(Map<String, Object> criteria) {
        try {
            JPAHelper.beginTransaction();
            List<PropertyReadOnlyDTO> readOnlyDTOS = propertyDAO.getByCriteria(criteria)
                    .stream()
                    .map(Mapper::mapPropertyToPropertyReadOnlyDTO)
                    .collect(Collectors.toList());
            JPAHelper.commitTransaction();
            return readOnlyDTOS;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }



    public List<CostPerChangeReadOnlyDTO> getTotalCostsPerProperty() {
        JPAHelper.beginTransaction();
        List<CostPerChange> list = Mapper
                .mapListOfObjectsToToCostPerDamagePerProperty
                        (propertyDAO.getCostOfDamagesPerProperty(), propertyDAO.count());

        return list.stream().map(Mapper::mapCostPerChangeToCostPerChangeReadOnlyDTO).collect(Collectors.toList());

    }



}
