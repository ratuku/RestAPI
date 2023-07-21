package org.cloud.api;

import org.cloud.dto.UserDto;
import org.cloud.entity.User;
import org.cloud.utility.EntityConverter;
import org.cloud.repository.UserRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
public class UserController {

    private UserRepository repository;
    private EntityConverter converter = new EntityConverter();

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("")
    public List<UserDto> getUsers() {
        List<UserDto> users  = repository.findAll()
                .stream()
                .map(u -> converter.getUserDto(u))
                .collect(Collectors.toList());
        return users;
    }

    @RequestMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        User user = repository.findById(id).get();
        return converter.getUserDto(user);
    }
}
