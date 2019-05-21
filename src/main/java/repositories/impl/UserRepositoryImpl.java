package repositories.impl;

import models.User;
import repositories.InMemoryDatabase;
import repositories.UserRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.dbutils.DbUtils;

import javax.ws.rs.core.Response;

/**
 * This class implements a user repository. It is responsible for manipulating users in the database,
 * as well as connecting and caching entities in a static Set, in order to be widely accessible from all the
 * modules.
 */
public class UserRepositoryImpl implements UserRepository {

    private static final Set<User> userCache = new HashSet<>();

    public UserRepositoryImpl() {
        cacheUsers();
    }

    /**
     * This method is used in order to cache the users. Every time a CRUD operation occurs, the cache is updated.
     */
    private void cacheUsers() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = InMemoryDatabase.getConnection();
            statement = Objects.requireNonNull(connection).prepareStatement("SELECT * FROM Users");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("ID"),
                        resultSet.getString("NAME"),
                        resultSet.getString("SURNAME"),
                        resultSet.getString("EMAILADDRESS"));
                userCache.add(user);
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection, statement, resultSet);
        }
    }

    /**
     * This method adds a new user to the database.
     *
     * @param newUser the new user to be stored.
     *
     * @return a response, indicating the result of the POST method.
     */
    public Response add(User newUser) {

        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            //Set auto commit to false
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("INSERT INTO USERS (name, surname, emailaddress) VALUES (?, ?, ?)");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setString(1, newUser.getName());
            statement.setString(2, newUser.getSurname());
            statement.setString(3, newUser.getEmail());

            statement.executeUpdate();

            //CLose the PreparedStatement object
            statement.close();

            //Close the Connection Object
            connection.commit();

        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

        userCache.add(newUser);

        return Response.status(Response.Status.CREATED).entity("User created successfully!").build();
    }

    /**
     * This method deletes a user from the database.
     *
     * @param id the user id to be deleted.
     *
     * @return a response, indicating the result of the DELETE method.
     */
    public Response delete(int id) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getId() == id)
                .findAny();

        if (!optionalUser.isPresent())
            return objectNotFound();

        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            //Set auto commit to false
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("DELETE FROM Users WHERE id = ? ");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setInt(1, id);

            statement.executeUpdate();

            //CLose the PreparedStatement object
            statement.close();

            //Close the Connection Object
            connection.commit();

        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

        userCache.remove(optionalUser.get());

        return Response.status(Response.Status.NO_CONTENT).entity("User deleted successfully!").build();
    }

    /**
     * This method updates a user in the database.
     *
     * @param id the user's id to be updated.
     * @param updatedUser the new user attributes.
     *
     * @return a response, indicating the result of the PUT method.
     */
    public Response update(int id, User updatedUser) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getId() == id)
                .findAny();

        if (!optionalUser.isPresent()) {
            return objectNotFound();
        }

        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            //Set auto commit to false
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("UPDATE Users SET name = ?, surname = ?, emailaddress = ? WHERE id = ?");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setString(1, updatedUser.getName());
            statement.setString(2, updatedUser.getSurname());
            statement.setString(3, updatedUser.getEmail());
            statement.setInt(4, id);

            statement.executeUpdate();

            //CLose the PreparedStatement object
            statement.close();

            //Close the Connection Object
            connection.commit();
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

        userCache.remove(optionalUser.get());

        updatedUser = updateIdOfUpdatedObject(optionalUser.get(), updatedUser);
        userCache.add(updatedUser);

        return Response.noContent().entity("User updated successfully!").build();
    }

    /**
     * This method is responsible for returning a NOT_FOUND response in case the user does not exist.
     *
     * @return a response, indicating that the user is not found.
     */
    public Response objectNotFound() {
        return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
    }

    /**
     * This method makes sure that the new updated user is stored to the proper id of the previous one.
     *
     * @param previousUser the previous user to be replaced.
     * @param updatedUser the updated user.
     *
     * @return the new user object.
     */
    public User updateIdOfUpdatedObject(User previousUser, User updatedUser) {
        return new User(
                previousUser.getId(),
                updatedUser.getName(),
                updatedUser.getSurname(),
                updatedUser.getEmail());
    }

    /**
     * This method is responsible for returning the user cache.
     *
     * @return a set containing all the users in the cache.
     */
    public Set<User> getAll() {
        return userCache;
    }

    /**
     * This method is responsible for returning a user based on its id.
     *
     * @param id the id of the user to retrieve.
     *
     * @return the retrieved user.
     */
    public User getById(int id) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getId() == id)
                .findAny();

        return optionalUser.orElse(null);
    }

    /**
     * This method is responsible for returning a user based on its name.
     *
     * @param name the name of the user to retrieve.
     *
     * @return the retrieved user.
     */
    @Override
    public User getByName(String name) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getName().equals(name))
                .findAny();

        return optionalUser.orElse(null);
    }

    /**
     * This method is responsible for returning a user based on its surname.
     *
     * @param surname the surname of the user to retrieve.
     *
     * @return the retrieved user.
     */
    @Override
    public User getBySurname(String surname) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getSurname().equals(surname))
                .findAny();

        return optionalUser.orElse(null);
    }

    /**
     * This method is responsible for returning a user based on its mail.
     *
     * @param mail the mail of the user to retrieve.
     *
     * @return the retrieved user.
     */
    @Override
    public User getByMail(String mail) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getEmail().equals(mail))
                .findAny();

        return optionalUser.orElse(null);
    }
}
