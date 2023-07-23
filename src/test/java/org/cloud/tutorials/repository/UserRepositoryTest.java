package org.cloud.tutorials.repository;

import org.cloud.tutorials.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.cloud.tutorials.CommonConstants.EMAIL;
import static org.cloud.tutorials.CommonConstants.FIRST_NAME;
import static org.cloud.tutorials.CommonConstants.LAST_NAME;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private List<User> users;
    @BeforeAll
    public void populateDB() {
        User user = new User(null, FIRST_NAME, LAST_NAME, EMAIL);
        userRepository.save(user);
        User _user = new User(null, "John", "France", "");
        userRepository.save(_user);
        users = List.of(user, _user);
    }

    @Test
    public void findAll() {
        List<User> userList = userRepository.findAll();
        Assertions.assertEquals(userList, users);
    }

    @Test
    public void findById() {
        Optional<User> userOptional = userRepository.findById(1L);
        Assertions.assertEquals(userOptional.get(), users.get(0));
    }

    @Test
    public void save() {
        User newUser = new User(null, "changeName", LAST_NAME, EMAIL);

        User user = userRepository.save(newUser);
        Assertions.assertEquals(user, new User(3L, "changeName", LAST_NAME, EMAIL));
    }

}
