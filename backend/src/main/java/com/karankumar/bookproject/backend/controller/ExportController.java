package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    public final ExportService exportService;

    @Autowired
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping( "/user-book-data")
    @ResponseStatus(HttpStatus.OK)
    public String exportUserBookData() throws IOException {
      return exportService.exportBookDataForCurrentUser();
    }
}
