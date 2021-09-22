package com.karankumar.bookproject.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.karankumar.bookproject.backend.json.PredefinedShelfSerializer;
import com.karankumar.bookproject.backend.json.UserCreatedShelfSerializer;
import com.fasterxml.jackson.core.Version;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.UserCreatedShelf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

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
    public String exportBookDataForCurrentUser() throws IOException {
        JsonNode userPredefinedShelfJSON = mapUserPredefinedShelves();

        String userCreatedShelfJSON = mapUserCreatedShelves();

        //Get User's Shelves
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> userBookDataMap = new HashMap<String, Object>();
        userBookDataMap.put("userPredefinedShelf", userPredefinedShelfJSON);
        userBookDataMap.put("userCreatedShelf", userCreatedShelfJSON);
        return mapper.writeValueAsString(userBookDataMap);
    }

    private JsonNode mapUserPredefinedShelves() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("PredefinedShelfSerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(PredefinedShelf.class, new PredefinedShelfSerializer());
        mapper.registerModule(module);

        //Get all User's books on predefined Shelf
        List<PredefinedShelf> userPredefinedShelf = predefinedShelfService.findAllForLoggedInUser();
        return mapper.valueToTree(userPredefinedShelf);
    }

    private String mapUserCreatedShelves() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("PredefinedShelfSerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(UserCreatedShelf.class, new UserCreatedShelfSerializer());
        mapper.registerModule(module);

        //Get all User's books on predefined Shelf
        List<UserCreatedShelf> userCustomShelves = userCreatedShelfService.findAllForLoggedInUser();
        return mapper.writeValueAsString(userCustomShelves);
    }
}