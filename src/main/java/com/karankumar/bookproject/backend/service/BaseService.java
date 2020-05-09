package com.karankumar.bookproject.backend.service;

/**
 * @author karan on 09/05/2020
 */
public abstract class BaseService<T, ID> {

    public abstract T findById(ID id);

    public abstract void save(T obj);

    public abstract void delete(T obj);
}
