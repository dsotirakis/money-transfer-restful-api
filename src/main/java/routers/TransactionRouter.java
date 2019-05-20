package routers;

import models.Transaction;
import repositories.RepositoryGenerator;
import repositories.TransactionRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionRouter {

    private TransactionRepository getTransactionRepository() {
        return RepositoryGenerator.getTransactionRepository();
    }

    @GET
    @Path("getAll")
    public Set<Transaction> getAllTransactions() {
        return getTransactionRepository().getAll();
    }

    @GET
    @Path("{id}")
    public Transaction getById(@PathParam("id") String id) {
        return getTransactionRepository().getById(id);
    }

    @POST
    @Path("makeTransaction")
    public Response makeTransaction(Transaction transaction) {
        return getTransactionRepository().makeTransaction(transaction);
    }
}
