package com.automation.petstore.tests;

import com.automation.petstore.base.BaseTest;
import com.automation.petstore.models.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.UUID;

import static com.automation.petstore.endpoints.Endpoints.PET;
import static com.automation.petstore.endpoints.Endpoints.PET_BY_ID;
import static com.automation.petstore.endpoints.Endpoints.PET_FIND_BY_STATUS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

public class PetTests extends BaseTest {

    private long petId;

    @BeforeMethod(alwaysRun = true)
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

    @Test(groups = {"smoke", "regression"})
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

    @Test(groups = {"regression"})
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

    @Test(groups = {"regression"})
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

    @Test(groups = {"regression"})
    public void deletePet_returns200() {
        given()
                .spec(requestSpec)
                .pathParam("petId", petId)
                .when()
                .delete(PET_BY_ID)
                .then()
                .statusCode(200);
    }

    @DataProvider(name = "petStatuses")
    public Object[][] petStatuses() {
        return new Object[][] {
                {"available"},
                {"pending"},
                {"sold"}
        };
    }

    @Test(dataProvider = "petStatuses", groups = {"regression"})
    public void createPet_withStatus_returnsSameStatus(String status) {
        long id = Math.abs(UUID.randomUUID().getMostSignificantBits());

        Pet pet = new Pet(id, "doggie", status);

        given()
                .spec(requestSpec)
                .body(pet)
                .when()
                .post(PET)
                .then()
                .statusCode(200)
                .body("status", equalTo(status));
    }
}
