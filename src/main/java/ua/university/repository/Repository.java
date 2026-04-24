package ua.university.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    void add(T entity);
    boolean deleteById(ID id);
    boolean update(ID id, T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
}
