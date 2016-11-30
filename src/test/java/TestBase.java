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
    static String accessToken;
    static RequestSpecification requestSpecification;

    public TestBase()
    {
        new TestBase("production");
    }

    public TestBase(String environment)
    {
        try {
            InputStream ios = new FileInputStream(new File("config.yaml"));
            Yaml yaml = new Yaml();
            Map config = (Map) yaml.load(ios);
            Map envConfig = (Map) config.get(environment);
            baseUrl = envConfig.get("baseUrl").toString();
            username = envConfig.get("username").toString();
            password = envConfig.get("password").toString();
            accessToken = this.getApiToken();
            requestSpecification = this.prepareRequest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private RequestSpecification prepareRequest()
    {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setRelaxedHTTPSValidation()
                .addHeader("User-Agent", "Foo/1.0 (https://github.com/milanoid/zonky-api-test)")
                .setContentType("application/x-www-form-urlencoded")
                .build();
        return requestSpecification;
    }

    private String getApiToken()
    {
        RequestSpecification requestSpecification = this.prepareRequest();

        accessToken =
                given(requestSpecification).
                        auth().preemptive().basic("web", "web").
                        formParam("username", username).
                        formParam("password", password).
                        formParam("grant_type", "password").
                        formParam("scope", "SCOPE_APP_WEB").
                when().
                        post("/oauth/token").
                then().
                        extract().path("access_token");
        return accessToken;
    }
}
