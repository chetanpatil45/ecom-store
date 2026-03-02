package com.ecom.api.controller.user;

import com.ecom.api.model.DataChange;
import com.ecom.model.Address;
import com.ecom.model.LocalUser;
import com.ecom.repository.AddressRepository;
import com.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/address")
    public ResponseEntity<List<Address>> getAddress(
            @AuthenticationPrincipal LocalUser user,
            @PathVariable long userId){

        if (!userService.userHasPermissionToUser(user, userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity
                    .ok(addressRepository.findByUser_Id(userId));
    }

    @PutMapping("/{userId}/address")
    public ResponseEntity<Address> putAddress(
            @AuthenticationPrincipal LocalUser user,
            @PathVariable long userId,
            @RequestBody Address address) {

        if (!userService.userHasPermissionToUser(user, userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        address.setId(null);
        LocalUser refUser = new LocalUser();
        refUser.setId(userId);
        address.setUser(refUser);

        Address savedAddress = addressRepository.save(address);
        simpMessagingTemplate.convertAndSend("/topic/user/" + userId +"/address",
                new DataChange<>(DataChange.ChangeType.INSERT, address));
        return ResponseEntity.ok(savedAddress);

    }

    @PatchMapping("/{userId}/address/{addressId}")
    public ResponseEntity patchAddress(
            @AuthenticationPrincipal LocalUser user,
            @PathVariable long userId,
            @RequestBody Address address,
            @PathVariable long addressId){

        if (!userService.userHasPermissionToUser(user, userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if (address.getId() == addressId){
            Optional<Address> ogAddress = addressRepository.findById(addressId);
            if (ogAddress.isPresent()){
                LocalUser originalUser = ogAddress.get().getUser();
                if (originalUser.getId() == userId){
                    address.setUser(originalUser);
                    Address savedAddress = addressRepository.save(address);
                    simpMessagingTemplate.convertAndSend("/topic/user/" + userId +"/address",
                            new DataChange<>(DataChange.ChangeType.UPDATE, address));
                    return ResponseEntity.ok(savedAddress);
                }
            }
        }

        return ResponseEntity.badRequest().build();
    }

}
