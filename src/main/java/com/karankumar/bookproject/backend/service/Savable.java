package com.karankumar.bookproject.backend.service;

public interface Savable<T> {
    void save(T obj);
}
