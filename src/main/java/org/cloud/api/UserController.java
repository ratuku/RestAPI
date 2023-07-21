package org.cloud.api;

import org.cloud.dto.UserDto;
import org.cloud.entity.User;
import org.cloud.utility.EntityConverter;
import org.cloud.repository.UserRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
public class UserController {

    private final UserRepository repository;
    private final EntityConverter converter = new EntityConverter();

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public List<UserDto> getUsers() {
        return repository.findAll()
                .stream()
                .map(converter::getUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        return converter.getUserDto(repository.findById(id).get());
    }

    @PostMapping("")
    public UserDto addUser(@Validated @RequestBody User user) {
        return converter.getUserDto(repository.save(user));
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") Long id,
                              @Validated @RequestBody User _user) {
        User user = repository.findById(id).get();

        user.setFirstName(_user.getFirstName());
        user.setLastName(_user.getLastName());
        user.setEmail(_user.getEmail());
        return converter.getUserDto(repository.save(user));
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable("id") Long id) {
        repository.deleteById(id);
    }
}
