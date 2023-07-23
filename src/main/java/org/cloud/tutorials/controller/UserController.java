package org.cloud.tutorials.controller;

import lombok.extern.slf4j.Slf4j;
import org.cloud.tutorials.dto.UserDto;
import org.cloud.tutorials.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
@Slf4j
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getUsers() {
        try {
            List<UserDto> users = userService.getUsers();
            if (users.isEmpty()) return new ResponseEntity<>(HttpStatus.OK);
            else return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Server error thrown when trying to get a list of users", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        try {
            Optional<UserDto> user = userService.getUser(id);
            return user.map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            log.error("Server error thrown when trying to get a user with ID", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<UserDto> addUser(@Validated @RequestBody UserDto userDto) {
        try {
            UserDto user = userService.addUser(userDto);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Server error thrown when trying to add a user", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id,
                              @Validated @RequestBody UserDto userDto) {
        try {
            Optional<UserDto> userDtoOptional = userService.updateUser(id, userDto);
            if (userDtoOptional.isEmpty()) {
                UserDto _userDto = userService.addUser(userDto);
                return new ResponseEntity<>(_userDto, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(userDtoOptional.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(String.format("Server error thrown trying to update the user with ID: {} ", id), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable("id") Long id) {
        try {
            userService.removeUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Server error thrown when trying to delete a user", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
