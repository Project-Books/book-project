/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.karankumar.bookproject.backend.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * The entity that all other entities inherit from
 */
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    public Long getId() {
        return id;
    }

    /**
     * This should be used sparingly & cautiously
     */
    public void removeId() {
        id = null;
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        BaseEntity other = (BaseEntity) obj;
        if (getId() == null || other.getId() == null) {
            return false;
        }
        return getId().equals(other.getId());
    }
}
