package com.karankumar.bookproject.backend.service;

public interface Deletable<T> {
    void delete(T obj);

    void deleteAll();
}
