package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class ApiTests {

    private static final String BASE_URI = "http://localhost:3000";
    private static final String BASE_PATH = "/api/items";
    private static int createdItemId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = BASE_PATH;
        System.out.println("RestAssured Base URI set to: " + RestAssured.baseURI + RestAssured.basePath);
    }

    @Test(priority = 1)
    public void testCreateNewItem() {
        System.out.println("\n--- Running testCreateNewItem ---");
        Map<String, String> newItem = new HashMap<>();
        newItem.put("name", "New Test Item");
        newItem.put("description", "This is a description for a new item created by API test.");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newItem)
                .when()
                .post();

        response.then().statusCode(201);
        System.out.println("Status Code: " + response.getStatusCode());

        response.then().body("name", equalTo("New Test Item"))
                .body("description", equalTo("This is a description for a new item created by API test."));
        System.out.println("Response Body: " + response.asString());

        createdItemId = response.jsonPath().getInt("id");
        Assert.assertTrue(createdItemId > 0, "Created item ID should be greater than 0");
        System.out.println("Created Item ID: " + createdItemId);
    }

    @Test(priority = 2)
    public void testCreateItemWithMissingFields() {
        System.out.println("\n--- Running testCreateItemWithMissingFields ---");
        Map<String, String> newItem = new HashMap<>();
        newItem.put("name", "Item with Missing Desc");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newItem)
                .when()
                .post();

        response.then().statusCode(400);
        System.out.println("Status Code: " + response.getStatusCode());

        response.then().body("message", equalTo("Name and description are required"));
        System.out.println("Response Body: " + response.asString());
    }

    @Test(priority = 3)
    public void testGetAllItems() {
        System.out.println("\n--- Running testGetAllItems ---");
        Response response = RestAssured.given()
                .when()
                .get();

        response.then().statusCode(200);
        System.out.println("Status Code: " + response.getStatusCode());

        response.then().contentType(ContentType.JSON)
                .body("$", hasSize(greaterThanOrEqualTo(1)));
        System.out.println("Response Body: " + response.asString());
    }

    @Test(priority = 4)
    public void testGetSpecificItem() {
        System.out.println("\n--- Running testGetSpecificItem ---");
        Assert.assertTrue(createdItemId > 0, "No item ID captured from previous creation test.");

        Response response = RestAssured.given()
                .when()
                .get("/" + createdItemId);

        response.then().statusCode(200);
        System.out.println("Status Code: " + response.getStatusCode());

        response.then().body("id", equalTo(createdItemId));
        System.out.println("Response Body: " + response.asString());
    }

    @Test(priority = 5)
    public void testGetNonExistentItem() {
        System.out.println("\n--- Running testGetNonExistentItem ---");
        int nonExistentId = 9999;

        Response response = RestAssured.given()
                .when()
                .get("/" + nonExistentId);

        response.then().statusCode(404);
        System.out.println("Status Code: " + response.getStatusCode());

        response.then().body("message", equalTo("Item not found"));
        System.out.println("Response Body: " + response.asString());
    }

    @Test(priority = 6)
    public void testUpdateItem() {
        System.out.println("\n--- Running testUpdateItem ---");
        Assert.assertTrue(createdItemId > 0, "No item ID captured from previous creation test.");

        Map<String, String> updatedItem = new HashMap<>();
        updatedItem.put("name", "Updated Test Item Name");
        updatedItem.put("description", "Updated description for the test item.");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedItem)
                .when()
                .put("/" + createdItemId);

        response.then().statusCode(200);
        System.out.println("Status Code: " + response.getStatusCode());

        response.then().body("name", equalTo("Updated Test Item Name"))
                .body("description", equalTo("Updated description for the test item."));
        System.out.println("Response Body: " + response.asString());
    }

    @Test(priority = 7)
    public void testUpdateNonExistentItem() {
        System.out.println("\n--- Running testUpdateNonExistentItem ---");
        int nonExistentId = 9998;

        Map<String, String> updatedItem = new HashMap<>();
        updatedItem.put("name", "Attempt to Update Non-Existent");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedItem)
                .when()
                .put("/" + nonExistentId);

        response.then().statusCode(404);
        System.out.println("Status Code: " + response.getStatusCode());

        response.then().body("message", equalTo("Item not found"));
        System.out.println("Response Body: " + response.asString());
    }

    @Test(priority = 8)
    public void testDeleteItem() {
        System.out.println("\n--- Running testDeleteItem ---");
        Assert.assertTrue(createdItemId > 0, "No item ID captured from previous creation test.");

        Response response = RestAssured.given()
                .when()
                .delete("/" + createdItemId);

        response.then().statusCode(204);
        System.out.println("Status Code: " + response.getStatusCode());

        RestAssured.given()
                .when()
                .get("/" + createdItemId)
                .then()
                .statusCode(404)
                .body("message", equalTo("Item not found"));
        System.out.println("Verified item is deleted.");
    }

    @Test(priority = 9)
    public void testDeleteNonExistentItem() {
        System.out.println("\n--- Running testDeleteNonExistentItem ---");
        int nonExistentId = 9997;

        Response response = RestAssured.given()
                .when()
                .delete("/" + nonExistentId);

        response.then().statusCode(404);
        System.out.println("Status Code: " + response.getStatusCode());

        response.then().body("message", equalTo("Item not found"));
        System.out.println("Response Body: " + response.asString());
    }
}

