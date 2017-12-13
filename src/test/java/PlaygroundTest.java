import com.fasterxml.jackson.databind.util.JSONPObject;
import groovy.json.JsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;

import javax.json.Json;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;


public class PlaygroundTest extends TestBase {

//    @Test
//    public void dummyTest() {
//
//        given(requestSpecification).auth().oauth2(accessToken).when().get("/mobile/v1/application/183500").then().statusCode(200);
//    }

    @Test
    public void onboardingOverMobileApi() {

        given(requestSpecification.contentType(ContentType.JSON))
                .auth().oauth2(accessToken)
        .when()
                .body(Json.createObjectBuilder().build())
                .post("/mobile/v1/application")
        .then()
                .statusCode(201);

    }
}
