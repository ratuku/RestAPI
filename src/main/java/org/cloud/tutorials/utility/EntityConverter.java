package org.cloud.tutorials.utility;

import org.cloud.tutorials.dto.UserDto;
import org.cloud.tutorials.entity.User;

public interface EntityConverter {
    UserDto getUserDto(User user);

    User getUser(UserDto userDto);
}
