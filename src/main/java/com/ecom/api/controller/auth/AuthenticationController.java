package com.ecom.api.controller.auth;

import com.ecom.api.model.LoginBody;
import com.ecom.api.model.LoginResponse;
import com.ecom.api.model.RegistrationBody;
import com.ecom.exception.EmailFailureException;
import com.ecom.exception.UserAlreadyExistsException;
import com.ecom.exception.UserNotVerifiedException;
import com.ecom.model.LocalUser;
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setJwt(jwt);
            loginResponse.setSuccess(true);
            return ResponseEntity.ok(loginResponse);
        }

    }

    @GetMapping("/me")
    public LocalUser getLoggedInUser(@AuthenticationPrincipal LocalUser user){
        return user;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String token){
        if (service.verifyUser(token)){
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
