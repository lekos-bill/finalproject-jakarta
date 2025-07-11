package gr.aueb.cf.managementapp.rest;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dto.PropertyFiltersDTO;
import gr.aueb.cf.managementapp.dto.PropertyInsertDTO;
import gr.aueb.cf.managementapp.dto.PropertyReadOnlyDTO;
import gr.aueb.cf.managementapp.dto.PropertyUpdateDTO;
import gr.aueb.cf.managementapp.mapper.Mapper;
import gr.aueb.cf.managementapp.service.IPropertyService;
import gr.aueb.cf.managementapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import javax.print.attribute.standard.Media;
import javax.swing.plaf.PanelUI;
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

    @Inject
    public PropertyRestController(IPropertyService propertyService) {
        this.propertyService = propertyService;
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
}
