package com.tsimerekis.user;

import com.tsimerekis.user.exception.UserAlreadyExistsException;
import com.tsimerekis.user.exception.UserNotFoundException;
import com.tsimerekis.user.repository.UserEntityRepository;
import com.tsimerekis.user.ui.NewUserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserEntityRepository userRepository;

    @Test
    @Transactional
    void registerUserUserAlreadyExistsException() {
        NewUserInfo newUser = new NewUserInfo();
        newUser.setFirstName("First");
        newUser.setLastName("Last");
        newUser.setEmail("example@example.com");
        newUser.setPassword("testPassword");
        userService.registerUser(newUser);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(newUser));
    }

    @Test
    void testRegisterUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    @Transactional
    void getUserEntityFromUserDetails() {
        NewUserInfo newUser = new NewUserInfo();
        newUser.setFirstName("First");
        newUser.setLastName("Last");
        newUser.setEmail("example@example.com");
        newUser.setPassword("testPassword");
        UserDetails userDetails = userService.registerUser(newUser);

        final UserEntity user = userRepository.findByUsername(userDetails.getUsername());
        UserDetails fakeUser = User.builder()
                .username("fake@example.com")
                .password("")
                .roles("USER")
                .build();

        assertNotNull(userService.getUserEntityFromUserDetails(userDetails));
        assertEquals(user, userService.getUserEntityFromUserDetails(userDetails).get());
        assertThrows(UserNotFoundException.class, () -> userService.getUserEntityFromUserDetails(fakeUser));
    }
}