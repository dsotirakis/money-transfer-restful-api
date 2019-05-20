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

    private AccountRepository getAccountRepository() {
        return RepositoryGenerator.getAccountRepository();
    }

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

    @GET
    @Path("/uname/{username}")
    public Account getByUsername(@PathParam("username") String username) {
        return getAccountRepository().getByUsername(username);
    }

    @POST
    @Path("createAccount")
    public Response createAccount(Account account) {
        return getAccountRepository().add(account);
    }

    @DELETE
    @Path("{id}")
    public Response deleteAccount(@PathParam("id") int id) {
        return getAccountRepository().delete(id);
    }

    @PUT
    @Path("{id}")
    public Response updateAccount(@PathParam("id") int accountIdToUpdate, Account updatedAccount) {
        return getAccountRepository().update(accountIdToUpdate, updatedAccount);
    }
}
