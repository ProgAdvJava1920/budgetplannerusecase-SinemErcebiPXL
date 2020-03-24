package be.pxl.student.entity;

import java.util.List;

public interface DAO<T, E extends Exception> {
    // CRUD principe

    // Create
    T create(T t) throws E;

    // Read
    T getById(int id) throws E;
    List<T> getAll() throws E;

    // Update
    T update(T t) throws E;

    // Delete
    T delete(T t) throws E;
}
