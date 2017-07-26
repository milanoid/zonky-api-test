import org.junit.Test;

import static io.restassured.RestAssured.given;


public class PlaygroundTest extends TestBase {

    @Test
    public void dummyTest() {

        given(requestSpecification).auth().oauth2(accessToken).when().get("/users/me/investments/tax-returns").then().statusCode(200);
    }
}
