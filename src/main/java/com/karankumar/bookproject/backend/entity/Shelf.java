package com.karankumar.bookproject.backend.entity;

import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@MappedSuperclass
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Shelf extends BaseEntity {
    @NotNull
    protected String shelfName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shelf")
    @Setter
    private Set<Book> books;

    protected Shelf(String shelfName) {
        this.shelfName = shelfName;
    }
}
