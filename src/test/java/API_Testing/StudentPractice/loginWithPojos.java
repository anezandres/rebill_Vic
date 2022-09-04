package API_Testing.StudentPractice;

import Pojos.logInPojos;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class loginWithPojos {

    @Test
    public void loginWithPojosData(){
        logInPojos data = new logInPojos();
        data.setPassword("TLA12345!");
        data.setUserName("anezj.andres@gmail.com");

        RestAssured.baseURI = "https://api.octoperf.com";
        String path = "/public/users/login";

        RestAssured.given()
                .queryParam("username", data.getUserName())
                .queryParam("password", data.getPassword())
                .when()
                .post(path)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .log().all();


    }
}
