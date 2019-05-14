package repositories;

import java.util.Set;

public interface Repository<T> {

    void add(T t);

    void delete(T t);

    T update(T t);

    Set<T> getAll();

    T getById(String id);

}
