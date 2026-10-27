package com.automation.petstore.clients;

import com.automation.petstore.models.Order;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static com.automation.petstore.endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;

public class StoreClient {

    private final RequestSpecification spec;

    public StoreClient(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response create(Order order) {
        return given()
                .spec(spec)
                .body(order)
                .when()
                .post(STORE_ORDER);
    }

    public Response getById(long orderId) {
        return given()
                .spec(spec)
                .pathParam("orderId", orderId)
                .when()
                .get(STORE_ORDER_BY_ID);
    }

    public Response delete(long orderId) {
        return given()
                .spec(spec)
                .pathParam("orderId", orderId)
                .when()
                .delete(STORE_ORDER_BY_ID);
    }

    public Response getInventory() {
        return given()
                .spec(spec)
                .when()
                .get(STORE_INVENTORY);
    }
}