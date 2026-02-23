package com.ecom.service;

import com.ecom.api.model.LoginBody;
import com.ecom.api.model.RegistrationBody;
import com.ecom.exception.EmailFailureException;
import com.ecom.exception.UserAlreadyExistsException;
import com.ecom.model.LocalUser;
import com.ecom.model.VerificationToken;
import com.ecom.repository.UserRepository;
import com.ecom.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    public UserService(UserRepository repository, EncryptionService encryptionService, JWTService jwtService, EmailService emailService, VerificationTokenRepository verificationTokenRepository) {
        this.repository = repository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException, EmailFailureException {

        if (repository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()
                || repository.findByEmail(registrationBody.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User already Exists");
        }

        LocalUser localUser = new LocalUser();
        localUser.setUsername(registrationBody.getUsername());
        localUser.setEmail(registrationBody.getEmail());

        // DONE- ENCRYPT PASSWORD USING BCRYPT-ALGORITHM
        localUser.setPassword(encryptionService.encryptPassword((registrationBody.getPassword())));
        localUser.setFirstName(registrationBody.getFirstName());
        localUser.setLastName(registrationBody.getLastName());

        VerificationToken verificationToken = createVerificationToken(localUser);
        emailService.sendEmail(verificationToken);
        verificationTokenRepository.save(verificationToken);

        return repository.save(localUser);
    }

    public String loginUser(LoginBody loginBody){
        Optional<LocalUser> opUser = repository.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()){
            LocalUser user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(),user.getPassword())){
                return jwtService.generateJWT(user);
            }
            return null;
        }
        else return null;
    }

    private VerificationToken createVerificationToken(LocalUser user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setUser(user);
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));

        user.getVerificationTokens().add(verificationToken);

        return verificationToken;
    }
}
