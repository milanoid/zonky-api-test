import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Created by milan on 16-Jan-17.
 */
public class PlaygroundTest extends TestBase {

    static TestBase config;

    @BeforeClass
    public static void setUp() {
        config = new TestBase(System.getProperty("env"));
    }

    @Test
    public void dummyTest() {

        given(requestSpecification).auth().oauth2(accessToken).when().get("/users/me/investments/tax-returns").then().statusCode(200);
    }
}
