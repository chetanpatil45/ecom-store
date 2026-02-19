package com.ecom.service;

import com.ecom.api.model.LoginBody;
import com.ecom.api.model.RegistrationBody;
import com.ecom.exception.UserAlreadyExistsException;
import com.ecom.model.LocalUser;
import com.ecom.repository.UserRepository;
import org.slf4j.spi.LocationAwareLogger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;

    public UserService(UserRepository repository, EncryptionService encryptionService, JWTService jwtService) {
        this.repository = repository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException{

        if (repository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()
                || repository.findByEmail(registrationBody.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User already Exists");
        }

        LocalUser localUser = new LocalUser();
        localUser.setUsername(registrationBody.getUsername());
        localUser.setEmail(registrationBody.getEmail());

        // TODO- ENCRYPT PASSWORD USING BCRYPT-ALGORITHM
        localUser.setPassword(encryptionService.encryptPassword((registrationBody.getPassword())));
        localUser.setFirstName(registrationBody.getFirstName());
        localUser.setLastName(registrationBody.getLastName());

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
}
