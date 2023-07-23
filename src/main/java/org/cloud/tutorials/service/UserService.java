package org.cloud.tutorials.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cloud.tutorials.dto.UserDto;
import org.cloud.tutorials.model.User;
import org.cloud.tutorials.repository.UserRepository;
import org.cloud.tutorials.utility.EntityConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;
    EntityConverter entityConverter;

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream().map(user -> entityConverter.getUserDto(user))
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUser(Long id) {
        return userRepository.findById(id)
                .map(_user -> entityConverter.getUserDto(_user))
                .or(Optional::empty);
    }

    public UserDto addUser(UserDto _userDto) {
        User user = userRepository.save(entityConverter.getNewUser(_userDto));
        return entityConverter.getUserDto(user);
    }

    /**
     *
     * @param id The user's id
     * @param _userDto @{@code UserDto}
     * @return Returns empty optional when the user was not found or Optional with UserDto when update succeeds
     *
     */
    public Optional<UserDto> updateUser(Long id, UserDto _userDto) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            log.error(String.format("Failed to find user with ID: {} when trying to update the user", id));
            return Optional.empty();
        }
        else {
            User user = userOptional.get();
            user.setFirstName(_userDto.getFirstName());
            user.setLastName(_userDto.getLastName());
            user.setEmail(_userDto.getEmail());
            return Optional.of(entityConverter.getUserDto(userRepository.save(user)));
        }
    }

    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }
}
