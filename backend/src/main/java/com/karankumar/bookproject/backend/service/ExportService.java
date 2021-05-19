package com.karankumar.bookproject.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.karankumar.bookproject.backend.json.PredefinedShelfSerializer;
import com.karankumar.bookproject.backend.model.UserCreatedShelf;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExportService {

    private final BookService bookService;
    private final PredefinedShelfService predefinedShelfService;
    private final UserCreatedShelfService userCreatedShelfService;

    public ExportService(BookService bookService,
                         PredefinedShelfService predefinedShelfService,
                         UserCreatedShelfService userCreatedShelfService) {
        this.bookService = bookService;
        this.predefinedShelfService = predefinedShelfService;
        this.userCreatedShelfService = userCreatedShelfService;
    }

    /**
     * Exports any data that was entered by the User
     *
     *
     *
     */
    @Transactional
    public String exportBookDataForCurrentUser() throws IOException {
        String userPredefinedShelfJSON = mapUserPredefinedShelves();
        String userCreatedShelfJSON = mapUserCreatedShelves();
        //Get User's custom Shelves

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> userBookDataMap = new HashMap<String, String>();
        userBookDataMap.put("userPredefinedShelf", userPredefinedShelfJSON);
        userBookDataMap.put("userCreatedShelf", userCreatedShelfJSON);

        String userBookDataString = mapper.writeValueAsString(userBookDataMap);

        return userBookDataString;
    }

    private String mapUserPredefinedShelves() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module =
                new SimpleModule("PredefinedShelfSerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(PredefinedShelf.class, new PredefinedShelfSerializer());
        mapper.registerModule(module);

        //Get User's books on predefined Shelf
        List<PredefinedShelf> userPredefinedShelf = predefinedShelfService.findAllForLoggedInUser();
        String jsonStr = mapper.writeValueAsString(userPredefinedShelf);

        return jsonStr;
    }

    private String mapUserCreatedShelves() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module =
                new SimpleModule("PredefinedShelfSerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(PredefinedShelf.class, new PredefinedShelfSerializer());
        mapper.registerModule(module);


        //Get User's books on predefined Shelf
        List<UserCreatedShelf> userCustomShelves = userCreatedShelfService.findAllForLoggedInUser();
        String jsonStr = mapper.writeValueAsString(userCustomShelves);

        return jsonStr;
    }
}
