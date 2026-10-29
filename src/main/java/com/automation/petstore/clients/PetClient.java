package com.automation.petstore.clients;

import com.automation.petstore.models.Pet;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;

import static com.automation.petstore.endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;

public class PetClient {

    private final RequestSpecification spec;

    public PetClient(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response create(Pet pet) {
        return given()
                .spec(spec)
                .body(pet)
                .when()
                .post(PET);
    }

    public Response getById(long petId) {
        return given()
                .spec(spec)
                .pathParam("petId", petId)
                .when()
                .get(PET_BY_ID);
    }

    public Response update(Pet pet) {
        return given()
                .spec(spec)
                .body(pet)
                .when()
                .put(PET);
    }

    public Response findByStatus(String status) {
        return given()
                .spec(spec)
                .queryParam("status", status)
                .when()
                .get(PET_FIND_BY_STATUS);
    }

    public Response delete(long petId) {
        return given()
                .spec(spec)
                .pathParam("petId", petId)
                .when()
                .delete(PET_BY_ID);
    }

    public Response uploadImage(long petId, File file) {
        return given()
                .spec(spec)
                .contentType("multipart/form-data")
                .pathParam("petId", petId)
                .multiPart("file", file)
                .when()
                .post(PET_UPLOAD_IMAGE);
    }
}