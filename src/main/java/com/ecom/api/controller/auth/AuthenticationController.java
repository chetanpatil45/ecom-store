package com.ecom.api.controller.auth;

import com.ecom.api.dto.LoginBody;
import com.ecom.api.dto.LoginResponse;
import com.ecom.api.dto.PasswordResetBody;
import com.ecom.api.dto.RegistrationBody;
import com.ecom.exception.*;
import com.ecom.entity.User;
import com.ecom.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService service;

    public AuthenticationController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody){
//        return "ok---> Tested";
        try {
            service.registerUser(registrationBody);
            return ResponseEntity.ok("Added");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EmailFailureException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginBody loginBody){
        String jwt = null;
        try {
            jwt = service.loginUser(loginBody);
        } catch (UserNotVerifiedException ex) {
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            String reason = "USER_NOT_VERIFIED";
            if (ex.isNewEmailSent()) {
                reason += "_EMAIL_RESENT";
            }
            response.setFailureReason(reason);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        } catch (EmailFailureException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (jwt == null){
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setFailureReason("User not found");
            loginResponse.setSuccess(false);
            return ResponseEntity.badRequest().body(loginResponse);
        }else if (jwt.isBlank()){
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setFailureReason("Invalid credentials");
            loginResponse.setSuccess(false);
            return ResponseEntity.badRequest().body(loginResponse);
        }
        else {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setJwt(jwt);
            loginResponse.setSuccess(true);
            return ResponseEntity.ok(loginResponse);
        }

    }

    @GetMapping("/me")
    public User getLoggedInUser(@AuthenticationPrincipal User user){
        return user;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String token){
        if (service.verifyUser(token)){
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/forgot")
    public ResponseEntity forgotPassword(@RequestParam String email){
        try {
            service.forgotPassword(email);
            return ResponseEntity.ok().build();
        } catch (EmailNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email Not Found");
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetBody body){
        try {
            service.resetPassword(body);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
