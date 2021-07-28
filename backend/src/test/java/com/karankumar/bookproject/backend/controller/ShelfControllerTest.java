package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.UserCreatedShelf;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    List<UserCreatedShelf> allUsersCreatedShelves;
    SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor authenticatedUser;

    final String PREDEFINED_SHELF_BASE_URL = "/api/shelf/predefined";
    final String USER_DEFINED_SHELF_BASE_URL = "/api/shelf/created-shelves";


    @BeforeEach
    void setUp() {
        //mocked user details
        User testUser1 = User.builder()
                .email("valid1@testmail.com")
                .password("aaaaAAAA1234@")
                .build();
        User testUser2 = User.builder()
                .email("valid2@testmail.com")
                .password("aaaaAAAA1234@")
                .build();
        //list of predefined shelves
        allPredefinedShelves = Arrays.asList(
                new PredefinedShelf(PredefinedShelf.ShelfName.READ, testUser1),
                new PredefinedShelf(PredefinedShelf.ShelfName.READING, testUser1),
                new PredefinedShelf(PredefinedShelf.ShelfName.TO_READ, testUser1),
                new PredefinedShelf(PredefinedShelf.ShelfName.DID_NOT_FINISH, testUser1));

        //list of users defined shelves
        allUsersCreatedShelves = Arrays.asList(
                new UserCreatedShelf("motivation", testUser1),
                new UserCreatedShelf("inspirational", testUser1),
                new UserCreatedShelf("spiritual", testUser2),
                new UserCreatedShelf("computer science", testUser2),
                new UserCreatedShelf("inspirational", testUser2));

        //mocked secured user
        authenticatedUser = SecurityMockMvcRequestPostProcessors
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
                .with(authenticatedUser);

        MvcResult result = mvc.perform(request).andReturn();

        assertEquals("[{\"shelfName\":\"Read\"},{\"shelfName\":\"Reading\"},{\"shelfName\":\"To read\"},{\"shelfName\":\"Did not finish\"}]",
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
                .with(authenticatedUser);

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

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/shelf-name?name=Read")
                .with(authenticatedUser);

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
    void testToThrowExceptionWhenShelfNotFound(){
        when(predefinedShelfService
                .getPredefinedShelfByNameAsString(any(String.class)))
                .thenThrow(new IllegalStateException("no shelf matches the shelf name: ReadMe"));

        assertThrows(IllegalStateException.class,
                ()-> shelfController.getPredefinedShelfByShelfName("Read Me"));
    }

    @Test
    @DisplayName("Get Shelf By Predefined ShelfName")
    void testToGetShelfByShelfName() throws Exception {
        //when
        when(predefinedShelfService.
                getPredefinedShelfByPredefinedShelfName(PredefinedShelf.ShelfName.READ))
                .thenReturn(
                        allPredefinedShelves
                            .stream()
                            .findFirst()
                );

        LOGGER.info("Shelf name is: --> {}", shelfController.getPredefinedShelfByPredefinedShelfName(PredefinedShelf.ShelfName.READ));

        //assert
        assertThat(shelfController
                .getPredefinedShelfByPredefinedShelfName(
                        PredefinedShelf.ShelfName.READ
                ).getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.READ.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"?name=READ")
                .with(authenticatedUser);

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
        ).thenReturn(allPredefinedShelves.get(2));

        LOGGER.info("Shelf name is: --> {}", shelfController.getToReadShelf());

//        assert
        assertThat(shelfController
                .getToReadShelf()
                .getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.TO_READ.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/to-read")
                .with(authenticatedUser);

        MvcResult result = mvc.perform(request).andReturn();



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
        ).thenReturn(allPredefinedShelves.get(0));

        LOGGER.info("Shelf name is: --> {}", shelfController.getReadShelf());

//        assert
        assertThat(shelfController
                .getReadShelf()
                .getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.READ.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/read")
                .with(authenticatedUser);

        MvcResult result = mvc.perform(request).andReturn();



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
        ).thenReturn(allPredefinedShelves.get(1));

        LOGGER.info("Shelf name is: --> {}", shelfController.getToReadShelf());

//        assert
        assertThat(shelfController
                .getReadingShelf()
                .getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.READING.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/reading")
                .with(authenticatedUser);

        MvcResult result = mvc.perform(request).andReturn();


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
        ).thenReturn(allPredefinedShelves.get(3));

        LOGGER.info("Shelf name is: --> {}", shelfController.getDidNotFinishShelf());

//        assert
        assertThat(shelfController
                .getDidNotFinishShelf()
                .getShelfName()
        ).isEqualTo(PredefinedShelf.ShelfName.DID_NOT_FINISH.toString());

        RequestBuilder request = get(PREDEFINED_SHELF_BASE_URL+"/unfinished")
                .with(authenticatedUser);

        MvcResult result = mvc.perform(request).andReturn();



//        assert
        assertEquals("{\"shelfName\":\"Did not finish\"}", result.getResponse().getContentAsString());
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }

    @Test
    void testToGetUserCreatedShelfById() throws Exception {

        //when
        when(userCreatedShelfService.findById(any(Long.class))
        ).thenReturn(Optional.of(
                new UserCreatedShelf())
        );

        LOGGER.info("Shelf is: --> {}", shelfController.getUserCreatedShelfById(0L));

//        assert
        assertThat(shelfController
                .getUserCreatedShelfById(0L)
        ).isNotNull();


        RequestBuilder request = get(USER_DEFINED_SHELF_BASE_URL+"/0")
                .with(authenticatedUser);

        MvcResult result = mvc.perform(request).andReturn();

//        assert
        assertThat(result.getResponse()).isNotNull();
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }

    @Test
    void getAllUsersCreatedShelves() throws Exception {
        //when
        when(userCreatedShelfService.findAll()
        ).thenReturn(allUsersCreatedShelves);

        LOGGER.info("All Shelves: --> {}", shelfController.getAllUsersCreatedShelves());

//        assert
        assertThat(shelfController
                .getAllUsersCreatedShelves().size()
        ).isEqualTo(5);


        RequestBuilder request = get(USER_DEFINED_SHELF_BASE_URL+"/all")
                .with(authenticatedUser);

        MvcResult result = mvc.perform(request).andReturn();

//        assert
        assertThat(result.getResponse()).isNotNull();
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }

    @Test
    void getAllUserCreatedShelfByName() throws Exception {
        List<UserCreatedShelf> searchedShelves =
                allUsersCreatedShelves
                        .stream()
                        .filter(
                                shelves -> shelves.getShelfName()
                                        .equals("inspirational"))
                        .collect(Collectors.toList());

        when(userCreatedShelfService.findAll("inspirational")
        ).thenReturn(searchedShelves);

        LOGGER.info("All Shelves: with name {INSPIRATIONAL}: --> {}", shelfController.
                getAllUsersCreatedShelvesByName("inspirational")
        );

//        assert
        assertThat(shelfController
                .getAllUsersCreatedShelvesByName("inspirational").size()
        ).isEqualTo(2);


        RequestBuilder request = get(USER_DEFINED_SHELF_BASE_URL+"/search?shelf-name=inspirational")
                .with(authenticatedUser);

        MvcResult result = mvc.perform(request).andReturn();

//        assert
        LOGGER.info("\n\n{}",result.getResponse().getContentAsString());
        assertThat(result.getResponse()).isNotNull();
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }

    @Test
    void getAllUserCreatedShelfForLoggedInUser() throws Exception {
        User testUser1 = User.builder()
                .email("valid1@testmail.com")
                .password("aaaaAAAA1234@")
                .build();

        List<UserCreatedShelf> loggedInUserCreatedShelves =
                allUsersCreatedShelves
                        .stream()
                        .filter(
                                shelves -> shelves.getUser()
                                        .equals(testUser1))
                        .collect(Collectors.toList());

        when(userCreatedShelfService.findAllForLoggedInUser()
        ).thenReturn(loggedInUserCreatedShelves);

        LOGGER.info("All Shelves: with name {INSPIRATIONAL}: --> {}", shelfController.
                getAllUserCreatedShelfForLoggedInUser()
        );

//        assert
        assertThat(shelfController
                .getAllUserCreatedShelfForLoggedInUser().size()
        ).isEqualTo(2);


        RequestBuilder request = get(USER_DEFINED_SHELF_BASE_URL+"/user/all")
                .with(authenticatedUser);

        MvcResult result = mvc.perform(request).andReturn();

//        assert
        LOGGER.info("\n\n{}",result.getResponse().getContentAsString());
        assertThat(result.getResponse()).isNotNull();
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }

    @Test
    void getUserCreatedShelfByNameForLoggedInUser() throws Exception {


        UserCreatedShelf loggedInUserCreatedShelves =
                allUsersCreatedShelves.stream().findFirst().orElseThrow();

        when(userCreatedShelfService.findByShelfNameAndLoggedInUser("motivation")
        ).thenReturn(Optional.of(loggedInUserCreatedShelves));

        System.out.println(userCreatedShelfService.findByShelfNameAndLoggedInUser("motivation"));

        LOGGER.info("User created shelf: --> {}", shelfController.
                getUserCreatedShelfByNameForLoggedInUser("motivation")
        );

//        assert
        assertThat(shelfController
                .getUserCreatedShelfByNameForLoggedInUser("motivation").getShelfName()
        ).isEqualTo("motivation");


        RequestBuilder request = get(USER_DEFINED_SHELF_BASE_URL+"?shelf-name=motivation")
                .with(authenticatedUser);

        MvcResult result = mvc.perform(request).andReturn();

//        assert
        LOGGER.info("\n\n{}",result.getResponse().getContentAsString());
        assertThat(result.getResponse()).isNotNull();
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }
    @Test
    void testToDeleteAllUserDefinedShelves() throws Exception {
        when(userCreatedShelfService.findAll()
        ).thenReturn(allUsersCreatedShelves);

        assertEquals(5 ,shelfController.getAllUsersCreatedShelves().size());
        doAnswer(
                invocation -> when(userCreatedShelfService.findAll())
                        .thenReturn(new ArrayList<>())
        ).when(userCreatedShelfService).deleteAll();

        shelfController.deleteAll();
        assertEquals(0, shelfController.getAllUsersCreatedShelves().size());

        RequestBuilder request = delete(USER_DEFINED_SHELF_BASE_URL+ "/delete/all")
                .with(authenticatedUser);
        MvcResult result = mvc.perform(request).andReturn();

        assertThat(result.getResponse().getContentLength()).isEqualTo(0);
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }

    @Test
    void testToDeleteAParticularUserCreatedShelf() throws Exception {
        //when
        when(userCreatedShelfService.findById(any(Long.class))).thenReturn(allUsersCreatedShelves.stream().findFirst());


        //given
        Optional<UserCreatedShelf> shelfToDelete = userCreatedShelfService.findById(0L);

        //when
        when(userCreatedShelfService.findAll()
        ).thenReturn(allUsersCreatedShelves);

        //assert
        assertEquals(5 ,shelfController.getAllUsersCreatedShelves().size());
        //do
        doAnswer(
                invocation -> when(userCreatedShelfService.findAll())
                        .thenReturn(allUsersCreatedShelves.subList(1, 5))
        ).when(userCreatedShelfService).delete(shelfToDelete.orElseThrow());

        //when
        shelfController.deleteUserCreatedShelf(0L);
        assertEquals(4, shelfController.getAllUsersCreatedShelves().size());

        RequestBuilder request = delete(USER_DEFINED_SHELF_BASE_URL+ "/delete/0")
                .with(authenticatedUser);
        MvcResult result = mvc.perform(request).andReturn();

        assertThat(result.getResponse()).isNotNull();
        assertEquals(200,
                result
                        .getResponse()
                        .getStatus()
        );
    }

}