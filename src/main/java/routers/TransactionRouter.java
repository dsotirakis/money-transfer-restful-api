package routers;

import models.Transaction;
import repositories.TransactionRepository;
import repositories.impl.TransactionRepositoryImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionRouter {

    private TransactionRepository transactionRepo = new TransactionRepositoryImpl();

    @GET
    @Path("getAll")
    public Set<Transaction> getAllAccounts() {
        return transactionRepo.getAll();
    }

    @GET
    @Path("{id}")
    public Transaction getById(@PathParam("id") int id) {
        return transactionRepo.getById(id);
    }

    @POST
    @Path("makeTransaction")
    public Response makeTransaction(Transaction transaction) {
        return transactionRepo.makeTransaction(transaction);
    }
}
