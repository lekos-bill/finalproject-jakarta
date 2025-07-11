package gr.aueb.cf.managementapp.dao;

import gr.aueb.cf.managementapp.model.User;

import java.util.Optional;

public interface IUserDAO extends IGenericDAO<User>{
    Optional<User> getByUsername(String username);
    boolean isUserValid(String username, String password);
    boolean isEmailExists(String username);
}
