package com.karankumar.bookproject.ui.shelf.listener;

import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.ui.shelf.CustomShelfForm;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class CustomShelfListener {
    private final CustomShelfService customShelfService;

    public CustomShelfListener(CustomShelfService customShelfService) {
        this.customShelfService = customShelfService;
    }

    public void bind(CustomShelfForm customShelfForm) {
        customShelfForm.addListener(CustomShelfForm.SaveEvent.class, this::saveCustomShelf);
    }

    private void saveCustomShelf(CustomShelfForm.SaveEvent event) {
        if (event.getCustomShelf() != null) {
            customShelfService.save(event.getCustomShelf());
            LOGGER.log(Level.INFO, "Custom shelf saved");
        } else {
            LOGGER.log(Level.SEVERE, "Custom shelf value is null");
        }
    }
}
