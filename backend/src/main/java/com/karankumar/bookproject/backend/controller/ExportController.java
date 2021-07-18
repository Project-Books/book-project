package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.dto.ExportBookDto;
import com.karankumar.bookproject.backend.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExportController {

    public final ExportService exportService;

    @Autowired
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping( "/export-user-book-data")
    @ResponseStatus(HttpStatus.OK)
    public ExportBookDto exportUserBookData(){
        return exportService.exportUserBookData();
    }
}
