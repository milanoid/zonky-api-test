import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.yaml.snakeyaml.Yaml;

import javax.swing.plaf.synth.SynthTextAreaUI;
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
    static Boolean proxyEnabled;
    static String proxyHost;
    static Integer proxyPort;
    static RequestSpecification requestSpecification;

    public TestBase() {
        new TestBase("test");
    }

    public TestBase(String environment) {
        try {
            InputStream ios = new FileInputStream(new File("config.yaml"));
            Yaml yaml = new Yaml();
            Map config = (Map) yaml.load(ios);
            Map envConfig = (Map) config.get(environment);
            baseUrl = (String) envConfig.get("baseUrl");
            username = (String) envConfig.get("username");
            password = (String) envConfig.get("password");
            proxyEnabled = (Boolean) envConfig.get("proxyEnabled");
            proxyHost = (String) envConfig.get("proxyHost");
            proxyPort = (Integer) envConfig.get("proxyPort");
        } catch (Exception e) {
            System.out.print("Could not read some values from config.yaml.");
        }

        try {
            accessToken = this.getApiToken();
        } catch (Exception e) {
            System.out.print(e);
        }


        requestSpecification = this.prepareRequest();
    }


    private RequestSpecification prepareRequest() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setRelaxedHTTPSValidation()
                .addHeader("User-Agent", "Foo/1.0 (https://github.com/milanoid/zonky-api-test)")
                .setContentType("application/x-www-form-urlencoded")
                .addHeader("Origin", baseUrl);

        if (proxyEnabled) builder.setProxy(proxyHost, proxyPort);

        return builder.build();
    }

    private String getApiToken() throws Exception {
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

        if (accessToken == null)
            throw new Exception(String.format("Could not get API token for \n username:%s \n password:%s\n", username, password));

        return accessToken;
    }
}
