package org.cloud.tutorials.controller;

import net.minidev.json.JSONObject;
import org.cloud.tutorials.dto.UserDto;
import org.cloud.tutorials.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.cloud.tutorials.CommonConstants.EMAIL;
import static org.cloud.tutorials.CommonConstants.FIRST_NAME;
import static org.cloud.tutorials.CommonConstants.ID;
import static org.cloud.tutorials.CommonConstants.LAST_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    private List<UserDto> userDtoList;

    @BeforeEach
    public void setup() {
        userDtoList = new ArrayList<>();
        userDtoList.add(new UserDto(ID, FIRST_NAME, LAST_NAME, EMAIL));
    }

    @Test
    @DisplayName("Test that it returns a list all users")
    void getUsers() throws Exception {
        userDtoList.add(new UserDto(2L, "testName2","testSurname2", ""));
        Mockito.when(userService.getUsers()).thenReturn(userDtoList);

        mockMvc.perform(get("/users"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is((int)ID)))
                .andExpect(jsonPath("$[0].firstName", Matchers.is(FIRST_NAME)))
                .andExpect(jsonPath("$[0].lastName", Matchers.is(LAST_NAME)))
                .andExpect(jsonPath("$[0].email", Matchers.is(EMAIL)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$[1].firstName", Matchers.is("testName2")))
                .andExpect(jsonPath("$[1].lastName", Matchers.is("testSurname2")))
                .andExpect(jsonPath("$[1].email", Matchers.is("")));
    }

    @Test
    @DisplayName("Test that it returns the user that matches the id")
    void getUser() throws Exception {
        Mockito.when(userService.getUser(1L)).thenReturn(Optional.of(userDtoList.get(0)));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is((int)ID)))
                .andExpect(jsonPath("$.firstName", Matchers.is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", Matchers.is(LAST_NAME)))
                .andExpect(jsonPath("$.email", Matchers.is(EMAIL)));
    }

    @Test
    @DisplayName("Test that it returns the user that has been added")
    void addUser() throws Exception {
        String _firstName = "Johanne";
        String _lastName = "Smith";
        String _email = "jsmith@gmail.com";
        UserDto userDto = new UserDto(2L, _firstName,_lastName, _email);
        userDtoList.add(userDto);

        Mockito.when(userService.addUser(userDto))
                .thenReturn(userDtoList.get(1));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 2);
        jsonObject.put("firstName", _firstName);
        jsonObject.put("lastName", _lastName);
        jsonObject.put("email", _email);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new JSONObject(jsonObject).toJSONString()))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(2)))
                .andExpect(jsonPath("$.firstName", Matchers.is(_firstName)))
                .andExpect(jsonPath("$.lastName", Matchers.is(_lastName)))
                .andExpect(jsonPath("$.email", Matchers.is(_email)));
    }

    @Test
    @DisplayName("Test that it finds the user and updates it")
    void updateUser() throws Exception {
        String _firstName = "Jay";
        String _lastName = "Berry";
        String _email = "jb@gmail.com";
        UserDto userDto = new UserDto(1L, _firstName,_lastName, _email);
        userDtoList.set(0, userDto);

        Mockito.when(userService.updateUser(eq(1L), any()))
                .thenReturn(Optional.of(userDtoList.get(0)));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", null);
        jsonObject.put("firstName", _firstName);
        jsonObject.put("lastName", _lastName);
        jsonObject.put("email", _email);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new JSONObject(jsonObject).toJSONString()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.firstName", Matchers.is(_firstName)))
                .andExpect(jsonPath("$.lastName", Matchers.is(_lastName)))
                .andExpect(jsonPath("$.email", Matchers.is(_email)));
    }

    @Test
    @DisplayName("Test that the PUT rest API adds the user if it does not exist")
    void updateUserCreatesUserWhenNotFound() throws Exception {
        String _firstName = "Jay";
        String _lastName = "Berry";
        String _email = "jb@gmail.com";
        UserDto userDto = new UserDto(2L, _firstName,_lastName, _email);
        userDtoList.add(userDto);

        Mockito.when(userService.updateUser(eq(1L), any()))
                .thenReturn(Optional.empty());
        Mockito.when(userService.addUser(any()))
                .thenReturn(userDtoList.get(1));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", null);
        jsonObject.put("firstName", _firstName);
        jsonObject.put("lastName", _lastName);
        jsonObject.put("email", _email);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new JSONObject(jsonObject).toJSONString()))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(2)))
                .andExpect(jsonPath("$.firstName", Matchers.is(_firstName)))
                .andExpect(jsonPath("$.lastName", Matchers.is(_lastName)))
                .andExpect(jsonPath("$.email", Matchers.is(_email)));
    }

    @Test
    @DisplayName("Test that the delete rest API deletes the user")
    void removeUser() throws Exception {
        doNothing().when(userService).removeUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().is(HttpStatus.OK.value()));
    }
}