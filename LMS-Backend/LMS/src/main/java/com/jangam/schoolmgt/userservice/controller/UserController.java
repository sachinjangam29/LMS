package com.jangam.schoolmgt.userservice.controller;

import com.jangam.schoolmgt.userservice.model.User;
import com.jangam.schoolmgt.userservice.payload.request.LoginRequest;
import com.jangam.schoolmgt.userservice.payload.request.SignupRequest;
import com.jangam.schoolmgt.userservice.service.UserService;
import com.jangam.schoolmgt.userservice.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(signupRequest.getUsername(),
//                        signupRequest.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token = jwtUtils.generateToken(authentication);
        String message = userService.registerUser(signupRequest);

        if (message.equals("successfully")) {
//            String generatedToken = userService.entryPointForTokenGeneration(authenticationManager,signupRequest);
            return new ResponseEntity<>("User registration in successfully. TOKEN: ", HttpStatus.CREATED);
        } else if (message.equals("already taken")) {
            return new ResponseEntity<>("Username or Email is already taken.", HttpStatus.CONFLICT);
        } else if (message.equals("role not valid")) {
            return new ResponseEntity<>("Users roles are invalid.", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> checkUserCredentials(@Valid @RequestBody SignupRequest signupRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(signupRequest.getUsername(),
//                        signupRequest.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token = jwtUtils.generateToken(authentication);
        Map<String, String> tokens = userService.entryPointForTokenGeneration(authenticationManager,signupRequest);

      //  String generatedToken = userService.entryPointForTokenGeneration(authenticationManager,loginRequest);

      //  return ResponseEntity.ok(generatedToken);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/all-users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete-all")
    public String deleteAllUsers() {
        userService.deleteAllUsersFromDB();
        return "All the users records are deleted.";
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, Authentication authentication) {
        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String,String>> refreshAccessToken(@RequestBody Map<String,String> request){
        String refreshToken = request.get("refreshToken");

        if(refreshToken!=null && jwtUtils.validateRefreshToken(refreshToken)){
            String username = jwtUtils.getUserNameFromToken(refreshToken);
            String newAccessToken = jwtUtils.generateRefreshToken(username);

            Map<String,String> tokens = new HashMap<>();
            tokens.put("accessToken",newAccessToken);
            tokens.put("refreshToken",refreshToken);

            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
