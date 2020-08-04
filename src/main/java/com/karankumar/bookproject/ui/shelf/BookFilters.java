package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.entity.Book;

class BookFilters {
    private String bookTitle;
    private String bookAuthor;

    private String getBookTitle() {
        return bookTitle.toLowerCase();
    }

    void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    private String getBookAuthor() {
        return bookAuthor.toLowerCase();
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
        return isBookAuthorNull() || book.getAuthor().toString().toLowerCase().contains(getBookAuthor());
    }

    private boolean containsBookTitle(Book book) {
        return isBookTitleNull() || book.getTitle().toLowerCase().contains(getBookTitle());
    }
}