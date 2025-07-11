package gr.aueb.cf.managementapp.authentication;

import gr.aueb.cf.managementapp.dto.UserLoginDTO;
import gr.aueb.cf.managementapp.service.IUserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import javax.swing.plaf.PanelUI;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
public class AuthenticationProvider {

    private final IUserService userService;

    public boolean authenticate(UserLoginDTO userLoginDTO) {
        return userService.isUserValid(userLoginDTO.username(), userLoginDTO.password());
    }


}
