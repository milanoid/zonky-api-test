import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class MarketPlaceTest extends TestBase {

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
