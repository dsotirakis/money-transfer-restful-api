package repositories.impl;

import models.User;
import repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

public class UserRepositoryImpl implements UserRepository {

    private Set<User> users;

    UserRepositoryImpl() {
        this.users = new HashSet<>();
    }

    public void add(User user) {
        this.users.add(user);
    }

    public void delete(User user) {
        this.users.remove(user);
    }

    public User update(User user) {
        return null;
    }

    public Set<User> getAll() {
        return users;
    }

    public User getById(String id) {
        return users.stream().filter(e -> e.getId().equals(id)).findAny().get();
    }
}
