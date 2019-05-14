package repositories;

import models.User;

import java.util.Set;

public interface UserRepository extends Repository<User> {

    public void add(User user);

    public void delete(User user);

    public User update(User user);

    public Set<User> getAll();

    public User getById(String id);
}
