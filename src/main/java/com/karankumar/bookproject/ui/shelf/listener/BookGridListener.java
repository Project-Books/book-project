package com.karankumar.bookproject.ui.shelf.listener;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.BookForm;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.grid.Grid;

public class BookGridListener {
    private final BookForm bookForm;

    public BookGridListener(BookForm bookForm) {
        this.bookForm = bookForm;
    }

    public HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<Grid<Book>, Book>> getListener() {
        return event -> {
            if (event.getValue() != null) {
                editBook(event.getValue());
            }
        };
    }

    private void editBook(Book book) {
        if (book == null || bookForm == null) {
            return;
        }

        bookForm.setBook(book);
        bookForm.openForm();
    }
}