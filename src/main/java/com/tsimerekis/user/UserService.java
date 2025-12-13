package com.tsimerekis.user;

import com.tsimerekis.user.exception.InvalidNewUserException;
import com.tsimerekis.user.exception.UserAlreadyExistsException;
import com.tsimerekis.user.repository.UserEntityRepository;
import com.tsimerekis.user.ui.NewUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserEntityRepository userRepository;

    private final UserDetailsManager userDetailsManager;

    private final PasswordEncoder passwordEncoder;

    private UserService(@Autowired UserEntityRepository userEntityRepository,
                        @Autowired UserDetailsManager userDetailsManager,
                        @Autowired PasswordEncoder passwordEncoder) {
        this.userRepository = userEntityRepository;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails registerUser(NewUserInfo newUserInfo) {
        final UserEntity userEntity = new UserEntity();

        userEntity.setFullName(newUserInfo.getFirstName() + " " + newUserInfo.getLastName());
        userEntity.setUsername(newUserInfo.getEmail());

        final UserDetails userDetails = User.builder()
                                            .username(newUserInfo.getEmail())
                                            .password(passwordEncoder.encode(newUserInfo.getPassword()))
                                            .roles("USER")
                                            .build();

        userEntity.setUserDetails(userDetails);

        registerUser(userEntity);

        return userDetails;
    }

    private void registerUser(UserEntity user) throws InvalidNewUserException {
        throwIfInvalidNewUserEntity(user);

        userRepository.save(user);

        userDetailsManager.createUser(user.getUserDetails());
    }

    public void updateUser(UserEntity user) {
        userRepository.save(user);

        //password change handling here
    }

    public Optional<UserEntity> getUserEntityFromUserDetails(UserDetails userDetails) {

            final UserEntity user = userRepository.findByUsername(userDetails.getUsername());

            if (user != null) {
                user.setUserDetails(userDetails);
            }

            return Optional.ofNullable(user);
    }

    private void throwIfInvalidNewUserEntity(UserEntity user) throws InvalidNewUserException {
        if ( !(user != null &&
            user.getUsername() != null &&
            user.getUserDetails() != null &&
            user.getUserDetails().getUsername() != null &&
            !user.getUserDetails().getUsername().isEmpty() &&
            user.getUserDetails().getPassword() != null &&
            !user.getUserDetails().getPassword().isEmpty()))
            throw new InvalidNewUserException();
        else
            throwIfAlreadyExistingUser(user);
    }

    private void throwIfAlreadyExistingUser(UserEntity user) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(user.getUsername()) || userDetailsManager.userExists(user.getUsername()))
            throw new UserAlreadyExistsException(user.getUsername());
    }

}
