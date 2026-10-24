package com.automation.petstore.tests;

import com.automation.petstore.base.BaseTest;
import com.automation.petstore.models.Order;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.automation.petstore.endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;

public class StoreTests extends BaseTest {

    private long orderId;
    private long petId = 10L;

    @BeforeMethod
    public void createTestOrder() {
        // Runs before EVERY @Test method, giving each one a fresh, known pet.
        orderId = System.currentTimeMillis(); // unique ID per run, avoids collisions

        Order newOrder = new Order(orderId, petId,1, "2025-01-01T00:00:00.000Z", false, "placed");

        given()
                .spec(requestSpec)
                .body(newOrder)
                .when()
                .post(STORE_ORDER)
                .then()
                .statusCode(200);
    }

    @Test
    public void fetchOrder_returnsCorrectOrder() {
        Order fetchedOrder =
                given()
                        .spec(requestSpec)
                        .pathParam("orderId", orderId)
                        .when()
                        .get(STORE_ORDER_BY_ID)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(Order.class);

        Assert.assertEquals(fetchedOrder.getStatus(), "placed");
    }

    @Test
    public void deleteOrder_returns200() {
        given()
                .spec(requestSpec)
                .pathParam("orderId", orderId)
                .when()
                .delete(STORE_ORDER_BY_ID)
                .then()
                .statusCode(200);
    }

    @Test
    public void getInventory_returns200() {
        given()
                .spec(requestSpec)
                .when()
                .get(STORE_INVENTORY)
                .then()
                .statusCode(200);
    }
}
