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

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import models.User;

/**
 * This test is responsible for testing the user router class. This is in fact the test class that tests the API
 * calls between the client and the server.
 */
public class UserRouterTest extends JerseyTest {

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(UserRouter.class);
    }

    @BeforeAll
    static void setup() throws Exception {
        RouterTestsConfig.setUp();
    }

    /**
     * This method is responsible for testing the getAll() method, which sends a GET request to the users repository,
     * in order to retrieve all the users.
     */
    @Test
    void getAll() {
        Response response = getRequest("getAll").get();
        assertEquals("should return status 200", 200, response.getStatus());
        assertNotNull("Should return user list", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the getById() method, which sends a GET request to the users repository,
     * in order to retrieve a user with a particular id.
     */
    @Test
    void getById() {
        Response response = getRequest("3").get();
        assertEquals("should return status 200", 200, response.getStatus());
        System.out.println(response.getEntity().toString());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the getById() method, which sends a GET request to the users repository,
     * in order to check that a particular id doesn't exist.
     */
    @Test
    void getById_DoesntExist() {
        Response response = getRequest("6").get();
        assertEquals("should return status 204", 204, response.getStatus());
    }

    /**
     * This method is responsible for testing the getByName() method, which sends a GET request to the users
     * repository, in order to retrieve a user with a particular name.
     */
    @Test
    void getByName() {
        Response response = getRequest("name/name3").get();
        assertEquals("should return status 200", 200, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the getByName() method, which sends a GET request to the users
     * repository, in order to check that a particular name doesn't exist.
     */
    @Test
    void getByName_nameDoesntExist() {
        Response response = getRequest("name/name7").get();
        assertEquals("should return status 204", 204, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the getByUsername() method, which sends a GET request to the users 
     * repository, in order to retrieve a user with a particular surname.
     */
    @Test
    void getBySurname() {
        Response response = getRequest("sname/surname3").get();
        assertEquals("should return status 200", 200, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the getBySurname() method, which sends a GET request to the users
     * repository, in order to check that a particular surname doesn't exist.
     */
    @Test
    void getBySurname_surnameDoesntExist() {
        Response response = getRequest("sname/surname7").get();
        assertEquals("should return status 204", 204, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the getByEmail() method, which sends a GET request to the users
     * repository, in order to retrieve a user with a particular email.
     */
    @Test
    void getByEmail() {
        Response response = getRequest("mail/name3@gmail.com").get();
        assertEquals("should return status 200", 200, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the getByEmail() method, which sends a GET request to the users
     * repository, in order to check that a particular email doesn't exist.
     */
    @Test
    void getByEmail_emailDoesntExist() {
        Response response = getRequest("mail/name6@gmail.com").get();
        assertEquals("should return status 204", 204, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the add() method, which sends a POST request to the users repository,
     * in order to create a new user.
     */
    @Test
    void createUser() {
        User newUser = new User("name4", "surname4", "name5@gmail.com");
        Response response = getRequest("createUser")
                .post(Entity.entity(newUser, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 201", 201, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity());
    }

    /**
     * This method is responsible for testing the delete() method, which sends a DELETE request to the users repository,
     * in order to delete a user. In this case, the POST must fail, should the username doesn't match with any
     * of the users' emails.
     */
    @Test
    void deleteUser(){
        Response response = getRequest("1").delete();

        assertEquals("Should return status 204", 204, response.getStatus());
    }

    /**
     * This method is responsible for testing the delete() method, which sends a DELETE request to the users repository,
     * in order to delete a user. In this case, the DELETE must fail, should the user id doesn't match with any
     * of the available users.
     */
    @Test
    void deleteUser_userDoesntExist() {
        Response response = getRequest("6").delete();
        assertEquals("Should return status 404", 404, response.getStatus());
    }

    /**
     * This method is responsible for testing the update() method, which sends a PUT request to the users repository,
     * in order to update a user.
     */
    @Test
    void updateUser() {
        User userToUpdate = new User("name2", "surname2", "name2@hotmail.com");
        Response response = getRequest("2").put(Entity.entity(userToUpdate, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 200", 204, response.getStatus());
    }

    /**
     * This method is responsible for testing the update() method, which sends a PUT request to the users repository,
     * in order to update a user. In this case, the PUT must fail, should the id doesn't match with any
     * of the user ids.
     */
    @Test
    void updateUser_userDoesntExist() {
        User userToUpdate = new User("name1", "surname1", "name1@hotmail.com");
        Response response = getRequest("6").put(Entity.entity(userToUpdate, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 404", 404, response.getStatus());
    }

    @AfterAll
    static void terminate() throws Exception {
        RouterTestsConfig.tearDown();
    }

    Invocation.Builder getRequest(String path) {
        Client client = ClientBuilder.newClient(new RouterTestsConfig());
        WebTarget webTarget = client.target("http://localhost:8080/").path("users/" + path);
        return webTarget.request();
    }
}
