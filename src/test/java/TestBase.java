import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestBase {

    static String baseUrl;
    static String username;
    static String password;
    static String origin;
    static String accessToken;
    static RequestSpecification requestSpecification;
    static RequestSpecification withoutOauth2spec;

    public TestBase() {
        new TestBase(System.getenv("ENV"));
    }

    public TestBase(String environment) {
        try {
            InputStream ios = new FileInputStream(new File("config.yaml"));
            Yaml yaml = new Yaml();
            Map config = (Map) yaml.load(ios);
            Map envConfig = (Map) config.get(environment);

            baseUrl = (String) envConfig.get("baseUrl");
            origin = (String) envConfig.get("origin");
            username = (String) envConfig.get("username");
            password = (String) envConfig.get("password");

        } catch (Exception e) {
            System.out.print("Could not read some values from config.yaml.");
        }

        try {
            accessToken = this.getApiToken();
        } catch (Exception e) {
            System.out.print(e);
        }


        requestSpecification = this.prepareOauth2Request();
    }


    private RequestSpecification prepareOauth2Request() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setRelaxedHTTPSValidation()
                .addHeader("User-Agent", "Foo/1.0 (https://github.com/milanoid/zonky-api-test)")
                .setContentType("application/x-www-form-urlencoded")
                .addHeader("Origin", origin);

        // this might be handy when debugging
        builder.setProxy("127.0.0.1", 8888);
        return builder.build();
    }

    private String getApiToken() throws Exception {
        RequestSpecification requestSpecification = this.prepareOauth2Request();

        accessToken =
                given(requestSpecification).
//                        auth().preemptive().basic("web", "web").
                        auth().preemptive().basic("mobileapp", "mobileapp").
                        formParam("username", username).
                        formParam("password", password).
                        formParam("grant_type", "password").
                        formParam("scope", "SCOPE_APP_WEB").
                        when().
                        post("/oauth/token").
                        then().
                        extract().path("access_token");

        if (accessToken == null)
            throw new Exception(String.format("Could not get API token for \n username:%s\n", username));

        return accessToken;
    }
}
