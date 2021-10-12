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

package com.karankumar.bookproject.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.karankumar.bookproject.backend.dto.UserToRegisterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.karankumar.bookproject.backend.controller.ReadingGoalController.GOAL_TYPE_NOT_FOUND;
import static com.karankumar.bookproject.backend.controller.ReadingGoalController.TARGET_BAD_REQUEST;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
@Tag("Integration")
class ReadingGoalControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    String url = "http://localhost:8080";
    String jwtToken = "";

    @BeforeEach
    void canLoginWithTestUser() throws Exception {
        // given
        UserToRegisterDto userToRegisterDto = new UserToRegisterDto(
                "user@user.user",
                "password"
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = writer.writeValueAsString(userToRegisterDto);

        // when & then
        MvcResult result = this.mockMvc.perform(post(url + "/login").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("login"))
                .andReturn();

        jwtToken = result.getResponse().getHeader("Authorization");
    }

    @Test
    void getPreviousReadingGoals_returnHttpStatus_whenReadingGoalExists() throws Exception {
        mockMvc.perform(get(url + "/api/goal/previous")
                .header("Authorization", jwtToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void addReadingGoal_returnBadRequestHttpStatus_whenWrongTargetInput() throws Exception {
        mockMvc.perform(post(url + "/api/goal/add")
                .header("Authorization", jwtToken)
                .param("goalType", "books")
                .param("target", "0"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addReadingGoals_returnBadRequestHttpStatus_whenWrongGoalTypeInput() throws Exception {
        mockMvc.perform(put(url + "/api/goal/update")
                .header("Authorization", jwtToken)
                .param("goalType", "journal")
                .param("target", "1"))
                .andDo(print())
                .andExpect(status().reason(GOAL_TYPE_NOT_FOUND))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user@user.user")
    void addReadingGoal_returnIsOkRequestHttpStatus_whenCorrectTargetInput() throws Exception {
        mockMvc.perform(post(url + "/api/goal/add")
                .header("Authorization", jwtToken)
                .param("goalType", "Books")
                .param("target", "1"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void updateReadingGoals_returnBadRequestHttpStatus_whenWrongTargetInput() throws Exception {
        mockMvc.perform(put(url + "/api/goal/update")
                .header("Authorization", jwtToken)
                .param("goalType", "books")
                .param("target", "0"))
                .andDo(print())
                .andExpect(status().reason(TARGET_BAD_REQUEST))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void updateReadingGoals_returnBadRequestHttpStatus_whenWrongGoalTypeInput() throws Exception {
        mockMvc.perform(put(url + "/api/goal/update")
                .header("Authorization", jwtToken)
                .param("goalType", "sheets")
                .param("target", "1"))
                .andDo(print())
                .andExpect(status().reason(GOAL_TYPE_NOT_FOUND))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void updateReadingGoal_returnIsOkRequestHttpStatus_whenCorrectTargetInput() throws Exception {
        mockMvc.perform(put(url + "/api/goal/update")
                .header("Authorization", jwtToken)
                .param("goalType", "books")
                .param("target", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void deleteReadingGoal_returnIsOkRequestHttpStatus_whenRequestIsDone() throws Exception {
        mockMvc.perform(delete(url + "/api/goal/delete")
                .header("Authorization", jwtToken))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
