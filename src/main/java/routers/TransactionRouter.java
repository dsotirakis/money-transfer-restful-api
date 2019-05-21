package routers;

import models.Transaction;
import repositories.RepositoryGenerator;
import repositories.TransactionRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * This class is responsible for the API connections between the application and the server,
 * regarding the transaction model.
 * All the responses are returning JSON objects.
 */
@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionRouter {

    /**
     * This is a getter method, responsible for getting the static transaction repository.
     *
     * @return the transaction repository.
     */
    private TransactionRepository getTransactionRepository() {
        return RepositoryGenerator.getTransactionRepository();
    }

    /**
     * This method is a GET method, responsible for getting all the transactions.
     *
     * @return the total transactions of the database.
     */
    @GET
    @Path("getAll")
    public Set<Transaction> getAllTransactions() {
        return getTransactionRepository().getAll();
    }

    /**
     * This method is a GET method, responsible for getting an transaction with a certain id.
     *
     * @param id the transaction id.
     *
     * @return the transaction with the certain id.
     */
    @GET
    @Path("{id}")
    public Transaction getById(@PathParam("id") String id) {
        return getTransactionRepository().getById(id);
    }

    /**
     * This method is a POST method, responsible for making a transaction between two transactions.
     * 
     * @param transaction the transaction to be made.
     *                    
     * @return a response, indicating if the POST method was successful.
     */
    @POST
    @Path("makeTransaction")
    public Response makeTransaction(Transaction transaction) {
        return getTransactionRepository().makeTransaction(transaction);
    }
}
