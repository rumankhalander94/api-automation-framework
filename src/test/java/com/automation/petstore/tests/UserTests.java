package com.automation.petstore.tests;

import com.automation.petstore.base.BaseTest;
import com.automation.petstore.models.User;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.automation.petstore.endpoints.Endpoints.USER;
import static com.automation.petstore.endpoints.Endpoints.USER_BY_USERNAME;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class UserTests extends BaseTest {

    private String username;

    @BeforeMethod
    public void createTestUser() {
        // Unique username per run: fixed prefix + timestamp
        username = "testuser_" + System.currentTimeMillis();

        User newUser = new User(
                System.currentTimeMillis(),
                username,
                "John",
                "Doe",
                username + "@example.com",
                "Password123",
                "9999999999",
                1
        );

        given()
                .spec(requestSpec)
                .body(newUser)
                .when()
                .post(USER)
                .then()
                .statusCode(200);
    }

    @Test
    public void fetchUser_returnsCorrectUser() {
        User fetchedUser =
                given()
                        .spec(requestSpec)
                        .pathParam("username", username)
                        .when()
                        .get(USER_BY_USERNAME)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(User.class);

        Assert.assertEquals(fetchedUser.getUsername(), username);
        Assert.assertEquals(fetchedUser.getFirstName(), "John");
        Assert.assertEquals(fetchedUser.getEmail(), username + "@example.com");
    }

    @Test
    public void deleteUser_returns200() {
        given()
                .spec(requestSpec)
                .pathParam("username", username)
                .when()
                .delete(USER_BY_USERNAME)
                .then()
                .statusCode(200);
    }
}