import org.junit.Before;
import org.junit.Test;
import requests.Payloads;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static requests.Payloads.createUserPayload;


public class PlaygroundTest extends TestBase {

    String testUser;
    String password = "Zebra2014";
    String accessToken;
    int loanId;

    @Before
    public void createTestUser() {

        testUser =
                given(TestBase.createUserRequest)
                        .when()
                        .body(createUserPayload())
                        .post("/mobile/v1/users")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("email");

        accessToken = TestBase.getApiToken(testUser, password);
    }

    @Test
    public void onboardingOverMobileApi() {

        // create application
        loanId =
                given(TestBase.requestSpecification)
                        .auth().oauth2(accessToken)
                        .when()
                        .body(Payloads.createApplication())
                        .post("/mobile/v1/application")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract()
                        .path("id");

        // update application
        given(TestBase.requestSpecification)
                .auth().oauth2(accessToken)
                .when()
                .body(Payloads.updateApplication())
                .patch(String.format("/mobile/v1/application/%s", Integer.toString(loanId)))
                .then()
                .log()
                .body()
                .statusCode(200);

        // finish application
        given(TestBase.requestSpecification)
                .auth().oauth2(accessToken)
                .when()
                .post(String.format("/mobile/v1/application/%s/finish", Integer.toString(loanId)))
                .then()
                .log().ifError()
                .assertThat().statusCode(400).and().body("error", is("AUTHORIZATION_SMS_REQUIRED"));

        // finish application - sign with SMS code
        given(TestBase.requestSpecification)
                .auth().oauth2(accessToken)
                .header("X-Authorization-Code", TestBase.getSMSverificationCode(testUser))
                .when()
                .post(String.format("/mobile/v1/application/%s/finish", Integer.toString(loanId)))
                .then()
                .log().ifError()
                .assertThat().statusCode(204);

        // get preliminary offer
        boolean isScored =
                given(TestBase.requestSpecification)
                        .auth().oauth2(accessToken)
                        .when()
                        .post(String.format("/mobile/v1/loans/%s/preliminary-offer", loanId))
                        .then()
                        .assertThat().statusCode(200).and().extract().path("scored");

        // wait for online scoring
        int maxTry = 120;

        while (!isScored && maxTry > 0) {
            isScored = given(TestBase.requestSpecification)
                    .auth().oauth2(accessToken)
                    .when()
                    .post(String.format("/mobile/v1/loans/%s/preliminary-offer", loanId))
                    .then()
                    .assertThat().statusCode(200).and().extract().path("scored");
            maxTry -= 1;
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // accept preliminary offer
        given(TestBase.requestSpecification)
                .auth().oauth2(accessToken)
                .when()
                .body("{\"text\" : \"Gimme money!\"}")
                .put(String.format("/mobile/v1/loans/%s/preliminary-offer/accept", loanId))
                .then()
                .assertThat().statusCode(200);


        // schvaleni v Adminu.. (pozor, test poptavka nejde schvalit)


        // sign final offer
        given(TestBase.requestSpecification)
                .auth().oauth2(accessToken)
                .when()
                .body("{\"preagreementConsent\" : true," +
                        "\"serviceTermsConsent\" : true }")
                .put(String.format("/mobile/v1/loans/%s/sign", loanId))
                .then()
                .assertThat().statusCode(400).and().body("error", is("AUTHORIZATION_SMS_REQUIRED"));

        given(TestBase.requestSpecification)
                .auth().oauth2(accessToken)
                .when()
                .header("X-Authorization-Code", TestBase.getSMSverificationCode(testUser))
                .body("{\"preagreementConsent\" : true," +
                        "\"serviceTermsConsent\" : true }")
                .put(String.format("/mobile/v1/loans/%s/sign", loanId))
                .then()
                .assertThat().statusCode(200).and().body("error", is("AUTHORIZATION_SMS_REQUIRED"));
    }

}
