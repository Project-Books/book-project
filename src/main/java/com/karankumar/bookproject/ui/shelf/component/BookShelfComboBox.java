package com.karankumar.bookproject.ui.shelf.component;

import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.utils.ShelfUtils;
import com.karankumar.bookproject.ui.shelf.BooksInShelfView;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.util.logging.Level;

@Log
public class BookShelfComboBox {
    private final ComboBox<String> comboBox;

    public BookShelfComboBox(CustomShelfService customShelfService) {
        this.comboBox = new ComboBox<>();

        comboBox.setPlaceholder("Select shelf");
        comboBox.setItems(ShelfUtils.findAllShelfNames(customShelfService.findAll()));
        comboBox.setRequired(true);
    }

    public void bind(BooksInShelfView view) {
        comboBox.addValueChangeListener(event -> {
            String chosenShelf = event.getValue();
            if (chosenShelf == null) {
                LOGGER.log(Level.FINE, "No choice selected");
                return;
            }

            view.chooseShelf(chosenShelf);
            view.updateGrid();

            try {
                view.showOrHideGridColumns(chosenShelf);
            } catch (NotSupportedException e) {
                e.printStackTrace();
            }
        });
    }

    public void addToLayout(HorizontalLayout layout) {
        layout.add(comboBox);
    }
}
