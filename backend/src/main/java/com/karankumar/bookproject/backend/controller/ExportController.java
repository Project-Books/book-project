package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.service.ExportService;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ExportController {

    public final ExportService exportService;

    @Autowired
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping(value = "/export-user-book-data", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String exportUserBookData() throws IOException {
        return exportService.exportBookDataForCurrentUser();
    }
}
