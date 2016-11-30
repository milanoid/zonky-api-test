import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
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
        super();
    }

    public TestBase(String environment)
    {
        try {
            InputStream ios = new FileInputStream(new File("src/test/java/config.yaml"));
            Yaml yaml = new Yaml();
            Map config = (Map) yaml.load(ios);
            Map envConfig = (Map) config.get(environment);
            baseUrl = envConfig.get("baseUrl").toString();
            username = URLEncoder.encode(envConfig.get("username").toString(), "UTF-8");
            password = URLEncoder.encode(envConfig.get("password").toString(), "UTF-8");
            accessToken = TestBase.getApiToken();
            requestSpecification = TestBase.prepareRequest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static RequestSpecification prepareRequest()
    {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setRelaxedHTTPSValidation()
                .addHeader("User-Agent", "Foo/1.0 (https://github.com/milanoid/zonky-api-test)")
                .setContentType("application/x-www-form-urlencoded")
                .build();
        return requestSpecification;
    }

    public static String getApiToken()
    {
        String requestBody = String.format("username=%s&password=%s&grant_type=password&scope=SCOPE_APP_WEB", username, password);
        RequestSpecification requestSpecification = TestBase.prepareRequest();

        accessToken =
                given(requestSpecification).
                        auth().preemptive().basic("web", "web").
                        body(requestBody).
                when().
                        post("/oauth/token").
                then().
                        extract().path("access_token");
        return accessToken;
    }
}
