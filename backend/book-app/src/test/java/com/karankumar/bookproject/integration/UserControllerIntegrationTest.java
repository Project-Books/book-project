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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    @Autowired
    private MockMvc mockMvc;

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

    @ParameterizedTest
    @ValueSource(strings = {"alphabeta", " ", "password", "p@ssword", "password1", "12345", "qwert12345", "helloworld",
            "aweoijr", "Nh1-orWL", "ZBciM|1J", "Vl1u-DFw", "ntzyj5", "aXog*f", "BoXEvYqY", "6scb1x16", "-%%?|$@^",
            "64186660", "+&9=2^", "PASSWORD", "pass word"})
    void cannotRegisterWithWeakPassword(String password) throws Exception {
        String url = "http://localhost:8080/api/user";

        // set up user with given username and password
        UserToRegisterDto userToRegisterDto = new UserToRegisterDto("user1@user.user", password);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = writer.writeValueAsString(userToRegisterDto);

        // expect error if password is weak
        this.mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andDo(document("register-with-weak-password"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"bzoYMvAhIGSP", "hsdtcaslaqcyxiisa", "fmL9398#i^Uo", "2E^U9|Dg56", "-^XhKFWcwJ",
            "r1lz8BmAVt", "PL!MeS34#*", "Pa332w0r9", "ISDJFISOPS", "&^=?$&?%-!", "1434987243", "23&?7&^96",
            "password11298735", "hello there", "kermit the frog"})
    void canRegisterWithStrongPassword(String password) throws Exception {
        String url = "http://localhost:8080/api/user";

        // set up user with given username and password
        UserToRegisterDto userToRegisterDto = new UserToRegisterDto("user@user.user", password);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = writer.writeValueAsString(userToRegisterDto);

        // expect strong password to be accepted
        this.mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("register-with-strong-password"));
    }

}
