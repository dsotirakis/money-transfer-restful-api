import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import repositories.InMemoryDatabase;
import repositories.RepositoryGenerator;
import routers.AccountRouter;
import routers.TransactionRouter;
import routers.UserRouter;

/**
 * This is the main class of the project, responsible for the connection between the database and the server, as well as
 * the API functionality.
 */
public class MoneyTransferApp {

    /**
     * This method is responsible for starting the jetty server for the API to post and listen to.
     *
     * @throws Exception in case something goes wrong when the server starts.
     */
    private static void start() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(1);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                AccountRouter.class.getCanonicalName() + "," +
                        UserRouter.class.getCanonicalName() + "," +
                        TransactionRouter.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }

    public static void main(String[] args) throws Exception {
        InMemoryDatabase.generateData();
        new RepositoryGenerator();
        start();
    }
}
