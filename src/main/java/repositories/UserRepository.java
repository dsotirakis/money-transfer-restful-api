package repositories;

import models.User;

import java.util.Set;

public interface UserRepository extends Repository<User> {

    User add(User user);

    User delete(int id);

    User update(int id, User updatedUser);

    Set<User> getAll();

    User getById(int id);

    User getByName(String name);

    User getBySurname(String surname);

    User getByMail(String mail);
}
