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
 * This class is implementing a User Repository.
 */
public class UserRepositoryImpl implements UserRepository {

    private static final Set<User> userCache = new HashSet<>();

    public UserRepositoryImpl() {
        cacheUsers();
    }

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

    public Response add(User user) {

        Connection connection;
        PreparedStatement statement;
        try {
            connection = InMemoryDatabase.getConnection();
            //Set auto commit to false
            Objects.requireNonNull(connection).setAutoCommit(false);

            String string = ("INSERT INTO USERS (name, surname, emailaddress) VALUES (?, ?, ?)");

            // Used prepare statement methods in order not to pass direct strings, to care about SQL Injection issues.
            statement = connection.prepareStatement(string);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());

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

        userCache.add(user);

        return Response.status(Response.Status.CREATED).entity("User created successfully!").build();
    }

    public Response delete(int id) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getId() == id)
                .findAny();

        if (!optionalUser.isPresent())
            return userNotFound();

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

    public Response update(int id, User updatedUser) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getId() == id)
                .findAny();

        if (!optionalUser.isPresent()) {
            return userNotFound();
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

        updatedUser = updateIdToUpdatedAccount(optionalUser.get(), updatedUser);
        userCache.add(updatedUser);

        return Response.noContent().entity("User updated successfully!").build();
    }

    private Response userNotFound() {
        return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
    }

    private User updateIdToUpdatedAccount(User previousUser, User updatedUser) {
        return new User(
                previousUser.getId(),
                updatedUser.getName(),
                updatedUser.getSurname(),
                updatedUser.getEmail());
    }

    public Set<User> getAll() {
        return userCache;
    }

    public User getById(int id) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getId() == id)
                .findAny();

        return optionalUser.orElse(null);
    }

    @Override
    public User getByName(String name) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getName().equals(name))
                .findAny();

        return optionalUser.orElse(null);
    }

    @Override
    public User getBySurname(String surname) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getSurname().equals(surname))
                .findAny();

        return optionalUser.orElse(null);
    }

    @Override
    public User getByMail(String mail) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getEmail().equals(mail))
                .findAny();

        return optionalUser.orElse(null);
    }
}
