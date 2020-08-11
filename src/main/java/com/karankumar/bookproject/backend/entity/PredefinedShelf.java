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

package com.karankumar.bookproject.backend.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * A predefined shelf is a shelf that is created by the app and will always exist (cannot be deleted or renamed)
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PredefinedShelf extends Shelf {
    // This field must not have a setter as it should not be possible to rename a PredefinedShelf
    @Getter
    private ShelfName predefinedShelfName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "predefinedShelf")
    @Getter
    @Setter
    protected Set<Book> books;

    public PredefinedShelf(ShelfName predefinedShelfName) {
        super(predefinedShelfName.toString());
        this.predefinedShelfName = predefinedShelfName;
    }

    /**
     * This setter must only set the predefinedShelfName if it has not already been set (e.g. via a Vaadin binder)
     * as a predefined shelf should not be renamed
     * @param predefinedShelfName the name of the shelf
     */
    public void setPredefinedShelfName(ShelfName predefinedShelfName) {
        if (this.predefinedShelfName == null) {
            this.predefinedShelfName = predefinedShelfName;
        }
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
