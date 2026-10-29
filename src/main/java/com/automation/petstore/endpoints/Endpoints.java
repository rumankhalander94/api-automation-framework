package com.automation.petstore.endpoints;

public final class Endpoints {

    private Endpoints() {
        // utility class: not meant to be instantiated
    }

    // Pet
    public static final String PET = "/pet";
    public static final String PET_BY_ID = "/pet/{petId}";
    public static final String PET_FIND_BY_STATUS = "/pet/findByStatus";
    public static final String PET_UPLOAD_IMAGE = "/pet/{petId}/uploadFile";

    // Store
    public static final String STORE_ORDER = "/store/order";
    public static final String STORE_ORDER_BY_ID = "/store/order/{orderId}";
    public static final String STORE_INVENTORY = "/store/inventory";

    // User
    public static final String USER = "/user";
    public static final String USER_BY_USERNAME = "/user/{username}";
}