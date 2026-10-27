    package com.automation.petstore.tests;

    import com.automation.petstore.base.BaseTest;
    import com.automation.petstore.clients.UserClient;
    import com.automation.petstore.models.User;
    import org.testng.Assert;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.BeforeMethod;
    import org.testng.annotations.Test;

    public class UserTests extends BaseTest {

        private String username;
        private UserClient userClient;

        @BeforeClass(alwaysRun = true)
        public void initClient() {
            userClient = new UserClient(requestSpec);
        }

        @BeforeMethod(alwaysRun = true)
        public void createTestUser() {
            // Unique username per run: fixed prefix + timestamp
            username = "testuser_" + System.currentTimeMillis();

            userClient.create(new User(
                            System.currentTimeMillis(),
                            username,
                            "John",
                            "Doe",
                            username + "@example.com",
                            "Password123",
                            "9999999999",
                            1))
                    .then()
                    .statusCode(200);
        }

        @Test(groups = {"smoke", "regression"})
        public void fetchUser_returnsCorrectUser() {
            User fetchedUser = userClient.getByUsername(username)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(User.class);

            Assert.assertEquals(fetchedUser.getUsername(), username);
            Assert.assertEquals(fetchedUser.getFirstName(), "John");
            Assert.assertEquals(fetchedUser.getEmail(), username + "@example.com");
        }

        @Test(groups = {"regression"})
        public void deleteUser_returns200() {
            userClient.delete(username)
                    .then()
                    .statusCode(200);
        }
    }