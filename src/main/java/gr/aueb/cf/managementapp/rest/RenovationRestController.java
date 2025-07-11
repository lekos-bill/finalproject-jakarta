package gr.aueb.cf.managementapp.rest;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dto.*;
import gr.aueb.cf.managementapp.service.IDamageService;
import gr.aueb.cf.managementapp.service.IRenovationService;
import gr.aueb.cf.managementapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/renovations")
public class RenovationRestController {

    private final IRenovationService renovationService;

    @Inject
    public RenovationRestController(IRenovationService renovationService) {
        this.renovationService = renovationService;
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<RenovationReadOnlyDTO> renovationReadOnlyDTOS = renovationService.getAllRenovations();
        return Response.status(Response.Status.OK).entity(renovationReadOnlyDTOS).build();
    }


    @POST
    @Path("/{propertyId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRenovationToProperty(RenovationInsertDTO insertDTO, @Context UriInfo uriInfo, @PathParam("propertyId") Long propertyId) throws EntityInvalidArgumentException, EntityAlreadyExistsException {
        List<String> errors = ValidatorUtil.validateDTO(insertDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Renovation", String.join("\n", errors));
        }
        RenovationReadOnlyDTO RenovationReadOnlyDTO = renovationService.insertRenovationToProperty(insertDTO, propertyId);
        URI newResourceUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(RenovationReadOnlyDTO.id()))
                .build();
        return Response.created(newResourceUri).entity(RenovationReadOnlyDTO).build();
    }

    @PUT
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRenovation(RenovationUpdateDTO renovationUpdateDTO, @Context UriInfo uriInfo) throws EntityInvalidArgumentException, EntityNotFoundException {
        List<String> errors = ValidatorUtil.validateDTO(renovationUpdateDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Property", String.join("\n", errors));
        }
        RenovationReadOnlyDTO renovationReadOnlyDTO = renovationService.updateRenovation(renovationUpdateDTO);
        URI newResourceUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(renovationReadOnlyDTO.id()))
                .build();
        return Response.created(newResourceUri).entity(renovationReadOnlyDTO).build();
    }

    @DELETE
    @Path("/{propertyId}/{renovationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeRenovation(@PathParam("propertyId") Long propertyId, @PathParam("renovationId") Long renovationId) throws  EntityNotFoundException {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("id", renovationId);
        RenovationReadOnlyDTO renovationReadOnlyDTO = renovationService.getRenovationsByCriteria(criteria).get(0);
        renovationService.remove(propertyId, renovationId);

        return Response.status(Response.Status.OK).entity(renovationReadOnlyDTO).build();
    }
}
