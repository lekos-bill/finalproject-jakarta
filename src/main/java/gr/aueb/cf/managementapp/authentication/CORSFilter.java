package gr.aueb.cf.managementapp.authentication;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
        String origin = req.getHeaderString("Origin");
        if ("http://localhost:5173".equals(origin)) {
            res.getHeaders().putSingle("Access-Control-Allow-Origin", origin); // always set for allowed origin
            res.getHeaders().putSingle("Vary", "Origin");

        }

    }
}
