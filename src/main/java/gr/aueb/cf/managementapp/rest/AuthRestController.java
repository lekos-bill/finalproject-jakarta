package gr.aueb.cf.managementapp.rest;


import gr.aueb.cf.managementapp.authentication.AuthenticationProvider;
import gr.aueb.cf.managementapp.authentication.AuthenticationResponseDTO;
import gr.aueb.cf.managementapp.core.exceptions.AppServerException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotAuthorizedException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dto.UserInsertDTO;
import gr.aueb.cf.managementapp.dto.UserLoginDTO;
import gr.aueb.cf.managementapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.managementapp.security.JwtService;
import gr.aueb.cf.managementapp.service.IUserService;
import gr.aueb.cf.managementapp.validator.UserInputValidator;
import gr.aueb.cf.managementapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Path("auth")
public class AuthRestController {

    private final IUserService userService;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(UserInsertDTO userInsertDTO, @Context UriInfo uriInfo, @Context SecurityContext securityContext)
            throws EntityInvalidArgumentException, AppServerException {
        UserReadOnlyDTO userReadOnlyDTO;

        List<String> beanErrors = ValidatorUtil.validateDTO(userInsertDTO);
        if (!beanErrors.isEmpty()) {
            throw new EntityInvalidArgumentException("User", String.join(", ", beanErrors));
        }

        Map<String , String > otherErrors = UserInputValidator.validate(userInsertDTO);
        if (!otherErrors.isEmpty()) {
            throw new EntityInvalidArgumentException("User", otherErrors.toString());
        }

        userReadOnlyDTO = userService.insertUser(userInsertDTO);
        return Response.status(Response.Status.ACCEPTED).entity(securityContext.getUserPrincipal()).build();

    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserLoginDTO loginDTO, @Context Principal principal)
            throws EntityNotFoundException, EntityNotAuthorizedException {
        boolean isUserValid = authenticationProvider.authenticate(loginDTO);
        if (!isUserValid) {
            //return Response.status(Response.Status.UNAUTHORIZED).build();
            throw new EntityNotAuthorizedException("User", "User not authorized");
        }

        if (principal != null) {
            String username = principal.getName();
            if (loginDTO.username().equals(username)) {
                return Response.status(Response.Status.OK).entity("Already authenticated").build();
            }
        }

        UserReadOnlyDTO userReadOnlyDTO = userService.getUserByUsername(loginDTO.username());
        String role = userReadOnlyDTO.getRole();
        String token = jwtService.generateToken(loginDTO.username(), role);
        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO(token);
        return Response.status(Response.Status.OK).entity(authenticationResponseDTO).build();
    }

}
