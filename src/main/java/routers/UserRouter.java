package routers;

import models.User;
import repositories.RepositoryGenerator;
import repositories.UserRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * This class is responsible for the API connections between the application and the server,
 * regarding the user model.
 * All the responses are returning JSON objects.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserRouter {

    /**
     * This is a getter method, responsible for getting the static user repository.
     *
     * @return the user repository.
     */
    private UserRepository getUserRepository() {
        return RepositoryGenerator.getUserRepository();
    }

    /**
     * This method is a GET method, responsible for getting all the users.
     *
     * @return the total users of the database.
     */
    @GET
    @Path("getAll")
    public Set<User> getAllUsers() {
        return getUserRepository().getAll();
    }

    /**
     * This method is a GET method, responsible for getting an user with a certain id.
     *
     * @param id the user id.
     *
     * @return the user with the certain id.
     */
    @GET
    @Path("{id}")
    public User getById(@PathParam("id") int id) {
        return getUserRepository().getById(id);
    }

    /**
     * This method is a GET method, responsible for getting an user with a certain username.
     *
     * @param name the user username.
     *
     * @return the user with the certain username.
     */
    @GET
    @Path("/name/{name}")
    public User getByName(@PathParam("name") String name) {
        return getUserRepository().getByName(name);
    }

    /**
     * This method is a GET method, responsible for getting an user with a certain username.
     *
     * @param surname the user username.
     *
     * @return the user with the certain username.
     */
    @GET
    @Path("/sname/{surname}")
    public User getBySurname(@PathParam("surname") String surname) {
        return getUserRepository().getBySurname(surname);
    }

    /**
     * This method is a GET method, responsible for getting an user with a certain username.
     *
     * @param mail the user username.
     *
     * @return the user with the certain username.
     */
    @GET
    @Path("/mail/{mail}")
    public User getByMail(@PathParam("mail") String mail) {
        return getUserRepository().getByMail(mail);
    }

    /**
     * This method is a POST method, responsible for adding a new user to the database.
     *
     * @param user the new user to be added.
     *
     * @return a response, indicating if the POST method was successful.
     */
    @POST
    @Path("createUser")
    public Response createUser(User user) {
        return getUserRepository().add(user);
    }

    /**
     * This method is a DELETE method, responsible for deleting an user from the database.
     *
     * @param id the id of the user to be deleted.
     *
     * @return a response, indicating if the DELETE method was successful.
     */
    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") int id) {
        return getUserRepository().delete(id);
    }

    /**
     * This method is a PUT method, responsible for updating an user in the database.
     *
     * @param userIdToUpdate the id of the user to be updated.
     * @param updatedUser the user with the updated details.
     *
     * @return a response, indicating if the PUT method was successful.
     */
    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") int userIdToUpdate, User updatedUser) {
        return getUserRepository().update(userIdToUpdate, updatedUser);
    }
}
