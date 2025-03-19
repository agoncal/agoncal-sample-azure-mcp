package org.agoncal.sample.mcp.azurestorage;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class AzureStorageResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/azurestorage")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy"));
    }

}