package com.airline.disruptiondetection;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class DisruptionHealthTest {

    @Test
    public void testHealthCheck() {
        given()
            .when().get("/q/health")
            .then()
            .statusCode(200)
            .body("status", equalTo("UP"));
    }

    @Test
    public void testReadinessCheck() {
        given()
            .when().get("/q/health/ready")
            .then()
            .statusCode(200);
    }
}