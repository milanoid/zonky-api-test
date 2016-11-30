import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MarketPlaceTest {

    static String username;
    static String password;
    static String accessToken;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://api.zonky.cz";
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.proxy("localhost", 8888);

        try {
            InputStream ios = new FileInputStream(new File("src/test/java/config.yaml"));
            Yaml yaml = new Yaml();
            Map config = (Map) yaml.load(ios);
            Map usersConfig = (Map) config.get("User");
            username = usersConfig.get("username").toString();
            password = usersConfig.get("password").toString();

            username = URLEncoder.encode(username, "UTF-8");
            password = URLEncoder.encode(password, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String requestBody = String.format("username=%s&password=%s&grant_type=password&scope=SCOPE_APP_WEB", username, password);

        // Get token first
        accessToken =
                given().
                        contentType("application/x-www-form-urlencoded").
                        header("User-Agent", "Foo/1.0 (https://github.com/john.doe/foo)").
                        auth().preemptive().basic("web", "web").
                        body(requestBody).
                        when().
                        post("https://api.zonky.cz/oauth/token").
                        then().
                        statusCode(200).
                        extract().path("access_token");
    }

    @Test
    public void MarketPlaceOffersLoans() {

        Response marketPlaceResponse =
        given().
                auth().oauth2(accessToken).
                header("User-Agent", "Foo/1.0 (https://github.com/john.doe/foo)").
        when().
                get("/loans/marketplace").
        then().
                statusCode(200).
                extract().response();

        ArrayList loanOffers = marketPlaceResponse.jsonPath().get();

        assertThat(loanOffers.size(), greaterThan(0));
    }
}
