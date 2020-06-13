package com.karankumar.bookproject.backend.model.shelves;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class PredefinedShelf extends Shelf {
    enum ShelfName {
        TO_READ,
        READING,
        READ
    }

    private ShelfName shelfName;

    public PredefinedShelf(ShelfName shelfName) {
        this.shelfName = shelfName;
    }

    public ShelfName getShelfName() {
        return shelfName;
    }
}
