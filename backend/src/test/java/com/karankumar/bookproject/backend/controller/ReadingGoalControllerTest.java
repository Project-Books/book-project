package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.ReadingGoal;
import com.karankumar.bookproject.backend.service.ReadingGoalService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.karankumar.bookproject.backend.model.ReadingGoal.GoalType.BOOKS;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-test.properties")
class ReadingGoalControllerTest {
    private final ReadingGoalService readingGoalService;
    private final ReadingGoalController readingGoalController;
    private final List<ReadingGoal> readingGoalList = new ArrayList<>();

    ReadingGoalControllerTest() {
        readingGoalService = mock(ReadingGoalService.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        readingGoalController = new ReadingGoalController(readingGoalService, modelMapper);

        this.readingGoalList.add(new ReadingGoal(5, BOOKS));
    }

    @Test
    void getCurrentReadingGoal_returnResponseStatusException_whenNoReadingGoalExists(){
        when(readingGoalService.findAll()).thenReturn(Collections.emptyList());

        ThrowableAssert.ThrowingCallable callable =
                readingGoalController::getExistingReadingGoal;

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(callable)
                .withMessage(String.format(
                        "%s \"%s\"",
                        HttpStatus.NOT_FOUND,
                        ReadingGoalController.READING_GOAL_NOT_FOUND
                ));
    }

    @Test
    void getPreviousReadingGoals_returnReadingGoals_whenPreviousGoalExists(){
        when(readingGoalService.findAll())
                .thenReturn((readingGoalList));

        assertThat(readingGoalController.getPreviousReadingGoals()).isEqualTo(readingGoalList);
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
}
