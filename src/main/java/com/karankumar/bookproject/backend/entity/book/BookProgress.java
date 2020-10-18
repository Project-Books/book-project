package com.karankumar.bookproject.backend.entity.book;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.entity.enums.State;
import com.karankumar.bookproject.backend.json.LocalDateSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Data
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
