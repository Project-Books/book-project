package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.UserCreatedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.service.UserCreatedShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*Get all shelves (predefined and custom)
        Delete a UserCreatedShelf
        Rename a UserCreatedShelf*/
@RestController
@RequestMapping(value ="/api/shelf")
public class ShelfController {
    private final UserCreatedShelfService userCreatedShelfService;
    private final PredefinedShelfService predefinedShelfService;

    @Autowired
    public ShelfController(UserCreatedShelfService userCreatedShelfService, PredefinedShelfService predefinedShelfService) {
        this.userCreatedShelfService = userCreatedShelfService;
        this.predefinedShelfService = predefinedShelfService;
    }

    @GetMapping("/predefined/all")
    @ResponseStatus(HttpStatus.OK)
    public List<PredefinedShelf> getAllShelf(){
        return predefinedShelfService.findAllForLoggedInUser();
    }

    @GetMapping("/all/to-read")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getToReadShelf(){
        return predefinedShelfService.findReadShelf();
    }
    @GetMapping("/all/reading")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getReadingShelf(){
        return predefinedShelfService.findReadingShelf();
    }
    @GetMapping("/all/read")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getReadShelf(){
        return predefinedShelfService.findReadShelf();
    }
    @GetMapping("/all/unfinished")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getDidNotFinishShelf(){
        return predefinedShelfService.findDidNotFinishShelf();
    }




    @GetMapping("/custom/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserCreatedShelf> getAllCustomShelf(){
        return userCreatedShelfService.findAll();
    }

    @GetMapping("/custom/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserCreatedShelf> getAllCustomShelf(@RequestParam String shelfName) {
        List<UserCreatedShelf> allShelvesByName = userCreatedShelfService.findAll(shelfName);
        if(allShelvesByName.isEmpty()){
            String errorMessage = String.format("no shelf matches the shelf name: %s",
                    shelfName);
            throw new IllegalStateException(errorMessage);
        }
        return allShelvesByName;
    }


     


}
