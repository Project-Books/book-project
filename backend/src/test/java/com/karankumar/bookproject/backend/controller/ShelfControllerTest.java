package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.service.UserCreatedShelfService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ShelfControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ShelfController shelfController;

    @MockBean
    private UserCreatedShelfService userCreatedShelfService;

    @MockBean
    private PredefinedShelfService predefinedShelfService;


    @Test
    void testToGetPredefinedShelfByTheShelf_Id() throws Exception {
        //given
        User  testUser = User.builder()
                .email("valid@testmail.com")
                .password("aaaaAAAA1234@")
                .build();


        PredefinedShelf reading = new PredefinedShelf(PredefinedShelf.ShelfName.READING, testUser);
        PredefinedShelf toRead = new PredefinedShelf(PredefinedShelf.ShelfName.TO_READ, testUser);
        PredefinedShelf didNotFinish = new PredefinedShelf(PredefinedShelf.ShelfName.DID_NOT_FINISH, testUser);
        PredefinedShelf read = new PredefinedShelf(PredefinedShelf.ShelfName.READ, testUser);

        List<PredefinedShelf> allPredefinedShelves = new ArrayList<>();
        allPredefinedShelves.add(read);
        allPredefinedShelves.add(reading);
        allPredefinedShelves.add(didNotFinish);
        allPredefinedShelves.add(toRead);


        //when
        when(predefinedShelfService.findAllForLoggedInUser()).thenReturn(allPredefinedShelves);
        LOGGER.info("list of predefined shelf --> {}", shelfController.getAllShelfForLoggedInUser());

        //assert
        assertThat(shelfController.getAllShelfForLoggedInUser().size()).isEqualTo(4);


        RequestBuilder request = get("/api/shelf/predefined/all")
                .with(SecurityMockMvcRequestPostProcessors
                .user("valid@testmail.com")
                        .roles("ADMIN", "USER"));

        MvcResult result = mvc.perform(request).andReturn();

        assertEquals("[{\"shelfName\":\"Read\"},{\"shelfName\":\"Reading\"},{\"shelfName\":\"Did not finish\"},{\"shelfName\":\"To read\"}]", result.getResponse().getContentAsString());
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getAllShelfForLoggedInUser() {

    }

    @Test
    void getPredefinedShelfByShelfName() {
    }

    @Test
    void getPredefinedShelfByPredefinedShelfName() {
    }

    @Test
    void getToReadShelf() {
    }

    @Test
    void getReadingShelf() {
    }

    @Test
    void getReadShelf() {
    }

    @Test
    void getDidNotFinishShelf() {
    }

    @Test
    void getUserCreatedShelfById() {
    }

    @Test
    void getAllUserCreatedShelf() {
    }

    @Test
    void getAllUserCreatedShelfByName() {
    }

    @Test
    void getAllUserCreatedShelfForLoggedInUser() {
    }

    @Test
    void getUserCreatedShelfByNameForLoggedInUser() {
    }

}