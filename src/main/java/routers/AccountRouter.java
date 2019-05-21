package routers;

import models.Account;
import repositories.AccountRepository;
import repositories.RepositoryGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Set;

/**
 * This class is responsible for the API connections between the application and the server,
 * regarding the account model.
 * All the responses are returning JSON objects.
 */
@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountRouter {

    /**
     * This is a getter method, responsible for getting the static account repository.
     *
     * @return the account repository.
     */
    private AccountRepository getAccountRepository() {
        return RepositoryGenerator.getAccountRepository();
    }

    /**
     * This method is a GET method, responsible for getting all the accounts.
     *
     * @return the total accounts of the database.
     */
    @GET
    @Path("getAll")
    public Set<Account> getAllAccounts() {
        return getAccountRepository().getAll();
    }

    /**
     * This method is a GET method, responsible for getting an account with a certain id.
     *
     * @param id the account id.
     *
     * @return the account with the certain id.
     */
    @GET
    @Path("{id}")
    public Account getById(@PathParam("id") int id) {
        return getAccountRepository().getById(id);
    }

    /**
     * This method is a GET method, responsible for getting an account with a certain username.
     *
     * @param username the account username.
     *
     * @return the account with the certain username.
     */
    @GET
    @Path("/uname/{username}")
    public Account getByUsername(@PathParam("username") String username) {
        return getAccountRepository().getByUsername(username);
    }

    /**
     * This method is a POST method, responsible for adding a new account to the database.
     *
     * @param account the new account to be added.
     *
     * @return a response, indicating if the POST method was successful.
     */
    @POST
    @Path("createAccount")
    public Response createAccount(Account account) {
        return getAccountRepository().add(account);
    }

    /**
     * This method is a DELETE method, responsible for deleting an account from the database.
     *
     * @param id the id of the account to be deleted.
     *
     * @return a response, indicating if the DELETE method was successful.
     */
    @DELETE
    @Path("{id}")
    public Response deleteAccount(@PathParam("id") int id) {
        return getAccountRepository().delete(id);
    }

    /**
     * This method is a PUT method, responsible for updating an account in the database.
     *
     * @param accountIdToUpdate the id of the account to be updated.
     * @param updatedAccount the account with the updated details.
     *
     * @return a response, indicating if the PUT method was successful.
     */
    @PUT
    @Path("{id}")
    public Response updateAccount(@PathParam("id") int accountIdToUpdate, Account updatedAccount) {
        return getAccountRepository().update(accountIdToUpdate, updatedAccount);
    }
}
