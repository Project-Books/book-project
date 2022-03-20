/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2021  Karan Kumar
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.karankumar.bookproject.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.karankumar.bookproject.Mappings;
import com.karankumar.bookproject.goal.ReadingGoalController;
import com.karankumar.bookproject.account.dto.UserToRegisterDto;
import com.karankumar.bookproject.BookProjectApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.karankumar.bookproject.goal.ReadingGoalController.TARGET_BAD_REQUEST;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BookProjectApplication.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "../target/snippets")
@Tag("Integration")
class ReadingGoalControllerIntegrationTest {
  @Autowired private MockMvc mockMvc;

  private final String AUTHORIZATION = "Authorization";
  private final String url = "http://localhost:8080";
  private String jwtToken = "";

  @BeforeEach
  void canLoginWithTestUser() throws Exception {
    // given
    UserToRegisterDto userToRegisterDto = new UserToRegisterDto("user@user.user", "password");

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = writer.writeValueAsString(userToRegisterDto);

    // when & then
    MvcResult result =
        this.mockMvc
            .perform(
                post(url + "/login").contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("login"))
            .andReturn();

    jwtToken = result.getResponse().getHeader(AUTHORIZATION);
  }

  @Test
  void getPreviousReadingGoals_returnHttpStatus_whenReadingGoalExists() throws Exception {
    mockMvc
        .perform(
            get(url + Mappings.GOAL + ReadingGoalController.Endpoints.PREVIOUS)
                .header(AUTHORIZATION, jwtToken))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  void addBooksReadingGoal_returnBadRequestHttpStatus_whenWrongTargetInput() throws Exception {
    mockMvc
        .perform(
            post(url + Mappings.GOAL + ReadingGoalController.Endpoints.ADD_BOOKS)
                .header(AUTHORIZATION, jwtToken)
                .param("target", "0"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  void addPagesReadingGoal_returnBadRequestHttpStatus_whenWrongTargetInput() throws Exception {
    mockMvc
        .perform(
            post(url + Mappings.GOAL + ReadingGoalController.Endpoints.ADD_PAGES)
                .header(AUTHORIZATION, jwtToken)
                .param("target", "0"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  void addBooksReadingGoal_returnIsOkRequestHttpStatus_whenCorrectTargetInput() throws Exception {
    mockMvc
        .perform(
            post(url + Mappings.GOAL + ReadingGoalController.Endpoints.ADD_BOOKS)
                .header(AUTHORIZATION, jwtToken)
                .param("target", "1"))
        .andDo(print())
        .andExpect(status().isCreated())
        .andReturn();
  }

  @Test
  void addPagesReadingGoal_returnIsOkRequestHttpStatus_whenCorrectTargetInput() throws Exception {
    mockMvc
        .perform(
            post(url + Mappings.GOAL + ReadingGoalController.Endpoints.ADD_PAGES)
                .header(AUTHORIZATION, jwtToken)
                .param("target", "1"))
        .andDo(print())
        .andExpect(status().isCreated())
        .andReturn();
  }

  @Test
  void deleteReadingGoal_returnIsOkRequestHttpStatus_whenRequestIsDone() throws Exception {
    mockMvc
        .perform(delete(url + Mappings.GOAL).header(AUTHORIZATION, jwtToken))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andReturn();
  }
}
