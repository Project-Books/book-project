package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.ui.book.BookForm;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class BookSaveListener {
    private final BookService bookService;
    private final BooksInShelfView view;

    public BookSaveListener(BookService bookService, BooksInShelfView view) {
        this.bookService = bookService;
        this.view = view;
    }

    private void saveBook(BookForm.SaveEvent event) {
        LOGGER.log(Level.INFO, "Saving book...");
        if (event.getBook() == null) {
            LOGGER.log(Level.SEVERE, "Retrieved book from event is null");
        } else {
            LOGGER.log(Level.INFO, "Book is not null");
            bookService.save(event.getBook());
            view.updateGrid();
        }
    }

    public void bind(BookForm bookForm) {
        bookForm.addListener(BookForm.SaveEvent.class, this::saveBook);
    }
}