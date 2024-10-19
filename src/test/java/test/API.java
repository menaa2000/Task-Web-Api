package test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class API {
    public static void main(String[] args) {
        // Base URL for ReqRes
        RestAssured.baseURI = "https://reqres.in/api";

        // Create a JSON object containing user data
        String requestBody = "{ \"name\": \"John Doe\", \"job\": \"Software Engineer\" }";

        try {
            System.out.println("Starting user creation...");

            // Send POST Request
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/users");

            // Print the response for debugging
            System.out.println("Response: " + response.asString());

            // Assert that the response status code is 201 (Created)
            response.then().statusCode(201);

            // Validate that the response body contains the required data
            response.then()
                    .body("id", notNullValue())
                    .body("name", equalTo("John Doe"))
                    .body("job", equalTo("Software Engineer"));
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

    }
}
