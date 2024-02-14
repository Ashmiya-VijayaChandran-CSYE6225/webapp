package com.neu.csye6225.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.csye6225.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private String userToJsonString(User user){
        try{
            return new ObjectMapper().writeValueAsString(user);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private String encodeBase64String(String username, String password){
        String auth = username + ":" + password;
        byte[] encodedBase64Bytes = Base64.getEncoder().encode(auth.getBytes());
        return new String(encodedBase64Bytes);
    }
    @Test
    public void createUser() throws Exception {
        User user = new User();
        user.setUsername("username@gmail.com");
        user.setFirstName("Brooke");
        user.setLastName("Kuttan");
        user.setPassword("password");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encodeBase64String("username@gmail.com", "password"));

        mockMvc.perform(post("/v1/user")
                .content(userToJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/" + "v1/user/self")
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders)).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Brooke"))
                .andExpect(jsonPath("$.lastName").value("Kuttan"));
    }
}