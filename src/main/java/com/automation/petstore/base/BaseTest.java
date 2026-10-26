package com.automation.petstore.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import com.automation.petstore.config.ConfigManager;

public class BaseTest {

    protected static RequestSpecification requestSpec;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = ConfigManager.get("base.url");

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();

    }
}