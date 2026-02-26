package com.ecom.service;

import com.ecom.api.model.LoginBody;
import com.ecom.api.model.RegistrationBody;
import com.ecom.exception.EmailFailureException;
import com.ecom.exception.UserAlreadyExistsException;
import com.ecom.exception.UserNotVerifiedException;
import com.ecom.model.LocalUser;
import com.ecom.model.VerificationToken;
import com.ecom.repository.UserRepository;
import com.ecom.repository.VerificationTokenRepository;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @RegisterExtension
    private static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot","secrete"))
            .withPerMethodLifecycle(true);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Test
    @Transactional
    public void testUserService() throws MessagingException {
        RegistrationBody body = new RegistrationBody();
        body.setUsername("UserA");
        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        body.setPassword("MySecretePassword123");
        body.setFirstName("FirstName");
        body.setLastName("Last Name");

        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(body),
                "Username should already in use.");

        body.setUsername("UserServiceTest$testRegisterUser");
        body.setEmail("UserA@junit.com");

        Assertions.assertThrows(UserAlreadyExistsException.class,
                ()-> userService.registerUser(body),
                "Email Should already be in use.");

        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        Assertions.assertDoesNotThrow(()-> userService.registerUser(body),
                "User should register successfully");

        Assertions.assertEquals(body.getEmail(), greenMailExtension.getReceivedMessages()[0]
                .getRecipients(Message.RecipientType.TO)[0].toString());

    }


    @Test
    @Transactional
    public void TestLoginUser() throws UserNotVerifiedException, EmailFailureException {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("UserA-NotExists");
        loginBody.setPassword("PasswordA123-BadPassword");
        Assertions.assertNull(userService.loginUser(loginBody), "The user should not exist");

        loginBody.setUsername("UserA");
        Assertions.assertNull(userService.loginUser(loginBody), "The password should be incorrect");

        loginBody.setPassword("PasswordB123");
        Assertions.assertNull(userService.loginUser(loginBody),"User should login successfully");

        loginBody.setUsername("UserB");
        loginBody.setPassword("PasswordB123");
        try{
            userService.loginUser(loginBody);
            Assertions.assertTrue(false, "User should not have email verified");
        }catch (UserNotVerifiedException ex){
            Assertions.assertTrue(ex.isNewEmailSent(), "Email verification should be sent.");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }

        try{
            userService.loginUser(loginBody);
            Assertions.assertFalse(false, "User should not have email verified");
        }catch (UserNotVerifiedException ex){
            Assertions.assertFalse(ex.isNewEmailSent(), "Email verification should be resent.");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
    }

    @Test
    @Transactional
    public void testVerifyUser() throws EmailFailureException {
        Assertions.assertFalse(userService.verifyUser("BAD-TOKEN"), "Token is bad or does not exist should return false.");
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("UserB");
        loginBody.setPassword("PasswordB123");

        LocalUser localUser = userRepository.findByUsernameIgnoreCase(loginBody.getUsername()).get();
        System.out.println("Id :: "+localUser.getId());
        System.out.println("Email :: "+localUser.getEmailVerified());

        try{
            userService.loginUser(loginBody);
            Assertions.assertTrue(false,"User should not have email verified");
        } catch (UserNotVerifiedException e) {
            List<VerificationToken> tokens = verificationTokenRepository.findByUser_IdOrderByIdDesc(localUser.getId());
            String token = tokens.getFirst().getToken();
            Assertions.assertTrue(userService.verifyUser(token), "Token should be valid");
            Assertions.assertNotNull(loginBody,"The user should now be verified");
        }
    }
}
