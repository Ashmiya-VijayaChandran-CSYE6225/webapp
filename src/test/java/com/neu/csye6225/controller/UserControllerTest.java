package com.neu.csye6225.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.csye6225.model.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import java.util.Base64;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(){
        RestAssured.port = port;
        RestAssured.basePath = "v1/user";
    }

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
    public void createUser() {
        User user = new User();
        user.setUsername("username@gmail.com");
        user.setFirstName("Brooke");
        user.setLastName("Kuttan");
        user.setPassword("password");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encodeBase64String("username@gmail.com", "password"));

        given()
                .contentType(ContentType.JSON)
                .body(userToJsonString(user))
                .when()
                .post()
                .then().statusCode(201);

        given()
                .headers(httpHeaders)
                .when()
                .get("/self")
                .then()
                .statusCode(200)
                .body("username", equalTo("username@gmail.com"))
                .body("firstName", equalTo("Brooke"))
                .body("lastName", equalTo("Kuttan"));
    }
}