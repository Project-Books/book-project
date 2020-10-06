package com.karankumar.bookproject.backend.service;

import java.util.List;

public interface Findable<T, Id> {
    T findById(Id id);

    List<T> findAll();
}
