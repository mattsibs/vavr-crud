package com.dm.vavr.crud.controller;

import com.dm.vavr.crud.VavrCrudApplication;
import com.dm.vavr.crud.data.User;
import com.dm.vavr.crud.service.UserDto;
import com.dm.vavr.crud.service.UserService;
import com.dm.vavr.crud.service.failure.NotFoundFailure;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import io.vavr.control.Either;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VavrCrudApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    /**
     * Verification of method calls have not been tested in these tests, project for demo purposes
     */

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void findAll_ReturnsAllUsers() throws Exception {

        when(userService.findAll())
                .thenReturn(ImmutableList.of(
                        new User("id1", "username", "email", "password")));

        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo("id1")))
                .andExpect(jsonPath("$[0].username", equalTo("username")))
                .andExpect(jsonPath("$[0].email", equalTo("email")))
                .andExpect(jsonPath("$[0].password", equalTo("password")));
    }

    @Test
    public void findByIds_NotFound_ReturnsBadRequest() throws Exception {

        when(userService.findByIds(any()))
                .thenReturn(Either.left(NotFoundFailure.many("User", ImmutableList.of("id1", "id2"))));

        this.mockMvc.perform(get("/users?ids=id1,id2,idn"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errorCode", equalTo("USER_NOT_FOUND")))
                .andExpect(jsonPath("$[0].errorMessage",
                        equalTo("Could not find User for given ids [id1, id2]")));
    }

    @Test
    public void findByIds_Found_ReturnsAllUsers() throws Exception {

        when(userService.findByIds(any()))
                .thenReturn(Either.right(ImmutableList.of(
                        new User("id1", "username", "email", "password"))));

        this.mockMvc.perform(get("/users?ids=id1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo("id1")))
                .andExpect(jsonPath("$[0].username", equalTo("username")))
                .andExpect(jsonPath("$[0].email", equalTo("email")))
                .andExpect(jsonPath("$[0].password", equalTo("password")));
    }

    @Test
    public void findById_Found_ReturnsAllUsers() throws Exception {

        when(userService.findById(any()))
                .thenReturn(Either.right(new User("id1", "username", "email", "password")));

        this.mockMvc.perform(get("/users/id1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("id1")))
                .andExpect(jsonPath("$.username", equalTo("username")))
                .andExpect(jsonPath("$.email", equalTo("email")))
                .andExpect(jsonPath("$.password", equalTo("password")));
    }

    @Test
    public void findById_NotFound_ReturnsBadRequest() throws Exception {

        when(userService.findById(any()))
                .thenReturn(Either.left(NotFoundFailure.one("User", "id1")));

        this.mockMvc.perform(get("/users/id1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errorCode", equalTo("USER_NOT_FOUND")))
                .andExpect(jsonPath("$[0].errorMessage",
                        equalTo("Could not find User for given ids [id1]")));
    }

    @Test
    public void create_ReturnsId() throws Exception {

        when(userService.create(any()))
                .thenReturn(new User("id1", "username", "email", "password"));

        this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDto("newUser", "email"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("id1")));
    }

    @Test
    public void update_Found_ReturnsId() throws Exception {

        when(userService.update(any(), any()))
                .thenReturn(Either.right(new User("id1", "newUsername", "email", "password")));

        this.mockMvc.perform(
                put("/users/id1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UserDto("newUsername", "newEmail"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("id1")));
    }


    @Test
    public void update_NotFound_ReturnsBadRequest() throws Exception {
        when(userService.update(any(), any()))
                .thenReturn(Either.left(NotFoundFailure.one("User", "id1")));

        this.mockMvc.perform(
                put("/users/id1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDto("newUsername", "newEmail"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errorCode", equalTo("USER_NOT_FOUND")))
                .andExpect(jsonPath("$[0].errorMessage",
                        equalTo("Could not find User for given ids [id1]")));
    }

    @Test
    public void delete_Found_ReturnsId() throws Exception {

        when(userService.delete(any()))
                .thenReturn(Either.right(new User("id1", "newUsername", "email", "password")));

        this.mockMvc.perform(delete("/users/id1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("id1")));
    }

    @Test
    public void delete_NotFound_ReturnsBadRequest() throws Exception {

        when(userService.delete(any()))
                .thenReturn(Either.left(NotFoundFailure.one("User", "id1")));

        this.mockMvc.perform(delete("/users/id1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errorCode", equalTo("USER_NOT_FOUND")))
                .andExpect(jsonPath("$[0].errorMessage",
                        equalTo("Could not find User for given ids [id1]")));
    }

}