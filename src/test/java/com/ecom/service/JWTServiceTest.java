package com.ecom.service;

import com.ecom.model.LocalUser;
import com.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class JWTServiceTest {

    @Autowired
    private JWTService service;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testVerificationTokenNotUsableForLogin(){
        LocalUser user = userRepository.findByUsernameIgnoreCase("UserA").get();
        String token = service.generateVerificationJWT(user);

        Assertions.assertNull(service.getUsername(token), "Verification token should not contain username");
    }

    @Test
    public void testAuthTokenReturnsUsername(){
        LocalUser user = userRepository.findByUsernameIgnoreCase("UserA").get();
        String token = service.generateJWT(user);

        Assertions.assertEquals(user.getUsername(), service.getUsername(token), "Token for auth should contain user's username");

    }
}
