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

package com.karankumar.bookproject.ui.shelf.listener;

import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.ui.book.BookForm;
import com.karankumar.bookproject.ui.shelf.BooksInShelfView;
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
