package com.karankumar.bookproject.backend.service;

public abstract class BaseService<T, ID> {

    public abstract T findById(ID id);

    public abstract void save(T obj);

    public abstract void delete(T obj);
}
