package routers;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import repositories.InMemoryDatabase;
import repositories.RepositoryGenerator;

/**
 * This class is responsible for configuring the server and it's properties for all the routers
 * tests. It contains static methods so there are accessible by all instances at the same time.
 */
class RouterTestsConfig extends ResourceConfig {

    private static Server jettyServer;

    /**
     * This method is responsible for setting up the jetty server.
     *
     * @throws Exception in case the jetty server can't start.
     */
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

    /**
     * This method is responsible for terminating the jetty server.
     *
     * @throws Exception in case the server can't be terminated.
     */
    static void tearDown() throws Exception {
        jettyServer.stop();
    }
}
