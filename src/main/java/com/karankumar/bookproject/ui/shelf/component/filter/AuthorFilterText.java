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

package com.karankumar.bookproject.ui.shelf.component.filter;

import com.karankumar.bookproject.ui.shelf.BooksInShelfView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class AuthorFilterText {
    private final TextField text;

    public AuthorFilterText() {
        text = new TextField();

        text.setPlaceholder("Filter by Author Name");
        text.setClearButtonVisible(true);
        text.setValueChangeMode(ValueChangeMode.LAZY);
    }

    public void bind(BooksInShelfView view) {
        text.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                view.setBookFilterAuthor(event.getValue());
            }
            view.updateGrid();
        });
    }

    public void addToLayout(HorizontalLayout layout) {
        layout.add(text);
    }
}
