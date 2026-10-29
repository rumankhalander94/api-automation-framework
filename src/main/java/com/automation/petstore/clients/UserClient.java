package com.automation.petstore.clients;

import com.automation.petstore.models.User;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static com.automation.petstore.endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;

public class UserClient {

    private final RequestSpecification spec;

    public UserClient(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response create(User user) {
        return given()
                .spec(spec)
                .body(user)
                .when()
                .post(USER);
    }

    public Response getByUsername(String username) {
        return given()
                .spec(spec)
                .pathParam("username", username)
                .when()
                .get(USER_BY_USERNAME);
    }

    public Response delete(String username) {
        return given()
                .spec(spec)
                .pathParam("username", username)
                .when()
                .delete(USER_BY_USERNAME);
    }

    public Response login(String username, String password) {
        return given()
                .spec(spec)
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get(USER_LOGIN);
    }

    public Response logout() {
        return given()
                .spec(spec)
                .when()
                .get(USER_LOGOUT);
    }

}
