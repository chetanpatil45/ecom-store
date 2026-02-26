package com.ecom.api.controller.user;

import com.ecom.model.Address;
import com.ecom.model.LocalUser;
import com.ecom.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AddressRepository addressRepository;

    @GetMapping("/{userId}/address")
    public ResponseEntity<List<Address>> getAddress(
            @AuthenticationPrincipal LocalUser user,
            @PathVariable long userId){

        if (!userHasPermission(user, userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity
                    .ok(addressRepository.findByUser_Id(userId));
    }

    @PutMapping("/{userId}/address")
    public ResponseEntity<Address> putAddress(
            @AuthenticationPrincipal LocalUser user,
            @PathVariable long userId,
            @RequestBody Address address) {

        if (!userHasPermission(user, userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        address.setId(null);
        LocalUser refUser = new LocalUser();
        refUser.setId(userId);
        address.setUser(refUser);

        return ResponseEntity.ok(addressRepository.save(address));

    }

    private boolean userHasPermission(LocalUser user, Long id){
        return Objects.equals(user.getId(), id);
    }
}
