import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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

    static String baseUrl;
    static String username;
    static String password;
    static String accessToken;
    static RequestSpecification requestSpecification;

    @BeforeClass
    public static void setUp() {

        String env = System.getProperty("env");

        try {
            InputStream ios = new FileInputStream(new File("src/test/java/config.yaml"));
            Yaml yaml = new Yaml();
            Map config = (Map) yaml.load(ios);
            Map envConfig = (Map) config.get(env);
            baseUrl = envConfig.get("baseUrl").toString();
            username = envConfig.get("username").toString();
            password = envConfig.get("password").toString();

            username = URLEncoder.encode(username, "UTF-8");
            password = URLEncoder.encode(password, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setRelaxedHTTPSValidation()
                .addHeader("User-Agent", "Foo/1.0 (https://github.com/milanoid/zonky-api-test)")
                .setContentType("application/x-www-form-urlencoded")
                .build();

        String requestBody = String.format("username=%s&password=%s&grant_type=password&scope=SCOPE_APP_WEB", username, password);

        // Get token first
        accessToken =
                given(requestSpecification).
                        auth().preemptive().basic("web", "web").
                        body(requestBody).
                when().
                        post("https://api.zonky.cz/oauth/token").
                then().
                        extract().path("access_token");
    }

    @Test
    public void marketPlaceOffersLoans() {

        Response marketPlaceResponse =
        given(requestSpecification).
                auth().oauth2(accessToken).
        when().
                get("/loans/marketplace").
        then().
                statusCode(200).
                extract().response();

        ArrayList loanOffers = marketPlaceResponse.jsonPath().get();

        assertThat(loanOffers.size(), greaterThan(0));
    }
}
