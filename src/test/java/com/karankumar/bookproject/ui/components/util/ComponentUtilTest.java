/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui.components.util;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("ComponentUtil should")
class ComponentUtilTest {

    @Test
    void clearComponentFields() {
        // given a variety of form fields
        TextField bookTitle = new TextField("Book title");

        ComboBox<PredefinedShelf.ShelfName> predefinedShelf = new ComboBox<>();
        predefinedShelf.setItems(PredefinedShelf.ShelfName.values());
        predefinedShelf.setValue(PredefinedShelf.ShelfName.READ);

        IntegerField numberOfPages = new IntegerField();
        numberOfPages.setValue(400);

        Checkbox inSeries = new Checkbox(false);

        HasValue[] components = {
                bookTitle,
                predefinedShelf,
                numberOfPages,
                inSeries
        };

        // when
        ComponentUtil.clearComponentFields(components);

        // then
        assertAllEmpty(components);
    }

    private void assertAllEmpty(HasValue[] fields) {
        for (HasValue field : fields) {
            assertThat(field.isEmpty()).isTrue();
        }
    }

    @Test
    void notThrowExceptionWhenClearingNullComponents() {
        TextField book = null;
        HasValue[] components = {
                book
        };

        assertDoesNotThrow(() -> ComponentUtil.clearComponentFields(components));
    }
}
