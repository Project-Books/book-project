/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.model;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
