package org.cloud.tutorials.utility;

import org.cloud.tutorials.dto.UserDto;
import org.cloud.tutorials.model.User;

public interface EntityConverter {
    UserDto getUserDto(User user);

    User getNewUser(UserDto userDto);
}
