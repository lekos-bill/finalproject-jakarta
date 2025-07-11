package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.AppServerException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dao.IUserDAO;
import gr.aueb.cf.managementapp.dto.UserInsertDTO;
import gr.aueb.cf.managementapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.managementapp.mapper.Mapper;
import gr.aueb.cf.managementapp.model.User;
import gr.aueb.cf.managementapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class UserServiceImpl implements IUserService{

    private final IUserDAO userDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    public UserServiceImpl(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    public UserReadOnlyDTO insertUser(UserInsertDTO dto) throws AppServerException {
        try {
            JPAHelper.beginTransaction();
            User user = Mapper.mapToUser(dto);

            UserReadOnlyDTO readOnlyDTO = userDAO.insert(user)
                    .map(Mapper::mapToUserReadOnlyDTO)
                    .orElseThrow(() -> new AppServerException("User", "User with username: " + dto.getUsername() + "not inserted"));
            JPAHelper.commitTransaction();
            LOGGER.info("User with username= {} inserted", dto.getUsername());
            return readOnlyDTO;
        } catch (AppServerException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("User with username={} not inserted.", dto.getUsername(), e);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public UserReadOnlyDTO getUserByUsername(String username) throws EntityNotFoundException {
            try {
                JPAHelper.beginTransaction();

                UserReadOnlyDTO readOnlyDTO = userDAO.getByUsername(username)
                        .map(Mapper::mapToUserReadOnlyDTO)
                        .orElseThrow(() -> new EntityNotFoundException("User", "User with username: " + username + "not found."));
                JPAHelper.commitTransaction();
                return readOnlyDTO;
            } catch (EntityNotFoundException e) {
                LOGGER.warn("User with username={} not found.", username, e);
                throw e;
            } finally {
                JPAHelper.closeEntityManager();
            }
    }

    @Override
    public boolean isUserValid(String username, String password) {
        try {
            JPAHelper.beginTransaction();

            boolean isValid = userDAO.isUserValid(username, password);
            JPAHelper.commitTransaction();
            return isValid;
        }
        finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public boolean isEmailExists(String username) {
        try {
            JPAHelper.beginTransaction();

            boolean isEMailExists = userDAO.isEmailExists(username);
            JPAHelper.commitTransaction();
            return isEMailExists;
        }
        finally {
            JPAHelper.closeEntityManager();
        }
    }
}
