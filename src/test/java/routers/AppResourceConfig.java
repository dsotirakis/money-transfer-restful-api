package routers;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import repositories.InMemoryDatabase;
import repositories.RepositoryGenerator;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("resources")
public class AppResourceConfig extends ResourceConfig {

    private static Server jettyServer;

    AppResourceConfig() {
        packages("routers");
    }

    static void setUp() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(1);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                TransactionRouter.class.getCanonicalName() + "," +
                UserRouter.class.getCanonicalName() + "," +
                AccountRouter.class.getCanonicalName());

        InMemoryDatabase.generateData();
        new RepositoryGenerator();
        jettyServer.start();
    }

    static void tearDown() throws Exception {
        jettyServer.stop();
    }
}
