/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.service;

import java.beans.BeanProperty;

import org.springframework.boot.ComandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframeworl.context.annotation.Bean;
import com.karankumar.bookproject.backend.controller.BookController;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonToDbService {
    
   public void importJson(String file){
        return args -> { 
            //read jason and write to db
            ObjectMapper mapper = new ObjectMapper();
            TypeReference <List<Book>> typeReference = new TypeReference<List<Book>>(){};
            InputStream inputStream = typeReference.class.getResourceAsStream(file);
            try { 
                List<Book> books = mapper.readValue(inputStream, typeReference);
                for (Book b: books){
                    bookcontroller.addBook(b);
                }
                System.out.println("Books Saved!");
            } catch (IOException e){
                System.out.println("Unable to save books: " + e.getMessage());
            }
        }; 
    }
}
