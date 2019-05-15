package repositories;

import models.User;

import java.util.Set;

public interface UserRepository extends Repository<User> {

    void add(User user);

    void delete(User user);

    User update(User user);

    Set<User> getAll();

    User getById(String id);
}
