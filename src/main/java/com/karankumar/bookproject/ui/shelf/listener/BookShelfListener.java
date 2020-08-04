package com.karankumar.bookproject.ui.shelf.listener;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.shelf.BooksInShelfView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.util.logging.Level;

@Log
public class BookShelfListener {
    private final BooksInShelfView view;

    public BookShelfListener(BooksInShelfView view) {
        this.view = view;
    }

    private HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<PredefinedShelf.ShelfName>, PredefinedShelf.ShelfName>> listener() {
        return event -> {
            PredefinedShelf.ShelfName chosenShelf = event.getValue();
            if (chosenShelf == null) {
                LOGGER.log(Level.FINE, "No choice selected");
            } else {
                view.chooseShelf(chosenShelf);
                view.updateGrid();
                try {
                    view.showOrHideGridColumns(chosenShelf);
                } catch (NotSupportedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void bind(ComboBox<PredefinedShelf.ShelfName> whichShelf) {
        whichShelf.addValueChangeListener(listener());
    }
}