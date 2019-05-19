package repositories;

import java.util.Set;

public interface Repository<T> {

    T add(T t);

    T delete(int id);

    T update(int id, T updatedObject);

    Set<T> getAll();

    T getById(int id);

}
