package gr.aueb.cf.managementapp.authentication;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION) // run early
public class PreflightCorsFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext req) {
        String origin = req.getHeaderString("Origin");
        String acrm   = req.getHeaderString("Access-Control-Request-Method");
        String acrh   = req.getHeaderString("Access-Control-Request-Headers");

        // Handle real CORS preflights
        if ("OPTIONS".equalsIgnoreCase(req.getMethod()) && acrm != null) {
            if ("http://localhost:5173".equals(origin)) {
                req.abortWith(Response.noContent() // 204
                        .header("Access-Control-Allow-Origin", origin)
                        .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,PATCH,OPTIONS")
                        .header("Access-Control-Allow-Headers", (acrh != null && !acrh.isBlank()) ? acrh : "authorization, content-type")
                        .header("Access-Control-Max-Age", "3600")
                        .header("Vary", "Origin")
                        // .header("Access-Control-Allow-Credentials", "true") // add if you use cookies
                        .build());
            } else {
                req.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            }
        }
    }
}
