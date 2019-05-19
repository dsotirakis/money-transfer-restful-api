package repositories;

import javax.ws.rs.core.Response;
import java.util.Set;

public interface Repository<T> {

    Response add(T t);

    Response delete(int id);

    Response update(int id, T updatedObject);

    Set<T> getAll();

    T getById(int id);

}
