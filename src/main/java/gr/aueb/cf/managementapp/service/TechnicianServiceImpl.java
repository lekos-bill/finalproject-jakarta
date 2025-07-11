package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dao.IDamageDAO;
import gr.aueb.cf.managementapp.dao.ITechnicianDAO;
import gr.aueb.cf.managementapp.dto.*;
import gr.aueb.cf.managementapp.mapper.Mapper;
import gr.aueb.cf.managementapp.model.Damage;
import gr.aueb.cf.managementapp.model.Property;
import gr.aueb.cf.managementapp.model.Technician;
import gr.aueb.cf.managementapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class TechnicianServiceImpl implements ITechnicianService{

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyServiceImpl.class);

    private final ITechnicianDAO technicianDAO;
    private final IDamageDAO damageDAO;

    @Inject
    public TechnicianServiceImpl(ITechnicianDAO technicianDAO, IDamageDAO damageDAO) {
        this.technicianDAO = technicianDAO;
        this.damageDAO = damageDAO;
    }

    @Override
    public TechnicianReadOnlyDTO insertTechnician(TechnicianInsertDTO insertDTO) throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Technician technician = Mapper.mapTechnicianInsertDTOToTechnician(insertDTO);
            if (technicianDAO.findByField("cell", technician.getCell()).isPresent()) {
                throw new EntityAlreadyExistsException("Technician", "Technician already exists");
            }
            TechnicianReadOnlyDTO technicianReadOnlyDTO = technicianDAO.insert(technician)
                    .map(Mapper::mapTechnicianToTechnicianReadOnlyDTO)
                    .orElseThrow(() ->new EntityInvalidArgumentException("Property", "Property not inserted"));//δεν καταλαβαινω πιο exception βλεπουμε εδω
            JPAHelper.commitTransaction();
            LOGGER.info("Technician with cell={} inserted", technician.getCell());
            return technicianReadOnlyDTO;
        } catch (EntityAlreadyExistsException | EntityInvalidArgumentException  e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Technician with cell={} not inserted, Reason={}", insertDTO.cell(), e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public TechnicianReadOnlyDTO updateTechnician(TechnicianUpdateDTO updateDTO) throws EntityInvalidArgumentException, EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            Technician technician = Mapper.mapTechnicianUpdateDTOToTechnician(updateDTO);
            technicianDAO.getById(updateDTO.id()).orElseThrow(() -> new EntityNotFoundException("Technician","Technician not found"));

            TechnicianReadOnlyDTO technicianReadOnlyDTO = technicianDAO.update(technician)
                    .map(Mapper::mapTechnicianToTechnicianReadOnlyDTO)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Technician", "Technician not updated"));
            JPAHelper.commitTransaction();
            LOGGER.info("Technician with cell={} updated", technician.getCell());
            return technicianReadOnlyDTO;
        } catch (EntityInvalidArgumentException | EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Technician with cell={} not updated, Reason={}", updateDTO.cell(), e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public void remove(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            technicianDAO.getById(id).orElseThrow(() -> new EntityNotFoundException("Technician", "Technician not found"));
            technicianDAO.delete(id);
            JPAHelper.commitTransaction();
            LOGGER.info("Technician with id={} deleted", id);
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Technician with id:{} not deleted", id);
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<TechnicianReadOnlyDTO> getAllTechnicians() {
        try {
            JPAHelper.beginTransaction();
            List<TechnicianReadOnlyDTO> listOfTechnicianReadOnlyDTOs = Mapper.mapTechniciansToTechnicianReadOnlyDTOs(technicianDAO.getAll());
            JPAHelper.commitTransaction();
            return listOfTechnicianReadOnlyDTOs;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<TechnicianReadOnlyDTO> getTechniciansByCriteria(Map<String, Object> criteria) {
        try {
            JPAHelper.beginTransaction();
            List<TechnicianReadOnlyDTO> readOnlyDTOS = technicianDAO.getByCriteria(criteria)
                    .stream()
                    .map(Mapper::mapTechnicianToTechnicianReadOnlyDTO)
                    .collect(Collectors.toList());
            JPAHelper.commitTransaction();
            return readOnlyDTOS;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public TechnicianReadOnlyDTO getTechnicianyByPhoneNumber(String cell) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            TechnicianReadOnlyDTO technicianReadOnlyDTO = technicianDAO.findByField("cell", cell)
                    .map(Mapper::mapTechnicianToTechnicianReadOnlyDTO)
                    .orElseThrow(() -> new EntityNotFoundException("Property", "Property not found"));

            JPAHelper.commitTransaction();
            LOGGER.info("Technician with cell:{} returned to controller", cell);
            return technicianReadOnlyDTO;
        } catch (EntityNotFoundException e) {
            LOGGER.error("Error.Technician with cell:{}  not returned", cell);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    public TechnicianReadOnlyDTO insertTechnicianToDamage(Long technicianId, Long damageId)
            throws  EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();
            Damage damage = damageDAO.findByField("id", damageId).get();

            Technician technician = technicianDAO.findByField("id",  technicianId).get();
            damage.setTechnician(technician);
            technician.addDamage(damage);

            TechnicianReadOnlyDTO technicianReadOnlyDTO = technicianDAO.insert(technician)
                    .map(Mapper::mapTechnicianToTechnicianReadOnlyDTO)
                    .orElseThrow(() ->new EntityInvalidArgumentException("Damage", "Damage not inserted"));//δεν καταλαβαινω πιο exception βλεπουμε εδω
            JPAHelper.commitTransaction();
            LOGGER.info("Technician with id={} inserted", technicianId);
            return technicianReadOnlyDTO;
        } catch (EntityInvalidArgumentException  e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Technician with id={} not inserted, Reason={}", technicianId , e.getCause());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }
}
