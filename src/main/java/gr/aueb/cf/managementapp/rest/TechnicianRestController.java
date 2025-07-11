package gr.aueb.cf.managementapp.rest;

import gr.aueb.cf.managementapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.managementapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.managementapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.managementapp.dao.ITechnicianDAO;
import gr.aueb.cf.managementapp.dto.*;
import gr.aueb.cf.managementapp.service.IDamageService;
import gr.aueb.cf.managementapp.service.ITechnicianService;
import gr.aueb.cf.managementapp.validator.ValidatorUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import javax.print.attribute.standard.Media;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/technicians")
public class TechnicianRestController {

    private final ITechnicianService technicianService;

    @Inject
    public TechnicianRestController(ITechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TechnicianReadOnlyDTO> technicianReadOnlyDTOS = technicianService.getAllTechnicians();
        return Response.status(Response.Status.OK).entity(technicianReadOnlyDTOS).build();
    }

    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTechnician(TechnicianInsertDTO insertDTO, @Context UriInfo uriInfo) throws EntityInvalidArgumentException, EntityAlreadyExistsException {
        List<String> errors = ValidatorUtil.validateDTO(insertDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Technician", String.join("\n", errors));
        }
        TechnicianReadOnlyDTO technicianReadOnlyDTO = technicianService.insertTechnician(insertDTO);
        URI newResourceUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(technicianReadOnlyDTO.id()))
                .build();
        return Response.created(newResourceUri).entity(technicianReadOnlyDTO).build();
    }

    @PUT
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTechnician(TechnicianUpdateDTO technicianUpdateDTO, @Context UriInfo uriInfo) throws EntityInvalidArgumentException, EntityNotFoundException {
        List<String> errors = ValidatorUtil.validateDTO(technicianUpdateDTO);
        if (!errors.isEmpty()) {
            throw new EntityInvalidArgumentException("Technician", String.join("\n", errors));
        }
        TechnicianReadOnlyDTO technicianReadOnlyDTO = technicianService.updateTechnician(technicianUpdateDTO);
        URI newResourceUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(technicianReadOnlyDTO.id()))
                .build();
        return Response.created(newResourceUri).entity(technicianReadOnlyDTO).build();
    }

    @DELETE
    @Path("/{technicianId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTechnician(@PathParam("technicianId") Long technicianId) throws  EntityNotFoundException {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("id", technicianId);
        TechnicianReadOnlyDTO technicianReadOnlyDTO = technicianService.getTechniciansByCriteria(criteria).get(0);
        technicianService.remove(technicianId);

        return Response.status(Response.Status.OK).entity(technicianReadOnlyDTO).build();
    }

    @POST
    @Path("/{damageId}/{technicianId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTechnicianToDamage(@PathParam("damageId") Long damageId, @PathParam("technicianId") Long technicianId, @Context UriInfo uriInfo) throws EntityInvalidArgumentException, EntityAlreadyExistsException {
        TechnicianReadOnlyDTO technicianReadOnlyDTO = technicianService.insertTechnicianToDamage(technicianId, damageId);
        return Response.status(Response.Status.OK).entity(technicianReadOnlyDTO).build();
    }





}
