package org.cloud.tutorials.service;

import org.cloud.tutorials.dto.UserDto;
import org.cloud.tutorials.model.User;
import org.cloud.tutorials.repository.UserRepository;
import org.cloud.tutorials.utility.EntityConverter;
import org.cloud.tutorials.utility.impl.EntityConverterImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.cloud.tutorials.CommonConstants.EMAIL;
import static org.cloud.tutorials.CommonConstants.FIRST_NAME;
import static org.cloud.tutorials.CommonConstants.LAST_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;
    private EntityConverter entityConverter;
    private List<User> userList;

    @BeforeEach
    public void setup() {
        userList = new ArrayList<>();
        entityConverter = new EntityConverterImpl();
        userService = new UserService(userRepository, entityConverter);
        User user = new User();
        user.setId(1L);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL);
        userList.add(user);

        // Second user
        User _user = new User();
        _user.setId(2L);
        _user.setFirstName(FIRST_NAME);
        _user.setLastName(LAST_NAME);
        _user.setEmail(EMAIL);
        userList.add(_user);
    }

    @Test
    @DisplayName("Test that the service returns a list of all users in DTO format")
    void getUsers() {
        List<UserDto> userDtoList = userList.stream().map(user -> entityConverter.getUserDto(user)).collect(Collectors.toList());
        when(userRepository.findAll()).thenReturn(userList);

        List<UserDto> responseList = userService.getUsers();

        Assertions.assertArrayEquals(responseList.toArray(), userDtoList.toArray());
    }

    @Test
    @DisplayName("Test that the service finds the correct user")
    void getUser() {
        UserDto user1Dto = entityConverter.getUserDto(userList.get(0));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userList.get(0)));

        Optional<UserDto> responseDTO = userService.getUser(1L);
        Assertions.assertEquals(responseDTO, Optional.of(user1Dto));
    }

    @Test
    @DisplayName("Test that the service adds the user")
    void addUser() {
        UserDto newUserDto = new UserDto(null, "rebecca", "April", "aprilfool@gmail.com");
        User newUser = new User();
        newUser.setId(3L);
        newUser.setFirstName(newUserDto.getFirstName());
        newUser.setLastName(newUserDto.getLastName());
        newUser.setEmail(newUserDto.getEmail());

        when(userRepository.save(any())).thenReturn(newUser);
        UserDto responseUserDto = userService.addUser(newUserDto);
        Assertions.assertEquals(
                responseUserDto, new UserDto(3L, "rebecca", "April", "aprilfool@gmail.com"));
    }

    @Test
    @DisplayName("Test that the service updates the user when it is found")
    void updateUser() {
        UserDto newUpdateUserDto = new UserDto(2L, "rebecca", "April", "aprilfool@gmail.com");
        User newUser = new User();
        newUser.setId(2L);
        newUser.setFirstName(newUpdateUserDto.getFirstName());
        newUser.setLastName(newUpdateUserDto.getLastName());
        newUser.setEmail(newUpdateUserDto.getEmail());

        when(userRepository.findById(2L)).thenReturn(Optional.of(userList.get(1)));
        when(userRepository.save(any())).thenReturn(newUser);
        Optional<UserDto> responseUserDto = userService.updateUser(2L, newUpdateUserDto);
        Assertions.assertEquals(
                responseUserDto.get(), new UserDto(2L, "rebecca", "April", "aprilfool@gmail.com"));

    }

    @Test
    @DisplayName("Test that the service returns empty optional when the user was not found")
    void updateUserReturnsEmptyOptional() {
        UserDto newUserDto = new UserDto(null, "sephora", "vivi", "sn@gmail.com");
        when(userRepository.findById(6L)).thenReturn(Optional.empty());

        Optional<UserDto> responseDTO = userService.updateUser(6L, newUserDto);
        Assertions.assertEquals(responseDTO, Optional.empty());
    }

    @Test
    @DisplayName("Test that no error is thrown removing user")
    void removeUser() {
        doNothing().when(userRepository).deleteById(any());
        userService.removeUser(1L);
    }
}