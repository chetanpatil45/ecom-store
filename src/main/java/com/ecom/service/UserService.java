package com.ecom.service;

import com.ecom.api.dto.LoginBody;
import com.ecom.api.dto.PasswordResetBody;
import com.ecom.api.dto.RegistrationBody;
import com.ecom.exception.*;
import com.ecom.entity.User;
import com.ecom.entity.VerificationToken;
import com.ecom.repository.UserRepository;
import com.ecom.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
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

    public User registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException, EmailFailureException {

        if (repository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()
                || repository.findByEmail(registrationBody.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User already Exists");
        }

        User localUser = new User();
        localUser.setUsername(registrationBody.getUsername());
        localUser.setEmail(registrationBody.getEmail());

        // DONE- ENCRYPT PASSWORD USING BCRYPT-ALGORITHM
        localUser.setPassword(encryptionService.encryptPassword((registrationBody.getPassword())));
        localUser.setFirstName(registrationBody.getFirstName());
        localUser.setLastName(registrationBody.getLastName());


        User savedUser = repository.save(localUser);

        VerificationToken token = createVerificationToken(savedUser);
        verificationTokenRepository.save(token);
        emailService.sendEmail(token);

        return savedUser;
    }

    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<User> opUser = repository.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()){
            User user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(),user.getPassword())){
                if (user.getEmailVerified()){
                    return jwtService.generateJWT(user);
                }else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.isEmpty() ||
                            verificationTokens.getFirst().getCreatedTimestamp().before(new Timestamp(System.currentTimeMillis() - (60 * 60 - 1000)));

                    if (resend){
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenRepository.save(verificationToken);
                        emailService.sendEmail(verificationToken);
                    }

                    throw new UserNotVerifiedException(resend);
                }
            }
            return "";
        }
        return null;
    }

    private VerificationToken createVerificationToken(User user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setUser(user);
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));

        user.getVerificationTokens().add(verificationToken);

        return verificationToken;
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> opToken = verificationTokenRepository.findByToken(token);

        if (opToken.isEmpty()) {
            return false;
        }

        VerificationToken verificationToken = opToken.get();
        User user = verificationToken.getUser();

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            return false;
        }

        user.setEmailVerified(true);
        repository.save(user);
        verificationTokenRepository.deleteByUser(user);

        return true;
    }

    public void forgotPassword(String email) throws EmailNotFoundException, EmailFailureException {
        Optional<User> opUser = repository.findByEmail(email);

        if (opUser.isPresent()){
            User user = opUser.get();
            String token = jwtService.generatePasswordResetJWT(user);
            emailService.sendPasswordResetMail(user,token);
        }else {
            throw new EmailNotFoundException();
        }
    }

    public void resetPassword(PasswordResetBody body) throws UserNotFoundException {
        String email = jwtService.getResetPasswordEmail(body.getToken());
        Optional<User> opUser = repository.findByEmail(email);

        if (opUser.isPresent()){
            User user = opUser.get();
            user.setPassword(encryptionService.encryptPassword(body.getPassword()));
            repository.save(user);
        }
        else {
            throw new UserNotFoundException();
        }
    }

    public boolean userHasPermissionToUser(User user, Long id){
        return user.getId() == id;
    }


//    private boolean userHasPermission(LocalUser user, Long id){
//        return Objects.equals(user.getId(), id);
//    }

}
