package com.ecom.service;

import com.ecom.api.model.RegistrationBody;
import com.ecom.exception.EmailFailureException;
import com.ecom.exception.UserAlreadyExistsException;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @RegisterExtension
    private static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot","secrete"))
            .withPerMethodLifecycle(true);

    @Autowired
    private UserService userService;

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
        body.setEmail("userA@junit.com");

        Assertions.assertThrows(UserAlreadyExistsException.class,
                ()-> userService.registerUser(body),
                "Email Should already be in use.");

        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        Assertions.assertDoesNotThrow(()-> userService.registerUser(body),
                "User should register successfully");

        Assertions.assertEquals(body.getEmail(), greenMailExtension.getReceivedMessages()[0]
                .getRecipients(Message.RecipientType.TO)[0].toString());

    }
}
