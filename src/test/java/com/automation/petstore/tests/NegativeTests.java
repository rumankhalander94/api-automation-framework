package com.automation.petstore.tests;

import com.automation.petstore.base.BaseTest;
import com.automation.petstore.models.ApiResponse;
import com.automation.petstore.models.Pet;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

import static com.automation.petstore.endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;

public class NegativeTests extends BaseTest {

    @Test(groups = {"negative"})
    public void getPet_nonExistentId_returns404() {
        long nonExistentId = Math.abs(UUID.randomUUID().getMostSignificantBits());

        ApiResponse error =
                given()
                        .spec(requestSpec)
                        .pathParam("petId", nonExistentId)
                        .when()
                        .get(PET_BY_ID)
                        .then()
                        .statusCode(404)
                        .extract()
                        .as(ApiResponse.class);

        Assert.assertEquals(error.getType(), "error");
        Assert.assertEquals(error.getMessage(), "Pet not found");
    }

    @Test(groups = {"negative"})
    public void getUser_nonExistentUsername_returns404() {
        String nonExistentUser = "no_such_user_" + System.currentTimeMillis();

        ApiResponse error =
                given()
                        .spec(requestSpec)
                        .pathParam("username", nonExistentUser)
                        .when()
                        .get(USER_BY_USERNAME)
                        .then()
                        .statusCode(404)
                        .extract()
                        .as(ApiResponse.class);

        Assert.assertEquals(error.getType(), "error");
        Assert.assertEquals(error.getMessage(), "User not found");
    }

    @Test(groups = {"negative"})
    public void getOrder_outOfRangeId_returns404() {
        long outOfRangeOrderId = 100_000 + Math.abs(UUID.randomUUID().getMostSignificantBits() % 900_000);

        ApiResponse error =
                given()
                        .spec(requestSpec)
                        .pathParam("orderId", outOfRangeOrderId)
                        .when()
                        .get(STORE_ORDER_BY_ID)
                        .then()
                        .statusCode(404)
                        .extract()
                        .as(ApiResponse.class);

        Assert.assertEquals(error.getType(), "error");
        Assert.assertEquals(error.getMessage(), "Order not found");
    }

    @Test(groups = {"negative"})
    public void deletePet_twice_secondCallReturns404() {
        long petId = System.currentTimeMillis();

        // Arrange: create a pet so there is something to delete
        Pet pet = new Pet(petId, "doggie", "available");

        given()
                .spec(requestSpec)
                .body(pet)
                .when()
                .post(PET)
                .then()
                .statusCode(200);

        // First delete: should succeed
        given()
                .spec(requestSpec)
                .pathParam("petId", petId)
                .when()
                .delete(PET_BY_ID)
                .then()
                .statusCode(200);

        // Second delete: the pet is already gone.
        // The API returns 404 with an empty body here (unlike GET errors, which return a JSON error object).
        given()
                .spec(requestSpec)
                .pathParam("petId", petId)
                .when()
                .delete(PET_BY_ID)
                .then()
                .statusCode(404);
    }
}