package workspaceRestAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class E2E_Project {

    public String path;
    String memberOf = "/workspaces/member-of";
    Map<String, String> variables;
    String workspaceId;
    String userId;
    Response response;

    String projectID;

    // What is a TestNG annotation that allows us to run a Test Before each Test
    @BeforeTest
    public String setupLogInAndToken(){
        RestAssured.baseURI = "https://api.octoperf.com";
        path = "/public/users/login";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("password", "TLA12345!");
        map.put("username", "anezj.andres@gmail.com");

        return RestAssured.given()
                .queryParams(map)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .post(path)// send request to end point
                .then()
                .statusCode(SC_OK) // Verify status code = 200 or OK
                .extract() // Method that extracts response JSON data
                .body() // Body Extracted as JSON format
                .jsonPath() // Navigate using jsonPath
                .get("token"); // get value for key Token
    }

    // Write a test for API endpoint member-of
    @Test
    public void memberOf(){
        response = RestAssured.given()
                .header("Authorization", setupLogInAndToken())
                .when()
                .get(memberOf)
                .then()
                .log().all()
                .extract().response();

        // Verify status code
        Assert.assertEquals(SC_OK, response.statusCode());
        Assert.assertEquals("Default", response.jsonPath().getString("name[0]"));
        // TODO add tests for ID, userID, Description

        // Save the workspaceId
        workspaceId = response.jsonPath().get("id[0]");

        // Save the userId
        userId = response.jsonPath().getString("userId[0]");

        // Store data as key and value
        variables = new HashMap<String, String>();
        variables.put("workspaceId", workspaceId);
        variables.put("userId", userId);

        //TODO Get results from Map
        for (Map.Entry<String, String> entry : variables.entrySet())
            System.out.println("Key = " + entry.getKey() +
                    " Value = " + entry.getValue());
    }

    @Test(dependsOnMethods = {"memberOf"})
    public void createProject(){
        String requestBody = "{\"id\":\"\",\"created\":\"2021-03-11T06:15:20.845Z\",\"lastModified\":\"2021-03-11T06:15:20.845Z\",\"userId\":\"" + variables.get("userId") + "\",\"workspaceId\":\"" + variables.get("workspaceId") + "\",\"name\":\"testingAgain\",\"description\":\"Testing\",\"type\":\"DESIGN\",\"tags\":[]}";

        Response createProject = RestAssured.given()
                .headers("Content-type", "application/json")
                .headers("Authorization", setupLogInAndToken())
                .and()
                .body(requestBody)
                .when()
                .post("/design/projects")
                .then()
                .extract()
                .response();

        System.out.println(createProject.prettyPrint());

        // Create TestNG Assert Name, id, userId, workspaceId
        Assert.assertEquals("testingAgain", createProject.jsonPath().getString("name"));

        // Using Hamcrest Matchers Validation:
        assertThat(createProject.jsonPath().getString("name"), is("testingAgain"));

        // Store id
        projectID = createProject.jsonPath().get("id");
        System.out.println("New projectID: " + projectID);
    }

    @Test(dependsOnMethods = {"memberOf", "createProject"})
    public void updateProject(){
        String requestBody2 = "{\"created\":1615443320845,\"description\":\"AndresUpdating\",\"id\":\"" + projectID + "\",\"lastModified\":1629860121757,\"name\":\"AndresAnez Update\",\"tags\":[],\"type\":\"DESIGN\",\"userId\":\"" + variables.get("userId") + "\",\"workspaceId\":\"" + variables.get("workspaceId") + "\"}";
        Response updateProject = RestAssured.given()
                .headers("Content-type", "application/json")
                .headers("Authorization", setupLogInAndToken())
                .and()
                .body(requestBody2)
                .when()
                .put("/design/projects/" + projectID)
                .then()
                .extract()
                .response();

        System.out.println(updateProject.jsonPath().prettyPrint());


    }

    @Test(dependsOnMethods = {"memberOf", "createProject", "updateProject"})
    public void deleteProject(){
        response = RestAssured.given()
                .headers("Authorization", setupLogInAndToken())
                .when()
                .delete("/design/projects/" + projectID)
                .then()
                .log().all()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 204);
    }
}
