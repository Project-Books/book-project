package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.dto.ExportBookDto;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.UserCreatedShelf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ExportService {
    private final PredefinedShelfService predefinedShelfService;
    private final UserCreatedShelfService userCreatedShelfService;

    public ExportService(PredefinedShelfService predefinedShelfService, UserCreatedShelfService userCreatedShelfService) {
        this.predefinedShelfService = predefinedShelfService;
        this.userCreatedShelfService = userCreatedShelfService;
    }

    @Transactional
    public ExportBookDto exportUserBookData() {
        //Get User's books on predefined Shelf
        ExportBookDto exportBookDto = new ExportBookDto();
        List<PredefinedShelf> userPredefinedShelves = predefinedShelfService.findAllForLoggedInUser();
        exportBookDto.setPredefinedShelfBook(predefinedShelfService.getBooksInPredefinedShelves(userPredefinedShelves));

        List<UserCreatedShelf> userCreatedShelves = userCreatedShelfService.findAllForLoggedInUser();
        exportBookDto.setUserCreatedShelfBook(userCreatedShelfService.getBooksInUserCreatedShelves(userCreatedShelves));
        return exportBookDto;
    }
}
