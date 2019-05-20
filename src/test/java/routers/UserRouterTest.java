package routers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

import models.User;
import repositories.InMemoryDatabase;

public class UserRouterTest extends JerseyTest {

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
                UserRouter.class.getCanonicalName());

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
        System.out.println(response.getEntity().toString());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    @Test
    void getById_DoesntExist() {
        Response response = getRequest("4").get();
        assertEquals("should return status 204", 204, response.getStatus());
    }

    @Test
    void getByName() {
        Response response = getRequest("name/name3").get();
        assertEquals("should return status 200", 200, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    @Test
    void getByName_nameDoesntExist() {
        Response response = getRequest("name/name5").get();
        assertEquals("should return status 204", 204, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    @Test
    void getBySurname() {
        Response response = getRequest("sname/surname3").get();
        assertEquals("should return status 200", 200, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    @Test
    void getBySurname_surnameDoesntExist() {
        Response response = getRequest("sname/surname5").get();
        assertEquals("should return status 204", 204, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    @Test
    void getByEmail() {
        Response response = getRequest("mail/name3@gmail.com").get();
        assertEquals("should return status 200", 200, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    @Test
    void getByEmail_emailDoesntExist() {
        Response response = getRequest("mail/name4@gmail.com").get();
        assertEquals("should return status 204", 204, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    @Test
    void createUser() {
        User newUser = new User("name4", "surname4", "name5@gmail.com");
        Response response = getRequest("createUser")
                .post(Entity.entity(newUser, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 201", 201, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity());
    }

    @Test
    void deleteUser(){
        Response response = getRequest("1").delete();
        assertEquals("Should return status 204", 204, response.getStatus());
    }

    @Test
    void deleteUser_userDoesntExist() {
        Response response = getRequest("4").delete();
        assertEquals("Should return status 404", 404, response.getStatus());
    }

    @Test
    void updateAccount() {
        User userToUpdate = new User("name2", "surname2", "name2@hotmail.com");
        Response response = getRequest("2").put(Entity.entity(userToUpdate, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 200", 204, response.getStatus());
    }

    @Test
    void updateAccount_accountDoesntExist() {
        User userToUpdate = new User("name1", "surname1", "name1@hotmail.com");
        Response response = getRequest("6").put(Entity.entity(userToUpdate, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 404", 404, response.getStatus());
    }

    @AfterAll
    static void terminate() throws Exception {
        jettyServer.stop();
    }

    Invocation.Builder getRequest(String path) {
        Client client = ClientBuilder.newClient(new AppResourceConfig());
        WebTarget webTarget = client.target("http://localhost:8080/").path("users/" + path);
        return webTarget.request();
    }

}
