package gr.aueb.cf;

import gr.aueb.cf.managementapp.authentication.CORSFilter;
import gr.aueb.cf.managementapp.authentication.JwtAuthenticationFilter;
import gr.aueb.cf.managementapp.authentication.PreflightCorsFilter;
import gr.aueb.cf.managementapp.dao.DamageDAOImpl;
import gr.aueb.cf.managementapp.dao.IDamageDAO;
import gr.aueb.cf.managementapp.dao.IPropertyDAO;
import gr.aueb.cf.managementapp.dao.PropertyDAOImpl;
import gr.aueb.cf.managementapp.model.CostPerChange;
import gr.aueb.cf.managementapp.rest.*;
import gr.aueb.cf.managementapp.service.OwnerServiceImpl;
import gr.aueb.cf.managementapp.service.util.JPAHelper;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@ApplicationPath("/api")
public class ManagementApp extends Application {


}
