/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */


package com.karankumar.bookproject.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.karankumar.bookproject.backend.model.account.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedAttributeNode;
import java.util.Set;

@Entity
@JsonIgnoreProperties(value = {"id", "books"})
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@NamedEntityGraph(name = "CustomShelf.books",
        attributeNodes = @NamedAttributeNode("books")
)
public class UserCreatedShelf extends Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userCreatedShelf")
    protected Set<Book> books;

    public UserCreatedShelf(String shelfName, User user) {
        super(shelfName, user);
    }

    public void setShelfName(String shelfName) {
        super.shelfName = shelfName;
    }
}
