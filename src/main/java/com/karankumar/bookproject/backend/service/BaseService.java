package com.karankumar.bookproject.backend.service;

/**
 * All other Spring services inherit from this
 *
 * @param <T> an entity type (e.g. {@code Book})
 * @param <ID> the unique ID
 */
public abstract class BaseService<T, ID> {

    public abstract T findById(ID id);

    public abstract void save(T obj);

    public abstract void delete(T obj);
}
