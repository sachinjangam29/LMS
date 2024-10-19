package com.jangam.schoolmgt.userservice.service;

import com.jangam.schoolmgt.userservice.model.Role;
import com.jangam.schoolmgt.userservice.model.User;
import com.jangam.schoolmgt.userservice.payload.request.SignupRequest;
import com.jangam.schoolmgt.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;


    public void deleteAllUsersFromDB() {
        userRepository.deleteAll();
    }

    public String registerUser(SignupRequest signupRequest) {

        if (!userRepository.existsByUsername(signupRequest.getUsername()) &&
                !userRepository.existsByEmail(signupRequest.getEmail())) {
            String roleName = signupRequest.getRole().toString().toUpperCase();
            logger.info("Role name " + roleName);
            if (roleName.equals(Role.USER.name()) || roleName.equals(Role.LIBRARIAN.name()) || roleName.equals(Role.ADMIN.name())) {
                User user = mapToUser(signupRequest);
                userRepository.save(user);
                return "successfully";
            } else {
                return "role not valid";
            }
        } else {
            return "already taken";
        }
    }

    public User mapToUser(SignupRequest signupRequest) {
        return User.builder()
                .email(signupRequest.getEmail())
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .phoneNumber(signupRequest.getPhoneNumber())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .role(signupRequest.getRole())
                .username(signupRequest.getUsername())
                .isActive(Boolean.FALSE)
                .build();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
