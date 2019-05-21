package routers;

import models.User;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import models.Account;
import repositories.RepositoryGenerator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

import java.math.BigDecimal;

/**
 * This test is responsible for testing the account router class. This is in fact the test class that tests the API
 * calls between the client and the server.
 */
public class AccountRouterTest extends JerseyTest {

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(AccountRouter.class);
    }

    @BeforeAll
    static void setup() throws Exception {
        RouterTestsConfig.setUp();
    }

    /**
     * This method is responsible for testing the getAll() method, which sends a GET request to the accounts repository,
     * in order to retrieve all the accounts.
     */
    @Test
    void getAll() {
        Response response = getRequest("getAll").get();
        assertEquals("Should return status 200", 200, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the getById() method, which sends a GET request to the accounts repository,
     * in order to retrieve an account with a particular id.
     */
    @Test
    void getById() {
        Response response = getRequest("3").get();
        assertEquals("Should return status 200", 200, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the getById() method, which sends a GET request to the accounts repository,
     * in order to check that a particular id doesn't exist.
     */
    @Test
    void getById_DoesntExist() {
        Response response = getRequest("6").get();
        assertEquals("Should return status 204", 204, response.getStatus());
    }

    /**
     * This method is responsible for testing the getByUsername() method, which sends a GET request to the accounts
     * repository, in order to retrieve an account with a particular username.
     */
    @Test
    void getByUsername() {
        Response response = getRequest("uname/name3@gmail.com").get();
        assertEquals("Should return status 200", 200, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the getByUsername() method, which sends a GET request to the accounts
     * repository, in order to check that a particular username doesn't exist.
     */
    @Test
    void getByUsername_usernameDoesntExist() {
        Response response = getRequest("uname/name7@gmail.com").get();
        assertEquals("Should return status 204", 204, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity().toString());

    }

    /**
     * This method is responsible for testing the add() method, which sends a POST request to the accounts repository,
     * in order to create a new account.
     */
    @Test
    void createAccount() {
        RepositoryGenerator.getUserRepository().add(new User("newName", "newSurname", "name4@gmail.com"));
        Account newAccount = new Account("name4@gmail.com", "password4", new BigDecimal(100.0), "USD");
        Response response = getRequest("createAccount")
                .post(Entity.entity(newAccount, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 201", 201, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity());
        RepositoryGenerator.getUserRepository().delete(0);
    }

    /**
     * This method is responsible for testing the add() method, which sends a POST request to the accounts repository,
     * in order to create a new account. In this case, the POST must fail, should the username doesn't match with any
     * of the users' emails.
     */
    @Test
    void createAccount_userForTheAccountDoesntExist() {
        Account newAccount = new Account("name6@gmail.com", "password4", new BigDecimal(100.0), "USD");
        Response response = getRequest("createAccount")
                .post(Entity.entity(newAccount, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 404", 404, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity());
    }

    /**
     * This method is responsible for testing the add() method, which sends a POST request to the accounts repository,
     * in order to create a new account. In this case, the POST must fail, should the currency code doesn't match with any
     * of the valid currencies.
     */
    @Test
    void createAccount_invalidCurrencyCode() {
        RepositoryGenerator.getUserRepository().add(new User("newName", "newSurname", "name4@gmail.com"));
        Account newAccount = new Account("name4@gmail.com", "password4", new BigDecimal(100.0), "ZZZ");
        Response response = getRequest("createAccount")
                .post(Entity.entity(newAccount, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 404", 404, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity());
        RepositoryGenerator.getUserRepository().delete(0);
    }

    /**
     * This method is responsible for testing the delete() method, which sends a DELETE request to the accounts repository,
     * in order to delete an account. In this case, the POST must fail, should the username doesn't match with any
     * of the users' emails.
     */
    @Test
    void deleteAccount(){
        Response response = getRequest("1").delete();
        assertEquals("Should return status 204", 204, response.getStatus());
    }

    /**
     * This method is responsible for testing the delete() method, which sends a DELETE request to the accounts repository,
     * in order to delete an account. In this case, the DELETE must fail, should the account id doesn't match with any
     * of the available accounts.
     */
    @Test
    void deleteAccount_accountDoesntExist() {
        Response response = getRequest("6").delete();
        assertEquals("Should return status 404", 404, response.getStatus());
    }

    /**
     * This method is responsible for testing the update() method, which sends a PUT request to the accounts repository,
     * in order to update an account.
     */
    @Test
    void updateAccount() {
        Account accountToUpdate = new Account("name2", "password2", new BigDecimal(101.0), "USD");
        Response response = getRequest("2").put(Entity.entity(accountToUpdate, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 200", 204, response.getStatus());
    }

    /**
     * This method is responsible for testing the update() method, which sends a PUT request to the accounts repository,
     * in order to update an account. In this case, the PUT must fail, should the id doesn't match with any
     * of the account ids.
     */
    @Test
    void updateAccount_accountDoesntExist() {
        Account accountToUpdate = new Account("name1", "password1", new BigDecimal(101.0), "USD");
        Response response = getRequest("6").put(Entity.entity(accountToUpdate, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 404", 404, response.getStatus());
    }

    @AfterAll
    static void terminate() throws Exception {
        RouterTestsConfig.tearDown();
    }

    Invocation.Builder getRequest(String path) {
        Client client = ClientBuilder.newClient(new RouterTestsConfig());
        WebTarget webTarget = client.target("http://localhost:8080/").path("accounts/" + path);
        return webTarget.request();
    }
}
