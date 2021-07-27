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

    final String PREDEFINED_SHELF_BASE_URL = "/api/shelf/predefined";
    final String USER_DEFINED_SHELF_BASE_URL = "/api/shelf/created-shelves";

    PredefinedShelf reading;
    PredefinedShelf toRead;
    PredefinedShelf didNotFinish;
    PredefinedShelf read;

    @BeforeEach
    void setUp() {
        //mocked user details
        User testUser = User.builder()
                .email("valid@testmail.com")
                .password("aaaaAAAA1234@")
                .build();
        //list of predefined shelves
        reading = new PredefinedShelf(PredefinedShelf.ShelfName.READING, testUser);
        toRead = new PredefinedShelf(PredefinedShelf.ShelfName.TO_READ, testUser);
        didNotFinish = new PredefinedShelf(PredefinedShelf.ShelfName.DID_NOT_FINISH, testUser);
        read = new PredefinedShelf(PredefinedShelf.ShelfName.READ, testUser);

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
    @DisplayName("Get All Predefined Shelves")
    void testToGetAllPredefinedShelf() throws Exception {
        //when
        when(predefinedShelfService.findAllForLoggedInUser()).thenReturn(allPredefinedShelves);
        LOGGER.info("list of predefined shelf --> {}", shelfController.getAllShelfForLoggedInUser());

        //assert
        assertThat(shelfController
                .getAllShelfForLoggedInUser()
                .size()
        ).isEqualTo(4);


        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/all")
                .with(userRole);

        MvcResult result = mvc.perform(request).andReturn();

        assertEquals("[{\"shelfName\":\"Read\"},{\"shelfName\":\"Reading\"},{\"shelfName\":\"Did not finish\"},{\"shelfName\":\"To read\"}]",
                result.getResponse().getContentAsString());
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }

    @Test
    @DisplayName("Get Predefined Shelf By Shelf Id")
    void testToGetPredefinedShelfByTheShelf_id() throws Exception {

        //when
        when(predefinedShelfService.
                findById(any(Long.class)))
                .thenReturn(
                        allPredefinedShelves
                                .stream()
                                .findFirst()
                );

        LOGGER.info("Shelf name is: --> {}", shelfController.getShelfById(0L));

        //assert
        assertThat(shelfController
                .getShelfById(0L)
                .getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.READ.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/0")
                .with(userRole);

        MvcResult result = mvc.perform(request).andReturn();


        //assert
        assertEquals("{\"shelfName\":\"Read\"}",
                result.getResponse().getContentAsString());
        assertEquals(200,
            result
                .getResponse()
                .getStatus()
        );

    }

    @Test
    @DisplayName("Get Predefined Shelf By Shelf Name")
    void getPredefinedShelfByShelfName() throws Exception {
        //when
        when(predefinedShelfService.
                getPredefinedShelfByNameAsString("Read"))
                .thenReturn(
                    allPredefinedShelves
                        .stream()
                        .findFirst()
                );

        LOGGER.info("Shelf name is: --> {}", shelfController.getPredefinedShelfByShelfName("Read"));

        //assert
        assertThat(shelfController
                .getPredefinedShelfByShelfName("Read")
                .getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.READ.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/shelfName?name=Read")
                .with(userRole);

        MvcResult result = mvc.perform(request).andReturn();

        //assert
        assertEquals("{\"shelfName\":\"Read\"}", result.getResponse().getContentAsString());
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );

    }

    @Test
    @DisplayName("Get Shelf By Predefined ShelfName")
    void testToGetShelfByShelfName() throws Exception {
        //when
        when(predefinedShelfService.
                getPredefinedShelfByPredefinedShelfName(PredefinedShelf.ShelfName.READ))
                .thenReturn(
                        Optional.of(allPredefinedShelves
                                .stream()
                                .findFirst()
                                .get()
                        )
                );

        LOGGER.info("Shelf name is: --> {}", shelfController.getPredefinedShelfByPredefinedShelfName(PredefinedShelf.ShelfName.READ));

        //assert
        assertThat(shelfController
                .getPredefinedShelfByPredefinedShelfName(
                        PredefinedShelf.ShelfName.READ
                ).getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.READ.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"?name=READ")
                .with(userRole);

        MvcResult result = mvc.perform(request).andReturn();

        //assert
        assertEquals("{\"shelfName\":\"Read\"}", result.getResponse().getContentAsString());
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );

    }

    @Test
    @DisplayName("Get To_Read Shelf")
    void testToGetToReadShelf() throws Exception {
        //when
        when(predefinedShelfService
                .findToReadShelf()
        ).thenReturn(toRead);

        LOGGER.info("Shelf name is: --> {}", shelfController.getToReadShelf());

//        assert
        assertThat(shelfController
                .getToReadShelf()
                .getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.TO_READ.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/to-read")
                .with(userRole);

        MvcResult result = mvc.perform(request).andReturn();

        System.out.println(result.getResponse().getContentAsString());


//        assert
        assertEquals("{\"shelfName\":\"To read\"}", result.getResponse().getContentAsString());
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }

    @Test
    void testToGetReadShelf() throws Exception {
        //when
        when(predefinedShelfService
                .findReadShelf()
        ).thenReturn(read);

        LOGGER.info("Shelf name is: --> {}", shelfController.getReadShelf());

//        assert
        assertThat(shelfController
                .getReadShelf()
                .getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.READ.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/read")
                .with(userRole);

        MvcResult result = mvc.perform(request).andReturn();

        System.out.println(result.getResponse().getContentAsString());


//        assert
        assertEquals("{\"shelfName\":\"Read\"}", result.getResponse().getContentAsString());
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }

    @Test
    void testToGetReadingShelf() throws Exception {
        //when
        when(predefinedShelfService
                .findReadingShelf()
        ).thenReturn(reading);

        LOGGER.info("Shelf name is: --> {}", shelfController.getToReadShelf());

//        assert
        assertThat(shelfController
                .getReadingShelf()
                .getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.READING.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/reading")
                .with(userRole);

        MvcResult result = mvc.perform(request).andReturn();

        System.out.println(result.getResponse().getContentAsString());


//        assert
        assertEquals("{\"shelfName\":\"Reading\"}", result.getResponse().getContentAsString());
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }

    @Test
    void toToGetDidNotFinishShelf() throws Exception {
        //when
        when(predefinedShelfService
                .findDidNotFinishShelf()
        ).thenReturn(didNotFinish);

        LOGGER.info("Shelf name is: --> {}", shelfController.getDidNotFinishShelf());

//        assert
        assertThat(shelfController
                .getDidNotFinishShelf()
                .getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.DID_NOT_FINISH.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/unfinished")
                .with(userRole);

        MvcResult result = mvc.perform(request).andReturn();

        System.out.println(result.getResponse().getContentAsString());


//        assert
        assertEquals("{\"shelfName\":\"Did not finish\"}", result.getResponse().getContentAsString());
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
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