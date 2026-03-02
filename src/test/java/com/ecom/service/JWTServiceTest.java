package com.ecom.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.ecom.entity.User;
import com.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JWTServiceTest {

    @Autowired
    private JWTService service;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Test
    @Transactional
    public void testVerificationTokenNotUsableForLogin(){
        User user = userRepository.findByUsernameIgnoreCase("UserA").get();
        String token = service.generateVerificationJWT(user);

        Assertions.assertNull(service.getUsername(token), "Verification token should not contain username");
    }

    @Test
    public void testAuthTokenReturnsUsername(){
        User user = userRepository.findByUsernameIgnoreCase("UserA").get();
        String token = service.generateJWT(user);

        Assertions.assertEquals(user.getUsername(), service.getUsername(token), "Token for auth should contain user's username");
    }

    @Test
    public void testLoginJWTNOtGeneratedByUs(){
        String token = JWT.create().withClaim("USERNAME","UserA")
                .sign(Algorithm.HMAC256("NotTheRealSecrete"));

        Assertions.assertThrows(SignatureVerificationException.class, ()-> service.getUsername(token));
    }

    @Test
    public void testLoginJWTCorrectlySignedNoIssuer(){
        String token = JWT.create().withClaim("USERNAME","UserA")
                .sign(Algorithm.HMAC256(algorithmKey));

        Assertions.assertThrows(MissingClaimException.class, ()-> service.getUsername(token));
    }

    @Test
    public void testPasswordResetToken(){
        User user = userRepository.findByUsernameIgnoreCase("UserA").get();
        String token = service.generatePasswordResetJWT(user);

        Assertions.assertEquals(user.getEmail(),service.getResetPasswordEmail(token),
                "Email should match inside JWT. ");
    }

    @Test
    public void testResetPasswordJWTNOtGeneratedByUs(){
        String token = JWT.create().withClaim("RESET_PASSWORD_EMAIL","UserA@junit.com")
                .sign(Algorithm.HMAC256("NotTheRealSecrete"));

        Assertions.assertThrows(SignatureVerificationException.class,
                ()-> service.getResetPasswordEmail(token));
    }

    @Test
    public void testResetPasswordJWTCorrectlySignedNoIssuer(){
        String token = JWT.create().withClaim("RESET_PASSWORD_EMAIL","UserA@junit.com")
                .sign(Algorithm.HMAC256(algorithmKey));

        Assertions.assertThrows(MissingClaimException.class,
                ()-> service.getResetPasswordEmail(token));
    }
}
