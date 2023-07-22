package org.cloud.tutorials.utility.impl;

import org.cloud.tutorials.dto.UserDto;
import org.cloud.tutorials.entity.User;
import org.cloud.tutorials.utility.EntityConverter;
import org.springframework.stereotype.Component;

/**
 * This class is used to convert Entities to DTOs, vice versa
 */
@Component
public class EntityConverterImpl implements EntityConverter {

    public UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(userDto.getEmail());
        return userDto;
    }

    public User getUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        return user;
    }

}
