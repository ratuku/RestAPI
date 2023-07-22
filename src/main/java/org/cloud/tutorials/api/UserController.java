package org.cloud.tutorials.api;

import lombok.extern.slf4j.Slf4j;
import org.cloud.tutorials.dto.UserDto;
import org.cloud.tutorials.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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
            if (users.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
        } catch (Exception e) {
            log.error("Server error thrown when trying to get a user with ID: "+1, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<UserDto> addUser(@Validated @RequestBody UserDto _user) {
        try {
            UserDto user = userService.addUser(_user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Server error thrown when trying to add a user", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id,
                              @Validated @RequestBody UserDto _userDto) {
        try {
            Optional<UserDto> userDtoOptional = userService.updateUser(id, _userDto);

            return userDtoOptional.map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            log.error("Server error thrown when trying to update a user", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Objects> removeUser(@PathVariable("id") Long id) {
        try {
            userService.removeUser(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Server error thrown when trying to delete a user", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
