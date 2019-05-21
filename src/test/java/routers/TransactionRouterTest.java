package routers;

import models.Transaction;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.RepositoryGenerator;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

/**
 * This test is responsible for testing the router class. This is in fact the test class that tests the API
 * calls between the client and the server.
 */
public class TransactionRouterTest extends JerseyTest {

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(TransactionRouter.class);
    }

    @BeforeAll
    static void setup() throws Exception {
        RouterTestsConfig.setUp();
    }

    /**
     * This method is responsible for testing the getAll() method, which sends a GET request to the transactions
     * repository, in order to retrieve all the transactions.
     */
    @Test
    void getAll() {
        Response response = getRequest("getAll").get();
        assertEquals("should return status 200", 200, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity().toString());
    }

    /**
     * This method is responsible for testing the makeTransaction() method, which sends a POST request to the
     * transactions repository, in order to create a new transaction.
     */
    @Test
    void makeTransaction() {
        Transaction transaction = new Transaction(4, 5, new BigDecimal(10.0), "USD");
        Response response = getRequest("makeTransaction")
                .post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 201", 201, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity());
        assertTrue(RepositoryGenerator.getTransactionRepository().getAll().size() != 0);
    }

    /**
     * This method is responsible for testing the makeTransaction() method, which sends a POST request to the
     * transactions repository, in order to create a new transaction. In this case, the test is expected to fail,
     * should the currency code is invalid.
     */
    @Test
    void makeTransaction_invalidCurrencyCode() {
        Transaction transaction = new Transaction(1, 2, new BigDecimal(10.0), "ZZZ");
        Response response = getRequest("makeTransaction")
                .post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 404", 404, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity());
    }

    /**
     * This method is responsible for testing the makeTransaction() method, which sends a POST request to the
     * transactions repository, in order to create a new transaction. In this case, the test is expected to fail,
     * should one or more accounts doesn't exist.
     */
    @Test
    void makeTransaction_invalidAccounts() {
        Transaction transaction = new Transaction(10, 2, new BigDecimal(10.0), "USD");
        Response response = getRequest("makeTransaction")
                .post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 404", 404, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity());
    }

    /**
     * This method is responsible for testing the makeTransaction() method, which sends a POST request to the
     * transactions repository, in order to create a new transaction. In this case, the test is expected to fail,
     * should the amount of the payee is insufficient.
     */
    @Test
    void makeTransaction_insufficientAmount() {
        Transaction transaction = new Transaction(2, 3, new BigDecimal(99999.0), "USD");
        Response response = getRequest("makeTransaction")
                .post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        assertEquals("Should return status 400", 400, response.getStatus());
        assertNotNull("Connection should be made.", response.getEntity());
    }

    @AfterAll
    static void terminate() throws Exception {
        RouterTestsConfig.tearDown();
    }

    Invocation.Builder getRequest(String path) {
        Client client = ClientBuilder.newClient(new RouterTestsConfig());
        WebTarget webTarget = client.target("http://localhost:8080/").path("transactions/" + path);
        return webTarget.request();
    }
}
