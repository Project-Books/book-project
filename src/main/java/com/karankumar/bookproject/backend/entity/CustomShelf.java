package com.karankumar.bookproject.backend.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomShelf extends Shelf {
    public CustomShelf(String shelfName) {
        super(shelfName);
    }
}
