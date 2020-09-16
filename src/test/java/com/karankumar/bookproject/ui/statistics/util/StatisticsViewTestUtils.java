package com.karankumar.bookproject.ui.statistics.util;

import com.karankumar.bookproject.backend.entity.*;
import com.karankumar.bookproject.backend.entity.BookGenre;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.statistics.StatisticsView;
import com.karankumar.bookproject.ui.statistics.StatisticsViewTest.Statistic;
import com.karankumar.bookproject.ui.statistics.StatisticsViewTest.StatisticNotFound;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.dom.Element;

import java.util.function.Predicate;

public class StatisticsViewTestUtils {

    private StatisticsViewTestUtils() {}

    public static Statistic getStatistic(StatisticsView.StatisticType statisticType, StatisticsView statisticsView) {
        try {
            Element verticalLayoutInDiv = getVerticalLayoutWithStatistic(statisticType, statisticsView);
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

    private static Element getVerticalLayoutWithStatistic(StatisticsView.StatisticType statisticType, StatisticsView statisticsView) {
        var divContainingStatistic = statisticsView.getChildren()
                                                            .filter(textWithinH3EqualsTo(statisticType.getCaption()))
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

    public static void populateDataWithBooks(BookService bookService, PredefinedShelfService predefinedShelfService) {
        Book book = createMobyDickBook(predefinedShelfService);
        bookService.save(book);
    }
    
    public static void populateDataWithBooksWithoutPageCount(BookService bookService, PredefinedShelfService predefinedShelfService) {
        Book book = createMobyDickBook(predefinedShelfService);
        book.setNumberOfPages(null);
        bookService.save(book);
    }

    public static void populateDataWithBooksWithoutGenre(BookService bookService, PredefinedShelfService predefinedShelfService) {
        Book book = createMobyDickBook(predefinedShelfService);
        book.setBookGenre(null);
        bookService.save(book);
    }

    public static void populateDataWithBooksWithoutRatings(BookService bookService, PredefinedShelfService predefinedShelfService) {
        Book book = createMobyDickBook(predefinedShelfService);
        book.setRating(null);
        bookService.save(book);
    }

    private static PredefinedShelf getReadShelf(PredefinedShelfService predefinedShelfService) {
        PredefinedShelfUtils predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        return predefinedShelfUtils.findReadShelf();
    }

    private static Book createMobyDickBook(PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = getReadShelf(predefinedShelfService);
        Author author = new Author("Herman", "Melville");
        Book book = new Book("Moby Dick", author, readShelf);
        book.setBookGenre(BookGenre.ADVENTURE);
        book.setNumberOfPages(2000);
        book.setPagesRead(1000);
        book.setRating(RatingScale.EIGHT_POINT_FIVE);
        return book;
    }
}
