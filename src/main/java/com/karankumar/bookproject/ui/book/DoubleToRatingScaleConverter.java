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

package com.karankumar.bookproject.ui.book;

import com.karankumar.bookproject.backend.entity.RatingScale;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class DoubleToRatingScaleConverter implements Converter<Double, RatingScale> {
    @Override
    public Result<RatingScale> convertToModel(Double ratingVal, ValueContext valueContext) {
        if (ratingVal == null) {
            return Result.ok(RatingScale.NO_RATING);
        } else if (ratingVal == 0.0) {
            return Result.ok(RatingScale.ZERO);
        } else if (ratingVal == 0.5) {
            return Result.ok(RatingScale.ZERO_POINT_FIVE);
        } else if (ratingVal == 1) {
            return Result.ok(RatingScale.ONE);
        } else if (ratingVal == 1.5) {
            return Result.ok(RatingScale.ONE_POINT_FIVE);
        } else if (ratingVal == 2.0) {
            return Result.ok(RatingScale.TWO);
        } else if (ratingVal == 2.5) {
            return Result.ok(RatingScale.TWO_POINT_FIVE);
        } else if (ratingVal == 3.0) {
            return Result.ok(RatingScale.THREE);
        } else if (ratingVal == 3.5) {
            return Result.ok(RatingScale.THREE_POINT_FIVE);
        } else if (ratingVal == 4.0) {
            return Result.ok(RatingScale.FOUR);
        } else if (ratingVal == 4.5) {
            return Result.ok(RatingScale.FOUR_POINT_FIVE);
        } else if (ratingVal == 5.0) {
            return Result.ok(RatingScale.FIVE);
        } else if (ratingVal == 5.5) {
            return Result.ok(RatingScale.FIVE_POINT_FIVE);
        } else if (ratingVal == 6.0) {
            return Result.ok(RatingScale.SIX);
        } else if (ratingVal == 6.5) {
            return Result.ok(RatingScale.SIX_POINT_FIVE);
        } else if (ratingVal == 7.0) {
            return Result.ok(RatingScale.SEVEN);
        } else if (ratingVal == 7.5) {
            return Result.ok(RatingScale.SEVEN_POINT_FIVE);
        } else if (ratingVal == 8.0) {
            return Result.ok(RatingScale.EIGHT);
        } else if (ratingVal == 8.5) {
            return Result.ok(RatingScale.EIGHT_POINT_FIVE);
        } else if (ratingVal == 9.0) {
            return Result.ok(RatingScale.NINE);
        } else if (ratingVal == 9.5) {
            return Result.ok(RatingScale.NINE_POINT_FIVE);
        } else if (ratingVal == 10.0) {
            return Result.ok(RatingScale.TEN);
        } else {
            return Result.error("Invalid rating");
        }
    }

    @Override
    public Double convertToPresentation(RatingScale rating, ValueContext valueContext) {
        if (rating == null) {
            LOGGER.log(Level.INFO, "Returning null as rating was null");
            return null;
        }

        switch (rating) {
            case NO_RATING:
                return null;
            case ZERO:
                return 0.0;
            case ZERO_POINT_FIVE:
                return 0.5;
            case ONE:
                return 1.0;
            case ONE_POINT_FIVE:
                return 1.5;
            case TWO:
                return 2.0;
            case TWO_POINT_FIVE:
                return 2.5;
            case THREE:
                return 3.0;
            case THREE_POINT_FIVE:
                return 3.5;
            case FOUR:
                return 4.0;
            case FOUR_POINT_FIVE:
                return 4.5;
            case FIVE:
                return 5.0;
            case FIVE_POINT_FIVE:
                return 5.5;
            case SIX:
                return 6.0;
            case SIX_POINT_FIVE:
                return 6.5;
            case SEVEN:
                return 7.0;
            case SEVEN_POINT_FIVE:
                return 7.5;
            case EIGHT:
                return 8.0;
            case EIGHT_POINT_FIVE:
                return 8.5;
            case NINE:
                return 9.0;
            case NINE_POINT_FIVE:
                return 9.5;
            default:
                return 10.0;
        }
    }
}
