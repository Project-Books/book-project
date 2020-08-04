package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.ui.book.BookForm;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class BookDeleteListener {
    private final BookService bookService;
    private final BooksInShelfView view;

    public BookDeleteListener(BookService bookService, BooksInShelfView view) {
        this.bookService = bookService;
        this.view = view;
    }

    public void bind(BookForm bookForm) {
        bookForm.addListener(BookForm.DeleteEvent.class, this::deleteBook);
    }

    private void deleteBook(BookForm.DeleteEvent event) {
        LOGGER.log(Level.INFO, "Deleting book...");
        bookService.delete(event.getBook());
        view.updateGrid();
    }
}