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

/**
 * A predefined shelf is a shelf that is created by the app and will always exist (cannot be
 * deleted or renamed)
 */
@Entity
@JsonIgnoreProperties(value = {"id", "books", "predefinedShelfName"})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraph(name = "PredefinedShelf.books",
        attributeNodes = @NamedAttributeNode("books")
)
public class PredefinedShelf extends Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Setter(AccessLevel.NONE)
    private ShelfName predefinedShelfName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "predefinedShelf")
    protected Set<Book> books;

    public PredefinedShelf(ShelfName predefinedShelfName, User user) {
        super(predefinedShelfName.toString(), user);
        this.predefinedShelfName = predefinedShelfName;
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

        @Override
        public String toString() {
            return name;
        }
    }
}
