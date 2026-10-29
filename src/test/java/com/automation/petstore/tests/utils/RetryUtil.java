package com.automation.petstore.utils;

import io.restassured.response.Response;

import java.util.function.Supplier;

public class RetryUtil {

    private RetryUtil() {
        // utility class: not meant to be instantiated
    }

    /**
     * Retries an API call up to maxAttempts times, retrying only when
     * the response's status code does not match expectedStatus.
     * Waits delayMillis between attempts. Throws if all attempts fail.
     */
    public static Response retryUntilStatus(Supplier<Response> call, int expectedStatus,
                                            int maxAttempts, long delayMillis) {
        Response response = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            response = call.get();

            if (response.getStatusCode() == expectedStatus) {
                return response;
            }

            System.out.printf("Attempt %d/%d: expected status %d but got %d. Retrying...%n",
                    attempt, maxAttempts, expectedStatus, response.getStatusCode());

            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry wait was interrupted", e);
                }
            }
        }

        return response; // last attempt's response, even if it never matched
    }
}