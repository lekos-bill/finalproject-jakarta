package gr.aueb.cf.managementapp.rest;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dto.*;
import gr.aueb.cf.managementapp.mapper.Mapper;
import gr.aueb.cf.managementapp.model.Owner;
import gr.aueb.cf.managementapp.service.IOwnerService;
import gr.aueb.cf.managementapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/owners")
public class OwnerRestController {
    private final IOwnerService ownerService;

    @Inject
    public OwnerRestController(IOwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GET
    @Path("/{propertyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnersByProperty(@PathParam("propertyId") Long propertyId, @Context SecurityContext securityContext) {

        System.out.println(securityContext.getUserPrincipal());
        if (securityContext.isUserInRole("OWNER")) {
            Map<String, Object> map = new HashMap<>();
            map.put("property.id", propertyId);
            List<OwnerReadOnlyDTO> owners = ownerService.getOwnersByCriteria(map);
            return Response.status(Response.Status.OK).entity(owners).build();
        } else {
            return Response.status(Response.Status.OK).entity("No").build();
        }
    }

    @POST
    @Path("{propertyId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOwner(@PathParam("propertyId") Long propertyId, OwnerInsertDTO insertDTO, @Context UriInfo uriInfo) throws EntityInvalidArgumentException, EntityAlreadyExistsException {
        List<String> errors = ValidatorUtil.validateDTO(insertDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Owner", String.join("\n", errors));
        }
        OwnerReadOnlyDTO ownerReadOnlyDTO = ownerService.insertOwnerToProperty(insertDTO, propertyId);
        URI newResourceUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(ownerReadOnlyDTO.id()))
                .build();
        return Response.created(newResourceUri).entity(ownerReadOnlyDTO).build();
    }

    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertOwner(OwnerInsertDTO insertDTO, @Context UriInfo uriInfo) throws EntityInvalidArgumentException, EntityAlreadyExistsException {
        List<String> errors = ValidatorUtil.validateDTO(insertDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Owner", String.join("\n", errors));
        }
        OwnerReadOnlyDTO ownerReadOnlyDTO = ownerService.insertOwner(insertDTO);
        URI newResourceUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(ownerReadOnlyDTO.id()))
                .build();
        return Response.created(newResourceUri).entity(ownerReadOnlyDTO).build();
    }

    @PUT
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOwner(OwnerUpdateDTO ownerUpdateDTO, @Context UriInfo uriInfo) throws EntityInvalidArgumentException, EntityNotFoundException {
        List<String> errors = ValidatorUtil.validateDTO(ownerUpdateDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Owner", String.join("\n", errors));
        }
        OwnerReadOnlyDTO OwnerReadOnlyDTO = ownerService.updateOwner(ownerUpdateDTO);
        URI newResourceUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(ownerUpdateDTO.id()))
                .build();
        return Response.created(newResourceUri).entity(ownerUpdateDTO).build();
    }

    @DELETE
    @Path("/{ownerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProperty(@PathParam("ownerId") Long ownerId) throws  EntityNotFoundException {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("id", ownerId);
        OwnerReadOnlyDTO ownerReadOnlyDTO = ownerService.getOwnersByCriteria(criteria).get(0);
        ownerService.remove(ownerId);

        return Response.status(Response.Status.OK).entity(ownerReadOnlyDTO).build();
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<OwnerReadOnlyDTO> propertyReadOnlyDTOS = ownerService.getAllOwners();
        return Response.status(Response.Status.OK).entity(propertyReadOnlyDTOS).build();
    }

    @POST
    @Path("/criteria")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPropertyByCriteria(PropertyFiltersDTO dto) {
        Map<String, Object> criteria = Mapper.mapPropertyFilfersDTOToCriteria(dto);
        List<OwnerReadOnlyDTO> ownersByCriteria = ownerService.getOwnersByCriteria(criteria);
        return Response.status(Response.Status.OK).entity(ownersByCriteria).build();
    }
}
