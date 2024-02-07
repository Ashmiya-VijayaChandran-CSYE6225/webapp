package com.neu.csye6225.controller;

import com.neu.csye6225.model.User;
import com.neu.csye6225.model.UserDTO;
import com.neu.csye6225.service.UserService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<Object> createUser(@RequestHeader(value = "Authorization", required = false) String auth, @RequestBody User requestBody) {
        if(auth == null && (requestBody.getUsername() != null && userService.getUserFromUserName(requestBody.getUsername()) == null) && userService.checkIfValidRequestBody(requestBody)) {
            User createdUser = userService.createUser(requestBody);
            UserDTO createdUserDTO = userService.userToUserDTOMapper(createdUser);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .body(createdUserDTO);
        }
        else return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .build();
    }

    @GetMapping("/self")
    public ResponseEntity<Object> getUser(@RequestHeader("Authorization") String auth) {
        if(userService.checkIsValidUser(auth)){
            User loggedInUser = userService.getUser(auth);
            UserDTO loggedInUserDTO = userService.userToUserDTOMapper(loggedInUser);
            return ResponseEntity
                    .ok()
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .body(loggedInUserDTO);
        }
        else return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .build();
    }

    @PutMapping("/self")
    public ResponseEntity<Object> updateUser(@RequestHeader("Authorization") String auth, @RequestBody User requestBody){
        if(userService.checkIsValidUser(auth)){
            if(userService.containsNecessaryFields(requestBody) && requestBody.getUsername() == null && requestBody.getAccountCreated() == null && requestBody.getAccountUpdated() == null){
                userService.updateUser(auth, requestBody);
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .cacheControl(CacheControl.noCache().mustRevalidate())
                        .build();
            }
            else{
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .cacheControl(CacheControl.noCache().mustRevalidate())
                        .build();
            }
        }
        else return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .build();
    }
}
