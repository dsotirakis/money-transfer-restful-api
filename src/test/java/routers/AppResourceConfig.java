package routers;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationPath("resources")
public class AppResourceConfig extends ResourceConfig {
    public AppResourceConfig() {
        packages("routers");
    }
}
