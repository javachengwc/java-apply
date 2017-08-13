package com.app.health;

import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Controller
@Produces(MediaType.APPLICATION_JSON)
@Path("/app/health")
public class HealthResource {

    @GET
    @Path("/")
    public Object appHealth() {
        return HealthCheckManager.getInstance().run();
    }
}
