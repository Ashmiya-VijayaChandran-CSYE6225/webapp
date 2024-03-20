package com.neu.csye6225.controller;

import com.neu.csye6225.model.User;
import com.neu.csye6225.model.UserDTO;
import com.neu.csye6225.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        final Logger logger = LoggerFactory.getLogger(UserController.class);
        if(auth == null && (requestBody.getUsername() != null && userService.getUserFromUserName(requestBody.getUsername()) == null) && userService.checkIfValidRequestBody(requestBody)) {
            User createdUser = userService.createUser(requestBody);
            UserDTO createdUserDTO = userService.userToUserDTOMapper(createdUser);
            logger.info("The User Post request is successful!");
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .body(createdUserDTO);
        }
        else {
            logger.error("The User Post request is unsuccessful. Check if you've included incorrect request body or an existing username.");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .build();
        }
    }

    @GetMapping("/self")
    public ResponseEntity<Object> getUser(@RequestHeader("Authorization") String auth, @RequestBody(required = false) String requestBody) {
        final Logger logger = LoggerFactory.getLogger(UserController.class);
        if(userService.checkIsValidUser(auth)){
            if(requestBody == null) {
                User loggedInUser = userService.getUser(auth);
                UserDTO loggedInUserDTO = userService.userToUserDTOMapper(loggedInUser);
                logger.debug("User Details to be returned:"+loggedInUserDTO);
                logger.info("Get Request for User - successful!");
                return ResponseEntity
                        .ok()
                        .cacheControl(CacheControl.noCache().mustRevalidate())
                        .body(loggedInUserDTO);
            }
            else{
                logger.warn("Request Body shouldn't be provided");
                logger.error("Request Failed as body is provided");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .cacheControl(CacheControl.noCache().mustRevalidate())
                        .build();
            }
        }
        else {
            logger.error("Request failed due to Unauthorized User.");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .build();
        }
    }

    @PutMapping("/self")
    public ResponseEntity<Object> updateUser(@RequestHeader("Authorization") String auth, @RequestBody User requestBody){
        final Logger logger = LoggerFactory.getLogger(UserController.class);
        if(userService.checkIsValidUser(auth)){
            if(userService.containsNecessaryFields(requestBody) && requestBody.getUsername() == null && requestBody.getAccountCreated() == null && requestBody.getAccountUpdated() == null){
                userService.updateUser(auth, requestBody);
                logger.info("Put Request for User - successful!");
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .cacheControl(CacheControl.noCache().mustRevalidate())
                        .build();
            }
            else{
                logger.warn("Fields like username, created date, updated date cannot be updated.");
                logger.error("Request Failed as the request body is incorrect.");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .cacheControl(CacheControl.noCache().mustRevalidate())
                        .build();
            }
        }
        else {
            logger.error("Request failed due to Unauthorized User.");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .build();
        }
    }
}
