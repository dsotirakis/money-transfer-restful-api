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

    @POST
    @Path("createUser")
    public User insertUser(User user) {
        return getUserRepository().add(user);
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") int id) {
        if (getUserRepository().delete(id) != null) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") int userIdToUpdate, User updatedUser) {
        if (getUserRepository().update(userIdToUpdate, updatedUser) != null) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private UserRepository getUserRepository() {
        return RepositoryGenerator.getUserRepository();
    }
}
