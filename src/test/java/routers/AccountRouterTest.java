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

    @Test
    void getAll() {
        Response response = getRequest("getAll").get();
        assertEquals("Should return status 200", 200, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity().toString());
    }

    @Test
    void getById() {
        Response response = getRequest("3").get();
        assertEquals("Should return status 200", 200, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity().toString());
    }

    @Test
    void getById_DoesntExist() {
        Response response = getRequest("6").get();
        assertEquals("Should return status 204", 204, response.getStatus());
    }

    @Test
    void getByUsername() {
        Response response = getRequest("uname/name3@gmail.com").get();
        assertEquals("Should return status 200", 200, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity().toString());
    }

    @Test
    void getByUsername_usernameDoesntExist() {
        Response response = getRequest("uname/name7@gmail.com").get();
        assertEquals("Should return status 204", 204, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity().toString());

    }

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

    @Test
    void createAccount_userForTheAccountDoesntExist() {
        Account newAccount = new Account("name6@gmail.com", "password4", new BigDecimal(100.0), "USD");
        Response response = getRequest("createAccount")
                .post(Entity.entity(newAccount, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 404", 404, response.getStatus());
        assertNotNull("Connection Should be made.", response.getEntity());
    }

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

    @Test
    void deleteAccount(){
        Response response = getRequest("1").delete();
        assertEquals("Should return status 204", 204, response.getStatus());
    }

    @Test
    void deleteAccount_accountDoesntExist() {
        Response response = getRequest("6").delete();
        assertEquals("Should return status 404", 404, response.getStatus());
    }

    @Test
    void updateAccount() {
        Account accountToUpdate = new Account("name2", "password2", new BigDecimal(101.0), "USD");
        Response response = getRequest("2").put(Entity.entity(accountToUpdate, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 200", 204, response.getStatus());
    }

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
