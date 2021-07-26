package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.service.UserCreatedShelfService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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


    List<PredefinedShelf> allPredefinedShelves;

    SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userRole;


    @BeforeEach
    void setUp() {
        //mocked user details
        User testUser = User.builder()
                .email("valid@testmail.com")
                .password("aaaaAAAA1234@")
                .build();

        //list of predefined shelves
        PredefinedShelf reading = new PredefinedShelf(PredefinedShelf.ShelfName.READING, testUser);
        PredefinedShelf toRead = new PredefinedShelf(PredefinedShelf.ShelfName.TO_READ, testUser);
        PredefinedShelf didNotFinish = new PredefinedShelf(PredefinedShelf.ShelfName.DID_NOT_FINISH, testUser);
        PredefinedShelf read = new PredefinedShelf(PredefinedShelf.ShelfName.READ, testUser);

        allPredefinedShelves = new ArrayList<>();
        allPredefinedShelves.add(read);
        allPredefinedShelves.add(reading);
        allPredefinedShelves.add(didNotFinish);
        allPredefinedShelves.add(toRead);

        //mocked secured user
        userRole = SecurityMockMvcRequestPostProcessors
                .user("valid@testmail.com")
                .roles("ADMIN", "USER");

    }

    @Test
    @DisplayName("Test 1")
    void testToGetAllPredefinedShelf() throws Exception {
        //when
        when(predefinedShelfService.findAllForLoggedInUser()).thenReturn(allPredefinedShelves);
        LOGGER.info("list of predefined shelf --> {}", shelfController.getAllShelfForLoggedInUser());

        //assert
        assertThat(shelfController.getAllShelfForLoggedInUser().size()).isEqualTo(4);


        RequestBuilder request = get("/api/shelf/predefined/all")
                .with(userRole);

        MvcResult result = mvc.perform(request).andReturn();

        assertEquals("[{\"shelfName\":\"Read\"},{\"shelfName\":\"Reading\"},{\"shelfName\":\"Did not finish\"},{\"shelfName\":\"To read\"}]", result.getResponse().getContentAsString());
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @DisplayName("Test 2")
    void testToGetPredefinedShelfByTheShelf_id() throws Exception {

        //when
        when(predefinedShelfService.
                findById(any(Long.class)))
                .thenReturn(
                        Optional.of(allPredefinedShelves
                                .stream()
                                .findFirst()
                                .get()
                        )
                );

        LOGGER.info("Shelf name is: --> {}", shelfController.getShelfById(0L));

        //assert
        assertThat(shelfController.getShelfById(0L).getShelfName()).isEqualTo("Read");

        RequestBuilder request = get("/api/shelf/predefined/0")
                .with(userRole);

        MvcResult result = mvc.perform(request).andReturn();


        //assert
        assertEquals("{\"shelfName\":\"Read\"}", result.getResponse().getContentAsString());
        assertEquals(200, result.getResponse().getStatus());

    }

    @Test
    @DisplayName("Test 3")
    void getPredefinedShelfByShelfName() throws Exception {
        //when
        when(predefinedShelfService.
                getPredefinedShelfByNameAsString("Read"))
                .thenReturn(
                        Optional.of(allPredefinedShelves
                                .stream()
                                .findFirst()
                                .get()
                        )
                );

        LOGGER.info("Shelf name is: --> {}", shelfController.getPredefinedShelfByShelfName("Read"));

        //assert
        assertThat(shelfController.getPredefinedShelfByShelfName("Read").getShelfName()).isEqualTo("Read");

        RequestBuilder request = get("/api/shelf/predefined/shelfName?name=Read")
                .with(userRole);

        MvcResult result = mvc.perform(request).andReturn();

        //assert
        assertEquals("{\"shelfName\":\"Read\"}", result.getResponse().getContentAsString());
        assertEquals(200, result.getResponse().getStatus());

    }

    @Test
    @DisplayName("Test 3")
    void testToGetShelfByShelfName() {

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