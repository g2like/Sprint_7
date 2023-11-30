package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.CoreMatchers.is;

public class OrderSteps {

    @Step("Check status code")
    public static void checkStatusCode(ValidatableResponse response) {
        response.assertThat().statusCode(201);
    }

    @Step("Check body")
    public static void checkBody(ValidatableResponse response, int trackOrder) {
        response.body("track", is(trackOrder));
    }
}
