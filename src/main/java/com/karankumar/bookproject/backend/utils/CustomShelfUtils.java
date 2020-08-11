package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.service.CustomShelfService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class CustomShelfUtils {
    private final CustomShelfService customShelfService;

    public CustomShelfUtils(CustomShelfService customShelfService) {
        this.customShelfService = customShelfService;
    }

    public List<@NotNull String> getCustomShelfNames() {
        return customShelfService.findAll()
                                 .stream()
                                 .map(CustomShelf::getShelfName)
                                 .collect(Collectors.toList());
    }
}
