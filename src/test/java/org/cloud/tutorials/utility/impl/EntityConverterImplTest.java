package org.cloud.tutorials.utility.impl;

import org.cloud.tutorials.dto.UserDto;
import org.cloud.tutorials.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.cloud.tutorials.CommonConstants.EMAIL;
import static org.cloud.tutorials.CommonConstants.FIRST_NAME;
import static org.cloud.tutorials.CommonConstants.LAST_NAME;

class EntityConverterImplTest {

    private final EntityConverterImpl entityConverter = new EntityConverterImpl();

    @Test
    void getUserDto() {
        User user = new User();
        user.setId(1L);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL);

        UserDto userDto = entityConverter.getUserDto(user);
        Assertions.assertEquals(1, userDto.getId());
        Assertions.assertEquals(FIRST_NAME, userDto.getFirstName());
        Assertions.assertEquals(LAST_NAME, userDto.getLastName());
        Assertions.assertEquals(EMAIL, userDto.getEmail());
    }

    @Test
    void getNewUser() {
        UserDto userDto = new UserDto(4L, FIRST_NAME, LAST_NAME, EMAIL);

        User user = entityConverter.getNewUser(userDto);
        Assertions.assertNull(user.getId());
        Assertions.assertEquals(FIRST_NAME, user.getFirstName());
        Assertions.assertEquals(LAST_NAME, user.getLastName());
        Assertions.assertEquals(EMAIL, user.getEmail());
    }
}