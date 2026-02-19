package com.ecom.service;

import com.ecom.api.model.RegistrationBody;
import com.ecom.exception.UserAlreadyExistsException;
import com.ecom.model.LocalUser;
import com.ecom.repository.UserRepository;
import org.slf4j.spi.LocationAwareLogger;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final EncryptionService encryptionService;

    public UserService(UserRepository repository, EncryptionService encryptionService) {
        this.repository = repository;
        this.encryptionService = encryptionService;
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
}
