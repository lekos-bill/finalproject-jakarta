package gr.aueb.cf.managementapp.rest;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dto.*;
import gr.aueb.cf.managementapp.mapper.Mapper;
import gr.aueb.cf.managementapp.service.IDamageService;
import gr.aueb.cf.managementapp.service.IOwnerService;
import gr.aueb.cf.managementapp.service.IPropertyService;
import gr.aueb.cf.managementapp.service.IRenovationService;
import gr.aueb.cf.managementapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import javax.print.attribute.standard.Media;
import javax.swing.plaf.PanelUI;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/properties")
public class PropertyRestController {

    private final IPropertyService propertyService;
    private final IDamageService damageService;
    private final IOwnerService ownerService;
    private final IRenovationService renovationService;
    @Inject
    public PropertyRestController(IPropertyService propertyService, IDamageService damageService, IOwnerService ownerService, IRenovationService renovationService) {
        this.propertyService = propertyService;
        this.damageService = damageService;
        this.ownerService = ownerService;
        this.renovationService = renovationService;
    }


    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProperty(PropertyInsertDTO insertDTO, @Context UriInfo uriInfo) throws EntityInvalidArgumentException, EntityAlreadyExistsException {
        List<String> errors = ValidatorUtil.validateDTO(insertDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Property", String.join("\n", errors));
        }
        PropertyReadOnlyDTO propertyReadOnlyDTO = propertyService.insertProperty(insertDTO);
        URI newResourceUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(propertyReadOnlyDTO.id()))
                .build();
        return Response.created(newResourceUri).entity(propertyReadOnlyDTO).build();
    }

    @PUT
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProperty(PropertyUpdateDTO propertyUpdateDTO, @Context UriInfo uriInfo) throws EntityInvalidArgumentException, EntityNotFoundException {
        List<String> errors = ValidatorUtil.validateDTO(propertyUpdateDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Property", String.join("\n", errors));
        }
        PropertyReadOnlyDTO propertyReadOnlyDTO = propertyService.updateProperty(propertyUpdateDTO);
        URI newResourceUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(propertyReadOnlyDTO.id()))
                .build();
        return Response.created(newResourceUri).entity(propertyReadOnlyDTO).build();
    }

    @DELETE
    @Path("/{propertyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProperty(@PathParam("propertyId") Long propertyId) throws  EntityNotFoundException {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("id", propertyId);
        PropertyReadOnlyDTO propertyReadOnlyDTO = propertyService.getPropertiesByCriteria(criteria).get(0);
        propertyService.remove(propertyId);

        return Response.status(Response.Status.OK).entity(propertyReadOnlyDTO).build();
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<PropertyReadOnlyDTO> propertyReadOnlyDTOS = propertyService.getAllProperties();
        return Response.status(Response.Status.OK).entity(propertyReadOnlyDTOS).build();
    }

    @POST
    @Path("/criteria")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPropertyByCriteria(PropertyFiltersDTO dto) {
        Map<String, Object> criteria = Mapper.mapPropertyFilfersDTOToCriteria(dto);
        List<PropertyReadOnlyDTO> propertiesByCriteria = propertyService.getPropertiesByCriteria(criteria);
        return Response.status(Response.Status.OK).entity(propertiesByCriteria).build();
    }

    @GET
    @Path("{propertyId}/damages")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDamagesPerProperty(@PathParam("propertyId") Long propertyId) {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("property.id", propertyId);
        List<DamageReadOnlyDTO> damageReadOnlyDTO = damageService.getDamagesByCriteria(criteria);

        return Response.status(Response.Status.OK).entity(damageReadOnlyDTO).build();
    }

    @POST
    @Path("{propertyId}/damages")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDamageToProperty(@PathParam("propertyId") Long propertyId, DamageInsertDTO damageInsertDTO) throws EntityInvalidArgumentException, EntityAlreadyExistsException {

        DamageReadOnlyDTO damageReadOnlyDTO = damageService.insertDamageToProperty(damageInsertDTO, propertyId);

        return Response.status(Response.Status.OK).entity(damageReadOnlyDTO).build();
    }

    @GET
    @Path("{propertyId}/renovations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRenovationsPerProperty(@PathParam("propertyId") Long propertyId) {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("property.id", propertyId);
        List<RenovationReadOnlyDTO> renovationReadOnlyDTOS = renovationService.getRenovationsByCriteria(criteria);

        return Response.status(Response.Status.OK).entity(renovationReadOnlyDTOS).build();
    }

    @GET
    @Path("{propertyId}/owners")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnersPerProperty(@PathParam("propertyId") Long propertyId) {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("property.id", propertyId);
        List<OwnerReadOnlyDTO> ownerReadOnlyDTOS = ownerService.getOwnersByCriteria(criteria);

        return Response.status(Response.Status.OK).entity(ownerReadOnlyDTOS).build();
    }

    @GET
    @Path("damages/${propertyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void insertDamageToProperty(@PathParam("propertyId") Long propertyId, @Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/api/damages/" + propertyId).forward(request, response);
    }




}
