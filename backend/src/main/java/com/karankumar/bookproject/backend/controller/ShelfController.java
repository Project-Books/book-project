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

    @GetMapping("/predefined/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getShelfById(@PathVariable Long id){
        return predefinedShelfService.findById(id).orElseThrow(
                () ->  new IllegalStateException("Shelf not found")
        );
    }
    @GetMapping("/predefined/all")
    @ResponseStatus(HttpStatus.OK)
    public List<PredefinedShelf> getAllShelfForLoggedInUser(){
        return predefinedShelfService.findAllForLoggedInUser();
    }
    @GetMapping("/predefined")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getPredefinedShelfByShelfName(@RequestParam String name){
        String errorMessage = String.format("no shelf matches the shelf name: %s",
                name);
        return predefinedShelfService.getPredefinedShelfByNameAsString(name).orElseThrow(
                () -> new IllegalStateException(errorMessage)
        );
    }

    @GetMapping("/predefined")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getPredefinedShelfByPredefinedShelfName(
            @RequestParam PredefinedShelf.ShelfName predefinedShelfName){
        return predefinedShelfService.getPredefinedShelfByPredefinedShelfName(predefinedShelfName)
                .orElseThrow(() -> new IllegalStateException("Shelf not found"));
    }
    @GetMapping("predefined/to-read")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getToReadShelf(){
        return predefinedShelfService.findReadShelf();
    }
    @GetMapping("/predefined/reading")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getReadingShelf(){
        return predefinedShelfService.findReadingShelf();
    }
    @GetMapping("/predefined/read")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getReadShelf(){
        return predefinedShelfService.findReadShelf();
    }
    @GetMapping("/predefined/unfinished")
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
