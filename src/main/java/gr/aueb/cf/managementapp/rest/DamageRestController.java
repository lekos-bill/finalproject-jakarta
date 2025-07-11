package gr.aueb.cf.managementapp.rest;


import gr.aueb.cf.managementapp.core.enums.RoleType;
import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dto.*;

import gr.aueb.cf.managementapp.model.Damage;
import gr.aueb.cf.managementapp.service.IDamageService;

import gr.aueb.cf.managementapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/damages")
public class DamageRestController {



    private final IDamageService damageService;

    @Inject
    public DamageRestController(IDamageService damageService) {
        this.damageService = damageService;
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll( @Context SecurityContext securityContext) {
        if (securityContext.isUserInRole("OWNER")) {
            List<DamageReadOnlyDTO> damageReadOnlyDTOS = damageService.getAllDamages();
            return Response.status(Response.Status.OK).entity(damageReadOnlyDTOS).build();
        } else {
            return Response.status(Response.Status.OK).entity(securityContext.getUserPrincipal()).build();
        }
    }


    @POST
    @Path("/{propertyId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDamageToProperty(DamageInsertDTO insertDTO, @Context UriInfo uriInfo, @PathParam("propertyId") Long propertyId) throws EntityInvalidArgumentException, EntityAlreadyExistsException {
        List<String> errors = ValidatorUtil.validateDTO(insertDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Damage", String.join("\n", errors));
        }
        DamageReadOnlyDTO DamageReadOnlyDTO = damageService.insertDamageToProperty(insertDTO, propertyId);
        URI newResourceUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(DamageReadOnlyDTO.id()))
                .build();
        return Response.created(newResourceUri).entity(DamageReadOnlyDTO).build();
    }

    @PUT
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDamage(DamageUpdateDTO damageUpdateDTO, @Context UriInfo uriInfo) throws EntityInvalidArgumentException, EntityNotFoundException {
        List<String> errors = ValidatorUtil.validateDTO(damageUpdateDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Property", String.join("\n", errors));
        }
        DamageReadOnlyDTO damageReadOnlyDTO = damageService.updateDamage(damageUpdateDTO);
        URI newResourceUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(damageReadOnlyDTO.id()))
                .build();
        return Response.created(newResourceUri).entity(damageReadOnlyDTO).build();
    }

    @DELETE
    @Path("/{propertyId}/{damageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeDamage(@PathParam("propertyId") Long propertyId, @PathParam("propertyId") Long damageId) throws  EntityNotFoundException {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("id", damageId);
        DamageReadOnlyDTO damageReadOnlyDTO = damageService.getDamagesByCriteria(criteria).get(0);
        damageService.remove(propertyId, damageId);

        return Response.status(Response.Status.OK).entity(damageReadOnlyDTO).build();
    }


}
