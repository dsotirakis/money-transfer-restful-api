package repositories.impl;

import models.User;
import repositories.InMemoryDatabase;
import repositories.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.dbutils.DbUtils;

/**
 * This class is implementing a User Repository.
 */
public class UserRepositoryImpl implements UserRepository {

    private final Set<User> userCache = new HashSet<>();

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(connection, statement, resultSet);
        }
    }

    public User add(User user) {

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

        this.userCache.add(user);

        return null;
    }

    public User delete(int id) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getId() == id)
                .findAny();

        if (!optionalUser.isPresent())
            return null;

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

        this.userCache.remove(optionalUser.get());

        return optionalUser.get();
    }

    public User update(int id, User updatedUser) {

        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getId() == id)
                .findAny();

        if (!optionalUser.isPresent())
            return null;

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

        this.userCache.remove(optionalUser.get());
        this.userCache.add(updatedUser);

        return updatedUser;
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
        return getByParameter(name);
    }

    @Override
    public User getBySurname(String surname) {
        return getByParameter(surname);
    }

    @Override
    public User getByMail(String mail) {
        return getByParameter(mail);
    }

    private User getByParameter(String parameter) {
        Optional<User> optionalUser = userCache.stream()
                .filter(user -> user.getEmail().equals(parameter))
                .findAny();

        return optionalUser.orElse(null);
    }
}
