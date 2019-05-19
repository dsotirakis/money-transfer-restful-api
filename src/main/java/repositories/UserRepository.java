package repositories;

import models.User;

import javax.ws.rs.core.Response;
import java.util.Set;

public interface UserRepository extends Repository<User> {

    Response add(User user);

    Response delete(int id);

    Response update(int id, User updatedUser);

    Set<User> getAll();

    User getById(int id);

    User getByName(String name);

    User getBySurname(String surname);

    User getByMail(String mail);
}
