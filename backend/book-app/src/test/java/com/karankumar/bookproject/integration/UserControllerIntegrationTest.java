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
 */

package com.karankumar.bookproject.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.karankumar.bookproject.account.dto.UserToRegisterDto;
import com.karankumar.bookproject.BookProjectApplication;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BookProjectApplication.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "../target/snippets")
@Tag("Integration")
class UserControllerIntegrationTest {
  @Autowired private MockMvc mockMvc;

  @Test
  void canLoginWithTestUser() throws Exception {
    // given
    String url = "http://localhost:8080/login";

    UserToRegisterDto userToRegisterDto = new UserToRegisterDto("user@user.user", "password");

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = writer.writeValueAsString(userToRegisterDto);

    // when & then
    this.mockMvc
        .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(document("login"));
  }

  @Test
  void shouldNotLoginIfWrongCredentials() throws Exception {
    String url = "http://localhost:8080/login";
    UserToRegisterDto userToRegisterDto = new UserToRegisterDto("user@user.user", "userPassword");

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = writer.writeValueAsString(userToRegisterDto);

    // when & then
    this.mockMvc
        .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(print())
        .andExpect(status().is4xxClientError())
        .andDo(document("login"));
  }
}
