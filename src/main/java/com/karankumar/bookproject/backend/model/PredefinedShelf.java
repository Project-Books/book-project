package com.karankumar.bookproject.backend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PredefinedShelf extends BaseEntity {
    @NotNull
    @Enumerated(value = EnumType.STRING)
    public ShelfName shelfName;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shelf")
    private Set<Book> books;

    public PredefinedShelf(ShelfName shelfName) {
        this.shelfName = shelfName;
    }

    public enum ShelfName {
        TO_READ("To read"),
        READING("Reading"),
        READ("Read"),
        DID_NOT_FINISH("Did not finish");

        private final String name;

        ShelfName(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}
