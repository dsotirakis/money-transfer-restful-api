package routers;

import models.User;
import repositories.RepositoryGenerator;
import repositories.UserRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserRouter {

    @GET
    @Path("getAll")
    public Set<User> getAllUsers() {
        return getUserRepository().getAll();
    }

    @GET
    @Path("{id}")
    public User getById(@PathParam("id") int id) {
        return getUserRepository().getById(id);
    }

    @GET
    @Path("/name/{name}")
    public User getByName(@PathParam("name") String name) {
        return getUserRepository().getByName(name);
    }

    @GET
    @Path("/sname/{surname}")
    public User getBySurname(@PathParam("surname") String surname) {
        return getUserRepository().getBySurname(surname);
    }

    @GET
    @Path("/mail/{mail}")
    public User getByMail(@PathParam("mail") String mail) {
        return getUserRepository().getByMail(mail);
    }

    @POST
    @Path("createUser")
    public Response insertUser(User user) {
        return getUserRepository().add(user);
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") int id) {
        return getUserRepository().delete(id);
    }

    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") int userIdToUpdate, User updatedUser) {
        return getUserRepository().update(userIdToUpdate, updatedUser);
    }

    private UserRepository getUserRepository() {
        return RepositoryGenerator.getUserRepository();
    }
}
