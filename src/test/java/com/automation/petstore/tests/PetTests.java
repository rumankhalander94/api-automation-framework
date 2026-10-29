package com.automation.petstore.tests;

import com.automation.petstore.base.BaseTest;
import com.automation.petstore.clients.PetClient;
import com.automation.petstore.models.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

public class PetTests extends BaseTest {

    private long petId;
    private PetClient petClient;

    @BeforeClass(alwaysRun = true)
    public void initClient() {
        petClient = new PetClient(requestSpec);
    }

    @BeforeMethod(alwaysRun = true)
    public void createTestPet() {
        petId = System.currentTimeMillis();
        petClient.create(new Pet(petId, "doggie", "available"))
                .then()
                .statusCode(200);
    }

    @Test(groups = {"smoke", "regression"})
    public void fetchPet_returnsCorrectPet() {
        Pet fetchedPet = petClient.getById(petId)
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

        petClient.update(updatedPet)
                .then()
                .statusCode(200)
                .body("status", equalTo("sold"));
    }

    @Test(groups = {"regression"})
    public void findPetsByStatus_returnsOnlyMatchingStatus() {
        petClient.findByStatus("available")
                .then()
                .statusCode(200)
                .body("status", everyItem(equalTo("available")));
    }

    @Test(groups = {"regression"})
    public void deletePet_returns200() {
        petClient.delete(petId)
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
        petClient.create(new Pet(id, "doggie", status))
                .then()
                .statusCode(200)
                .body("status", equalTo(status));
    }

    @Test(groups = {"regression"}, enabled = false)
    public void uploadPetImage_returns200() {
        // Disabled: petstore.swagger.io's /pet/{petId}/uploadFile endpoint
        // returns a generic 404 even for valid requests (confirmed independently via curl).
        // This appears to be a server-side issue with the public sandbox, not a client bug.
        URL fileUrl = getClass().getClassLoader().getResource("test-pet-photo.txt");
        File file = new File(fileUrl.getFile());

        petClient.uploadImage(petId, file)
                .then()
                .statusCode(200)
                .body("message", containsString("File uploaded"));
    }
}
