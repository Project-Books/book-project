package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.UserCreatedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.service.UserCreatedShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*Get all shelves (predefined and custom)
        Delete a UserCreatedShelf
        Rename a UserCreatedShelf*/
@RestController
@RequestMapping(value ="/api/shelf")
public class ShelfController {

    @Autowired
    private UserCreatedShelfService userCreatedShelfService;

    @Autowired
    private PredefinedShelfService predefinedShelfService;


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
    @GetMapping("/predefined/shelfName")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getPredefinedShelfByShelfName(@RequestParam("name") String name){
        String errorMessage = String.format("no shelf matches the shelf name: %s",
                name);
        return predefinedShelfService.getPredefinedShelfByNameAsString(name).orElseThrow(
                () -> new IllegalStateException(errorMessage)
        );
    }

    @GetMapping("/predefined")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getPredefinedShelfByPredefinedShelfName(
            @RequestParam(name= "name") PredefinedShelf.ShelfName predefinedShelfName){
        return predefinedShelfService.getPredefinedShelfByPredefinedShelfName(predefinedShelfName)
                .orElseThrow(() -> new IllegalStateException("Shelf not found"));
    }
    @GetMapping("/predefined/to-read")
    @ResponseStatus(HttpStatus.OK)
    public PredefinedShelf getToReadShelf(){
        return predefinedShelfService.findToReadShelf();
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






    @GetMapping("/created-shelves/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserCreatedShelf getUserCreatedShelfById(@PathVariable("id") Long shelfId){
        String errorMessage = String.format("no shelf matches the shelf name: %d",
                shelfId);
        return userCreatedShelfService.findById(shelfId).orElseThrow(
                () -> new IllegalStateException(errorMessage)
        );
    }

    @GetMapping("/created-shelves/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserCreatedShelf> getAllUsersCreatedShelves(){
        return userCreatedShelfService.findAll();
    }

    @GetMapping("/created-shelves/search")
    @ResponseStatus(HttpStatus.OK)
    public List<UserCreatedShelf> getAllUsersCreatedShelvesByName(@RequestParam(name = "shelf-name", required = false) String shelfName) {
        List<UserCreatedShelf> allShelvesByName = userCreatedShelfService.findAll(shelfName);
        if(allShelvesByName.isEmpty()){
            String errorMessage = String.format("no shelf matches the shelf name: %s",
                    shelfName);
            throw new IllegalStateException(errorMessage);
        }
        return allShelvesByName;
    }

    @GetMapping("/created-shelves/user/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserCreatedShelf> getAllUserCreatedShelfForLoggedInUser(){
        List<UserCreatedShelf> loggedInUserCreatedShelves = userCreatedShelfService.findAllForLoggedInUser();
        if(loggedInUserCreatedShelves.isEmpty()){
            throw new IllegalStateException("there is no shelve present at the moment");
        }
        return loggedInUserCreatedShelves;
    }

    @GetMapping("/created-shelves")
    @ResponseStatus(HttpStatus.OK)
    public UserCreatedShelf getUserCreatedShelfByNameForLoggedInUser(@RequestParam(name = "shelf-name") String shelfName) {
        String errorMessage = String.format("no shelf matches the shelf name: %s",
                shelfName);
        return userCreatedShelfService.findByShelfNameAndLoggedInUser(shelfName)
                .orElseThrow(
                        () -> new IllegalStateException(errorMessage)
                );
    }

    @DeleteMapping("created-shelves/delete/all")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAll() {
        userCreatedShelfService.deleteAll();
    }

    @DeleteMapping("created-shelves/delete/{id}")
    public void deleteUserCreatedShelf(@PathVariable(name = "id") Long shelfId) {
        Optional<UserCreatedShelf> shelfToDelete = userCreatedShelfService.findById(shelfId);
        shelfToDelete.ifPresent(userCreatedShelf -> userCreatedShelfService.delete(userCreatedShelf));
    }
}
