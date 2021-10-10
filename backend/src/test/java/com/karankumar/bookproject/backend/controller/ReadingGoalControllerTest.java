package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.ReadingGoal;
import com.karankumar.bookproject.backend.service.ReadingGoalService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.karankumar.bookproject.backend.controller.ReadingGoalController.GOAL_TYPE_NOT_FOUND;
import static com.karankumar.bookproject.backend.controller.ReadingGoalController.TARGET_BAD_REQUEST;
import static com.karankumar.bookproject.backend.model.ReadingGoal.GoalType.BOOKS;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

class ReadingGoalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private ReadingGoalService readingGoalService;
    private ReadingGoalController readingGoalController;
    private List<ReadingGoal> readingGoalList = new ArrayList<>();

    ReadingGoalControllerTest() {
        readingGoalService = mock(ReadingGoalService.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        readingGoalController = new ReadingGoalController(readingGoalService, modelMapper);

        this.readingGoalList.add(new ReadingGoal(5, BOOKS));
    }

    @Test
    void getPreviousReadingGoals_returnReadingGoals_whenPreviousGoalExists(){
        when(readingGoalService.findAll())
                .thenReturn((readingGoalList));

        assertThat(readingGoalController.getPreviousReadingGoals()).isEqualTo(readingGoalList);
    }

    @Test
    void getCurrentReadingGoal_returnResponseStatusException_whenNoReadingGoalExists(){
        when(readingGoalService.findAll()).thenReturn(Collections.emptyList());

        ThrowableAssert.ThrowingCallable callable =
                () -> readingGoalController.getExistingReadingGoal();

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(callable)
                .withMessage(String.format(
                        "%s \"%s\"",
                        HttpStatus.NOT_FOUND,
                        ReadingGoalController.READING_GOAL_NOT_FOUND
                ));
    }

    @Test
    void getCurrentReadingGoal_returnCurrentGoal_whenReadingGoalExists(){
        when(readingGoalService.findAll())
                .thenReturn((readingGoalList));

        assertThat(readingGoalController.getExistingReadingGoal()).isEqualTo(readingGoalList.get(0));
    }

    @Test
    void getPreviousReadingGoals_returnSizeOfReadingGoals_whenNoGoalExist() {
        when(readingGoalService.findAll())
                .thenReturn(Collections.emptyList());
        assertThat(readingGoalController.getPreviousReadingGoals().size()).isZero();
    }

    @Test
    @WithMockUser(username = "user@user.user")
    void getPreviousReadingGoals_returnHttpStatus_whenReadingGoalExists() throws Exception {
        mockMvc.perform(
                get
                        ("/api/goal/previous")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithMockUser(username = "user@user.user")
    void addReadingGoal_returnBadRequestHttpStatus_whenWrongTargetInput() throws Exception {
        mockMvc.perform(
                post
                        ("/api/goal/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("goalType", "books")
                        .param("target", "0")
        )
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithMockUser(username = "user@user.user")
    void addReadingGoals_returnBadRequestHttpStatus_whenWrongGoalTypeInput() throws Exception {
        mockMvc.perform(
                put
                        ("/api/goal/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("goalType", "journal")
                        .param("target", "1")        )
                .andExpect(status().reason(GOAL_TYPE_NOT_FOUND))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @WithMockUser(username = "user@user.user")
    void addReadingGoal_returnIsOkRequestHttpStatus_whenCorrectTargetInput() throws Exception {
        mockMvc.perform(
                post
                        ("/api/goal/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("goalType", "Books")
                        .param("target", "1")
        )
                .andExpect(status().isCreated()).andReturn();
    }

    @Test
    @WithMockUser(username = "user@user.user")
    void updateReadingGoals_returnBadRequestHttpStatus_whenWrongTargetInput() throws Exception {
        mockMvc.perform(
                put
                        ("/api/goal/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("goalType", "books")
                        .param("target", "0")        )
                .andExpect(status().reason(TARGET_BAD_REQUEST))
                .andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    @WithMockUser(username = "user@user.user")
    void updateReadingGoals_returnBadRequestHttpStatus_whenWrongGoalTypeInput() throws Exception {
        mockMvc.perform(
                put
                        ("/api/goal/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("goalType", "sheets")
                        .param("target", "1")        )
                .andExpect(status().reason(GOAL_TYPE_NOT_FOUND))
                .andExpect(status().isNotFound()).andReturn();

    }

    @Test
    @WithMockUser(username = "user@user.user")
    void updateReadingGoal_returnIsOkRequestHttpStatus_whenCorrectTargetInput() throws Exception {
        mockMvc.perform(
                put
                        ("/api/goal/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("goalType", "books")
                        .param("target", "1")        )
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithMockUser(username = "user@user.user")
    void deleteReadingGoal_returnIsOkRequestHttpStatus_whenRequestIsDone() throws Exception {
        mockMvc.perform(
                delete
                        ("/api/goal/delete")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent()).andReturn();
    }
}
