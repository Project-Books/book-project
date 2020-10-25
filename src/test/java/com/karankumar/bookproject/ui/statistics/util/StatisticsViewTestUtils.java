package com.karankumar.bookproject.ui.statistics.util;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.BookGenre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.statistics.StatisticsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.dom.Element;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Predicate;

public class StatisticsViewTestUtils {

    private StatisticsViewTestUtils() {
    }

    @AllArgsConstructor
    public static class Statistic {
        @Getter private final String caption;
        @Getter private final String value;
    }

    public static class StatisticNotFound extends Statistic {
        public StatisticNotFound() {
            super("statistic", "not found");
        }
    }

    public static Statistic getStatistic(StatisticsView.StatisticType statisticType,
                                         StatisticsView statisticsView) {
        try {
            Element verticalLayoutInDiv =
                    getVerticalLayoutWithStatistic(statisticType, statisticsView);
            // statistic caption is in VL -> H3 -> Text
            String caption = verticalLayoutInDiv.getChild(0)
                                                .getText();
            // statistic value is in VL -> Text
            String value = verticalLayoutInDiv.getText();
            return new Statistic(caption, value);
        } catch (Exception e) {
            return new StatisticNotFound();
        }
    }

    private static Element getVerticalLayoutWithStatistic(
            StatisticsView.StatisticType statisticType, StatisticsView statisticsView) {
        var divContainingStatistic = statisticsView.getChildren()
                                                   .filter(textWithinH3EqualsTo(
                                                           statisticType.getCaption()))
                                                   .findFirst()
                                                   .get()
                                                   .getElement();
        var verticalLayoutInDiv = divContainingStatistic.getChild(0);
        return verticalLayoutInDiv;
    }

    private static Predicate<Component> textWithinH3EqualsTo(String text) {
        return x -> x.getElement() // DIV ->
                     .getChild(0) // VerticalLayout ->
                     .getChild(0) // H3 ->
                     .getText() // Text
                     .equals(text);
    }

    public static void populateDataWithBook(BookService bookService,
                                            PredefinedShelfService predefinedShelfService) {
        Book mobyDickBook = createMobyDickBook(predefinedShelfService);
        bookService.save(mobyDickBook);
    }

    public static void populateDataWithBooksInDifferentGenres(
            BookService bookService, PredefinedShelfService predefinedShelfService) {
        bookService.save(createMobyDickBook(predefinedShelfService));
        bookService.save(createHobbitBook(predefinedShelfService));
    }

    public static void populateDataWithBooksDifferentGenresWithoutPageCount(
            BookService bookService, PredefinedShelfService predefinedShelfService) {
        Book mobyDickBook = createMobyDickBook(predefinedShelfService);
        Book hobbitBook = createHobbitBook(predefinedShelfService);
        mobyDickBook.setNumberOfPages(null);
        hobbitBook.setNumberOfPages(null);
        bookService.save(mobyDickBook);
        bookService.save(hobbitBook);
    }

    public static void populateDataWithOnlyOneBook(
            BookService bookService, PredefinedShelfService predefinedShelfService) {
        Book mobyDickBook = createMobyDickBook(predefinedShelfService);
        bookService.save(mobyDickBook);
    }

    public static void populateDataWithBooksWithoutGenre(
            BookService bookService, PredefinedShelfService predefinedShelfService) {
        Book mobyDickBook = createMobyDickBook(predefinedShelfService);
        Book hobbitBook = createHobbitBook(predefinedShelfService);
        mobyDickBook.setBookGenre(null);
        hobbitBook.setBookGenre(null);
        bookService.save(mobyDickBook);
        bookService.save(hobbitBook);
    }

    public static void populateDataWithBooksWithoutRatings(
            BookService bookService, PredefinedShelfService predefinedShelfService) {
        Book book = createMobyDickBook(predefinedShelfService);
        book.setRating(null);
        bookService.save(book);
    }

    private static Book createBook(String title, Author author, BookGenre genre,
                                   PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = predefinedShelfService.findReadShelf();
        final var book = new Book(title, author, readShelf);
        book.setBookGenre(genre);
        book.setNumberOfPages(2000);
        book.setPagesRead(1000);
        book.setRating(RatingScale.EIGHT_POINT_FIVE);
        return book;
    }

    private static Book createMobyDickBook(PredefinedShelfService predefinedShelfService) {
        final var author = new Author("Herman", "Melville");
        return createBook("Moby Dick", author, BookGenre.FANTASY, predefinedShelfService);
    }

    private static Book createHobbitBook(PredefinedShelfService predefinedShelfService) {
        final var author = new Author("J.R.R", "Tolkien");
        return createBook("The Hobbit", author, BookGenre.ADVENTURE, predefinedShelfService);
    }
}
