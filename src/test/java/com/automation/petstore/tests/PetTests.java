package com.automation.petstore.tests;

import com.automation.petstore.base.BaseTest;
import com.automation.petstore.models.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.automation.petstore.endpoints.Endpoints.PET;
import static com.automation.petstore.endpoints.Endpoints.PET_BY_ID;
import static com.automation.petstore.endpoints.Endpoints.PET_FIND_BY_STATUS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

public class PetTests extends BaseTest {

    private long petId;

    @BeforeMethod
    public void createTestPet() {
        petId = System.currentTimeMillis();

        Pet newPet = new Pet(petId, "doggie", "available");

        given()
                .spec(requestSpec)
                .body(newPet)
                .when()
                .post(PET)
                .then()
                .statusCode(200);
    }

    @Test
    public void fetchPet_returnsCorrectPet() {
        Pet fetchedPet =
                given()
                        .spec(requestSpec)
                        .pathParam("petId", petId)
                        .when()
                        .get(PET_BY_ID)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(Pet.class);

        Assert.assertEquals(fetchedPet.getName(), "doggie");
        Assert.assertEquals(fetchedPet.getStatus(), "available");
    }

    @Test
    public void updatePet_returns200() {
        Pet updatedPet = new Pet(petId, "doggie", "sold");

        given()
                .spec(requestSpec)
                .body(updatedPet)
                .when()
                .put(PET)
                .then()
                .statusCode(200)
                .body("status", equalTo("sold"));
    }

    @Test
    public void findPetsByStatus_returnsOnlyMatchingStatus() {
        given()
                .spec(requestSpec)
                .queryParam("status", "available")
                .when()
                .get(PET_FIND_BY_STATUS)
                .then()
                .statusCode(200)
                .body("status", everyItem(equalTo("available")));
    }

    @Test
    public void deletePet_returns200() {
        given()
                .spec(requestSpec)
                .pathParam("petId", petId)
                .when()
                .delete(PET_BY_ID)
                .then()
                .statusCode(200);
    }
}