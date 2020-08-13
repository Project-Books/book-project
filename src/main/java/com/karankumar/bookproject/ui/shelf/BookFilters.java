package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.entity.Book;

class BookFilters {
    private String bookTitle;
    private String bookAuthor;

    void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    private boolean isBookTitleNull() {
        return bookTitle == null;
    }

    private boolean isBookAuthorNull() {
        return bookAuthor == null;
    }

    boolean apply(Book book) {
        return containsBookTitle(book) && containsBookAuthor(book);
    }

    private boolean containsBookAuthor(Book book) {
        return isBookAuthorNull() || book.getAuthor()
                                         .toString()
                                         .toLowerCase()
                                         .contains(bookAuthor.toLowerCase());
    }

    private boolean containsBookTitle(Book book) {
        return isBookTitleNull() || book.getTitle()
                                        .toLowerCase()
                                        .contains(bookTitle.toLowerCase());
    }
}
