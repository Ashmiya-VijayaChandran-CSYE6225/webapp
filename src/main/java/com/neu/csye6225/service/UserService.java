package com.neu.csye6225.service;

import com.neu.csye6225.model.User;
import com.neu.csye6225.model.UserDTO;
import com.neu.csye6225.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        String pass = passwordEncoderBCrypt(user.getPassword());
        user.setPassword(pass);
        return userRepository.save(user);
   }
    public String[] decodeBase64String(String s){
        byte[] decodedBase64Bytes = Base64.getDecoder().decode(s);
        String decodedBase64String = new String(decodedBase64Bytes, StandardCharsets.UTF_8);
        return decodedBase64String.split(":");
    }

    public String[] getSubStrings(String s){
        String base64SubStrings = s.substring("Basic ".length());
        String[] subStrings = decodeBase64String(base64SubStrings);
        return subStrings;
    }

    public boolean checkIsValidUser(String s){
        String[] subStrings = getSubStrings(s);
        String userName;
        String password;
        if(subStrings.length == 2){
            userName = subStrings[0];
            password = subStrings[1];
            User loggedInUser = userRepository.findByUsername(userName);
            return loggedInUser != null && bCryptPasswordEncoder.matches(password, loggedInUser.getPassword());
        }
        return false;
//        System.out.println(password);
//        User loggedInUser = userRepository.findByUsername(userName);
//        return loggedInUser != null && ((password != null || !password.isBlank()) && bCryptPasswordEncoder.matches(password, loggedInUser.getPassword()));
    }
   public String getUserNameFromAuth(String auth){
       String[] subStrings = getSubStrings(auth);
       return subStrings[0];
   }

   public User getUser(String auth) {
        String userName = getUserNameFromAuth(auth);
        return userRepository.findByUsername(userName);
   }

    public User getUserFromUserName(String userName) {
//        String userName = getUserNameFromAuth(auth);
        return userRepository.findByUsername(userName);
    }

    public String passwordEncoderBCrypt(String pass){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(pass);
    }
    public UserDTO userToUserDTOMapper(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setAccountCreated(user.getAccountCreated());
        userDTO.setAccountUpdated(user.getAccountUpdated());
        return userDTO;
    }
   public UserDTO updateUser(String auth, User requestBodyUser){
       User user = getUser(auth);
       if(null != requestBodyUser.getFirstName() && !requestBodyUser.getFirstName().isBlank()){
           user.setFirstName(requestBodyUser.getFirstName());
       }
       if(null != requestBodyUser.getLastName() && !requestBodyUser.getLastName().isBlank()){
           user.setLastName(requestBodyUser.getLastName());
       }
       if(null != requestBodyUser.getPassword() && !requestBodyUser.getPassword().isBlank()){
           user.setPassword(passwordEncoderBCrypt(requestBodyUser.getPassword()));
       }
       User savedUser = userRepository.save(user);
       return userToUserDTOMapper(savedUser);

   }

   public boolean checkIfValidRequestBody(User requestBodyUser){
        String userName = requestBodyUser.getUsername();
       String password = requestBodyUser.getPassword();
       String firstName = requestBodyUser.getFirstName();
       String lastName = requestBodyUser.getLastName();
       if(userName == null || password == null || firstName == null || lastName == null){
           return false;
       }
       return true;
   }

   public boolean containsNecessaryFields(User requestBodyUser){
       String password = requestBodyUser.getPassword();
       String firstName = requestBodyUser.getFirstName();
       String lastName = requestBodyUser.getLastName();
       if(password == null && firstName == null && lastName == null){
           return false;
       }
       return true;
   }
}
