package org.cloud.utility;

import org.cloud.dto.UserDto;
import org.cloud.entity.User;

public class EntityConverter {

    public UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(userDto.getEmail());
        return userDto;
    }

}
