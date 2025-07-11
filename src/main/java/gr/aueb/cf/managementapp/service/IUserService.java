package gr.aueb.cf.managementapp.service;

import gr.aueb.cf.managementapp.core.exceptions.AppServerException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dto.UserInsertDTO;
import gr.aueb.cf.managementapp.dto.UserReadOnlyDTO;

public interface IUserService {
    UserReadOnlyDTO insertUser(UserInsertDTO dto) throws AppServerException;
    UserReadOnlyDTO getUserByUsername(String username) throws EntityNotFoundException;
    boolean isUserValid(String username, String password);
    boolean isEmailExists(String username);

}
