package com.karankumar.bookproject.backend.dto;

import com.karankumar.bookproject.backend.model.Book;
import lombok.Data;

import java.util.Set;

@Data
public class ExportBookDto {
    private Set<Book> predefinedShelfBook;
    private Set<Book> userCreatedShelfBook;
}
