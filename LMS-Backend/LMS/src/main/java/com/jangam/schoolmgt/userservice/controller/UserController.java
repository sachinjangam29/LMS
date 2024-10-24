package com.jangam.schoolmgt.userservice.controller;

import com.jangam.schoolmgt.userservice.model.User;
import com.jangam.schoolmgt.userservice.payload.request.SignupRequest;
import com.jangam.schoolmgt.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        String message = userService.registerUser(signupRequest);

        if(message.equals("successfully")){
            return new ResponseEntity<>("User registration in successfully.", HttpStatus.CREATED);
        } else if (message.equals("already taken")){
            return new ResponseEntity<>("Username or Email is already taken.",HttpStatus.CONFLICT);
        } else if(message.equals("role not valid")){
            return new ResponseEntity<>("Users roles are invalid.",HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Internal Server Error.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-all")
    public String deleteAllUsers() {
        userService.deleteAllUsersFromDB();
        return "All the users records are deleted.";
    }
}
