/**
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.entity.book;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.entity.enums.State;
import com.karankumar.bookproject.backend.json.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BookProgress {

    @EmbeddedId
    private BookProgressId id;

    private RatingScale rating;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateStartedReading;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateFinishedReading;

    private String bookReview;

    @Range(max = 23_000)
    private Integer pagesRead;

    private State state;

}
