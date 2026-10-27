package com.automation.petstore.tests;

import com.automation.petstore.base.BaseTest;
import com.automation.petstore.clients.StoreClient;
import com.automation.petstore.models.Order;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.UUID;
import static org.hamcrest.Matchers.equalTo;

public class StoreTests extends BaseTest {

    private long orderId;
    private long petId = 10L;
    private StoreClient storeClient;

    @BeforeClass(alwaysRun = true)
    public void initClient() {
        storeClient = new StoreClient(requestSpec);
    }

    @BeforeMethod(alwaysRun = true)
    public void createTestOrder() {
        // Runs before EVERY @Test method, giving each one a fresh, known pet.
        orderId = System.currentTimeMillis(); // unique ID per run, avoids collisions
        storeClient.create(new Order(orderId, petId, 1, "2025-01-01T00:00:00.000Z", false, "placed"))
                .then()
                .statusCode(200);
    }

    @Test(groups = {"smoke", "regression"})
    public void fetchOrder_returnsCorrectOrder() {
        Order fetchedOrder = storeClient.getById(orderId)
                .then()
                .statusCode(200)
                .extract()
                .as(Order.class);

        Assert.assertEquals(fetchedOrder.getStatus(), "placed");
    }

    @Test(groups = {"regression"})
    public void deleteOrder_returns200() {
        storeClient.delete(orderId)
                .then()
                .statusCode(200);
    }

    @Test(groups = {"regression"})
    public void getInventory_returns200() {
        storeClient.getInventory()
                .then()
                .statusCode(200);
    }

    @DataProvider(name = "orderStatuses")
    public Object[][] orderStatuses() {
        return new Object[][] {
                {"placed"},
                {"approved"},
                {"delivered"}
        };
    }

    @Test(dataProvider = "orderStatuses", groups = {"regression"})
    public void createOrder_withStatus_returnsSameStatus(String status) {
        long id = Math.abs(UUID.randomUUID().getMostSignificantBits());
        storeClient.create(new Order(id, petId, 1, "2025-01-01T00:00:00.000Z", false, status))
                .then()
                .statusCode(200)
                .body("status", equalTo(status));
    }
}
