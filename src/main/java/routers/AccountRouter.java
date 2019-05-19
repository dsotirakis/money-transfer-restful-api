package routers;

import models.Account;
import repositories.AccountRepository;
import repositories.RepositoryGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountRouter {

    @GET
    @Path("getAll")
    public Set<Account> getAllAccounts() {
        return getAccountRepository().getAll();
    }

    @GET
    @Path("{id}")
    public Account getById(@PathParam("id") int id) {
        return getAccountRepository().getById(id);
    }

    @POST
    @Path("createAccount")
    public Account insertAccount(Account account) {
        return getAccountRepository().add(account);
    }

    @DELETE
    @Path("{id}")
    public Response deleteAccount(@PathParam("id") int id) {
        if (getAccountRepository().delete(id) != null) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response updateAccount(@PathParam("id") int accountIdToUpdate, Account updatedAccount) {
        if (getAccountRepository().update(accountIdToUpdate, updatedAccount) != null) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private AccountRepository getAccountRepository() {
        return RepositoryGenerator.getAccountRepository();
    }
}
