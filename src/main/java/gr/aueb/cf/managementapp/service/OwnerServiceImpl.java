package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dao.IDamageDAO;
import gr.aueb.cf.managementapp.dao.IOwnerDAO;
import gr.aueb.cf.managementapp.dao.IPropertyDAO;
import gr.aueb.cf.managementapp.dto.*;
import gr.aueb.cf.managementapp.mapper.Mapper;
import gr.aueb.cf.managementapp.model.Damage;
import gr.aueb.cf.managementapp.model.Owner;
import gr.aueb.cf.managementapp.model.Property;
import gr.aueb.cf.managementapp.model.Technician;
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
public class OwnerServiceImpl implements IOwnerService{

    private static final Logger LOGGER = LoggerFactory.getLogger(DamageServiceImpl.class);

    private final IOwnerDAO ownerDAO;
    private final IPropertyDAO propertyDAO;

    @Inject
    public OwnerServiceImpl(IOwnerDAO ownerDAO, IPropertyDAO propertyDAO) {
        this.ownerDAO = ownerDAO;
        this.propertyDAO= propertyDAO;
    }

    @Override
    public OwnerReadOnlyDTO insertOwnerToProperty(OwnerInsertDTO insertDTO, Long id) throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Owner owner = Mapper.mapOwnerInsertDTOToOwner(insertDTO);
            Property property = propertyDAO.getById(id).get();
            owner.addProperty(property);
            property.addOwner(owner);

            if (ownerDAO.findByField("cell", owner.getCell()).isPresent()) {
                throw new EntityAlreadyExistsException("Owner", "Owner already exists");
            }
            OwnerReadOnlyDTO ownerReadOnlyDTO = ownerDAO.insert(owner)
                    .map(Mapper::mapOwnerToOwnerReadOnlyDTO)
                    .orElseThrow(() ->new EntityInvalidArgumentException("Property", "Property not inserted"));//δεν καταλαβαινω πιο exception βλεπουμε εδω
            JPAHelper.commitTransaction();
            LOGGER.info("Technician with cell={} inserted", owner.getCell());
            return ownerReadOnlyDTO;
        } catch (EntityAlreadyExistsException | EntityInvalidArgumentException  e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Technician with cell={} not inserted, Reason={}", insertDTO.cell(), e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public OwnerReadOnlyDTO insertOwner(OwnerInsertDTO insertDTO) throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Owner owner = Mapper.mapOwnerInsertDTOToOwner(insertDTO);


            if (ownerDAO.findByField("cell", owner.getCell()).isPresent()) {
                throw new EntityAlreadyExistsException("Owner", "Owner already exists");
            }
            OwnerReadOnlyDTO ownerReadOnlyDTO = ownerDAO.insert(owner)
                    .map(Mapper::mapOwnerToOwnerReadOnlyDTO)
                    .orElseThrow(() ->new EntityInvalidArgumentException("Property", "Property not inserted"));//δεν καταλαβαινω πιο exception βλεπουμε εδω
            JPAHelper.commitTransaction();
            LOGGER.info("Technician with cell={} inserted", owner.getCell());
            return ownerReadOnlyDTO;
        } catch (EntityAlreadyExistsException | EntityInvalidArgumentException  e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Technician with cell={} not inserted, Reason={}", insertDTO.cell(), e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public OwnerReadOnlyDTO updateOwner(OwnerUpdateDTO updateDTO) throws EntityInvalidArgumentException, EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            Owner owner = Mapper.mapOwnerUpdateDTOToOwner(updateDTO);
            ownerDAO.getById(updateDTO.id()).orElseThrow(() -> new EntityNotFoundException("Owner","Owner not found"));

            OwnerReadOnlyDTO ownerReadOnlyDTO = ownerDAO.update(owner)
                    .map(Mapper::mapOwnerToOwnerReadOnlyDTO)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Owner", "Owner not updated"));
            JPAHelper.commitTransaction();
            LOGGER.info("Owner with cell={} updated", owner.getCell());
            return ownerReadOnlyDTO;
        } catch (EntityInvalidArgumentException | EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Owner with cell={} not updated, Reason={}", updateDTO.cell(), e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public void remove(Long ownerId) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            Owner owner = ownerDAO.getById(ownerId).orElseThrow(() -> new EntityNotFoundException("Owner", "Owner not found"));

            owner.setActive(false);
            Optional<Property> property =   Optional.of(owner.getProperty());
            if (property.isPresent()) {
                property.get().removeOwner(owner);
            }
            owner.setProperty(null);
            JPAHelper.commitTransaction();
            LOGGER.info("Owner with id={} deleted", ownerId);
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Owner with id:{} not deleted", ownerId);
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<OwnerReadOnlyDTO> getAllOwners() {
        try {
            JPAHelper.beginTransaction();
            List<OwnerReadOnlyDTO> listOfOwnerReadOnlyDTOs = Mapper.mapOwnersToListOfOwnerReadOnlyDTOs(ownerDAO.getAll());
            JPAHelper.commitTransaction();
            return listOfOwnerReadOnlyDTOs;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<OwnerReadOnlyDTO> getOwnersByCriteria(Map<String, Object> criteria) {
        JPAHelper.beginTransaction();
        List<OwnerReadOnlyDTO> readOnlyDTOS = ownerDAO.getByCriteria(criteria)
                .stream()
                .map(Mapper::mapOwnerToOwnerReadOnlyDTO)
                .collect(Collectors.toList());
        JPAHelper.commitTransaction();
        return readOnlyDTOS;
    }
}
