package routers;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import models.Account;
import repositories.InMemoryDatabase;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class AccountRouterTest extends JerseyTest {

    private static Server jettyServer;

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(AccountRouter.class);
    }

    @BeforeAll
    static void setup() throws Exception {
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
                AccountRouter.class.getCanonicalName() + "," +
                        TransactionRouter.class.getCanonicalName());

        InMemoryDatabase.generateData();
        jettyServer.start();
    }

    @Test
    void getAll() {
        Response response = getRequest("getAll").get();
        assertEquals("should return status 200", 200, response.getStatus());
        assertNotNull("Should return user list", response.getEntity().toString());
    }

    @Test
    void getById() {
        Response response = getRequest("3").get();
        assertEquals("should return status 200", 200, response.getStatus());
        assertNotNull("Should return user list", response.getEntity().toString());
    }

    @Test
    void getById_DoesntExist() {
        Response response = getRequest("4").get();
        assertEquals("should return status 204", 204, response.getStatus());
    }

    @Test
    void getByUsername() {
        Response response = getRequest("uname/name3").get();
        assertEquals("should return status 200", 200, response.getStatus());
        assertNotNull("Should return user list", response.getEntity().toString());
    }

    @Test
    void getByUsername_usernameDoesntExist() {
        Response response = getRequest("uname/name5").get();
        assertEquals("should return status 204", 204, response.getStatus());
        assertNotNull("Should return user list", response.getEntity().toString());

    }

    @Test
    void createAccount() {
        Account newAccount = new Account("name4", "password4", 100.0);
        Response response = getRequest("createAccount")
                .post(Entity.entity(newAccount, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 201", 201, response.getStatus());
        assertNotNull("Should return new Account", response.getEntity());
    }

    @Test
    void deleteAccount(){
        Response response = getRequest("1").delete();
        assertEquals("Should return status 204", 204, response.getStatus());
    }

    @Test
    void deleteAccount_accountDoesntExist() {
        Response response = getRequest("4").delete();
        assertEquals("Should return status 404", 404, response.getStatus());
    }

    @Test
    void updateAccount() {
        Account accountToUpdate = new Account("name2", "password2", 101.0);
        Response response = getRequest("2").put(Entity.entity(accountToUpdate, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 200", 204, response.getStatus());
    }

    @Test
    void updateAccount_accountDoesntExist() {
        Account accountToUpdate = new Account("name1", "password1", 101.0);
        Response response = getRequest("6").put(Entity.entity(accountToUpdate, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 404", 404, response.getStatus());
    }

    @AfterAll
    static void terminate() throws Exception {
        jettyServer.stop();
    }

    Invocation.Builder getRequest(String path) {
        Client client = ClientBuilder.newClient(new AppResourceConfig());
        WebTarget webTarget = client.target("http://localhost:8080/").path("accounts/" + path);
        return webTarget.request();
    }
}
