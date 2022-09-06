package Rebill_API;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import java.io.File;

public class rebill {
    String organizationAlias;
    Response response;

    @Test
    public void signUp(){
        File requestBody = new File("src/resources/requestBody.json");

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("https://api.rebill.to/v2/auth/merchant/signup");

        response.then().assertThat().statusCode(201).extract().response().prettyPrint();
        organizationAlias = response.jsonPath().getString("organizationAlias");

        System.out.println("alias: " + organizationAlias);

    }
    @Test
    public void logIn(){
        File loginBody = new File("src/resources/loginBody.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(loginBody)
                .post("https://api.rebill.to/v2/auth/login/vicabu2")// send request to end point
                .then()
                .statusCode(201) // Verify status code = 200 or OK
                .extract() // Method that extracts response JSON data
                .response()
                .prettyPrint();
    }
}
