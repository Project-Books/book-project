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
import lombok.ToString;

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
