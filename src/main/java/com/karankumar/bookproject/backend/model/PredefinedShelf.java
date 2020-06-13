package com.karankumar.bookproject.backend.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class PredefinedShelf extends BaseEntity {
    public enum ShelfName {
        TO_READ("To read"),
        READING("Reading"),
        READ("Read");

        private final String name;

        ShelfName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @NotNull
    public ShelfName shelfName;

    public PredefinedShelf() {
    }

    public PredefinedShelf(ShelfName shelfName) {
        this.shelfName = shelfName;
    }

    public ShelfName getShelfName() {
        return shelfName;
    }

    public void setShelfName(ShelfName shelfName) {
        this.shelfName = shelfName;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shelf")
    private Set<Book> books;

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
