package com.jangam.schoolmgt.userservice.service;

import com.jangam.schoolmgt.userservice.model.Role;
import com.jangam.schoolmgt.userservice.model.User;
import com.jangam.schoolmgt.userservice.payload.request.LoginRequest;
import com.jangam.schoolmgt.userservice.payload.request.SignupRequest;
import com.jangam.schoolmgt.userservice.repository.UserRepository;
import com.jangam.schoolmgt.userservice.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.grammars.hql.HqlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public void deleteAllUsersFromDB() {
        userRepository.deleteAll();
    }

    public String registerUser(SignupRequest signupRequest) {

        if (!userRepository.existsByUsername(signupRequest.getUsername()) &&
                !userRepository.existsByEmail(signupRequest.getEmail())) {
            String roleName = signupRequest.getRole().toString().toUpperCase();
            logger.info("Role name " + roleName);
            if (roleName.equals(Role.USER.name()) || roleName.equals(Role.LIBRARIAN.name()) || roleName.equals(Role.ADMIN.name())) {
                try {
                    User user = mapToUser(signupRequest);
                    userRepository.save(user);
                    Map<String, String> tokens = entryPointForTokenGeneration(authenticationManager, signupRequest);
                    logger.info("generated token " + tokens);
                } catch (Exception e) {
                    logger.error("error saving user details!", e);
                }
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
//                .createdAt(LocalDateTime.now())
                .isActive(Boolean.FALSE)
                .build();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Map<String, String> entryPointForTokenGeneration(AuthenticationManager authenticationManager, SignupRequest signupRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signupRequest.getUsername(),
                        signupRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("Username ", signupRequest.getUsername());
        logger.info("Password ", signupRequest.getPassword());

        String accessToken = jwtUtils.generateToken(authentication);
        logger.info("access Token 2 ", accessToken);

        String refreshAccessToken = jwtUtils.generateRefreshToken(signupRequest.getUsername());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshAccessToken);

        return tokens;
    }
}
