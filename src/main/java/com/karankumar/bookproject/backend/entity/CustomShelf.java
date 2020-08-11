package com.karankumar.bookproject.backend.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomShelf extends Shelf {
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customShelf")
    @Getter
    @Setter
    protected Set<Book> books;

    public CustomShelf(String shelfName) {
        super(shelfName);
    }

    public void setShelfName(String shelfName) {
        super.shelfName = shelfName;
    }
}
