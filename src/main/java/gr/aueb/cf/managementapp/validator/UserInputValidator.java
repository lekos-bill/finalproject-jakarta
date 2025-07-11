package gr.aueb.cf.managementapp.validator;

import gr.aueb.cf.managementapp.dao.IUserDAO;
import gr.aueb.cf.managementapp.dao.UserDAOImpl;
import gr.aueb.cf.managementapp.dto.UserInsertDTO;
import gr.aueb.cf.managementapp.service.IUserService;
import gr.aueb.cf.managementapp.service.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class UserInputValidator {
    private static final IUserDAO userDAO = new UserDAOImpl();
    private static final IUserService userService = new UserServiceImpl(userDAO);

    private UserInputValidator() {

    }

    public static <T extends UserInsertDTO> Map<String , String> validate(T dto) {
        Map<String , String > errors = new HashMap<>();

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            errors.put("confirmPassword", "Το password και το confirmedPassword δεν είναι ίδια");
        }

        if (userService.isEmailExists(dto.getUsername())) {
            errors.put("username", "Το e-mail/username υπάρχει ήδη");
        }

        return errors;
    }
}
