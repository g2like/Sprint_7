package steps;

import data.CourierData;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.CoreMatchers.is;

public class LoginSteps {


    @Step("Check status code")
    public static void checkStatusCode(ValidatableResponse response) {
        response.assertThat().statusCode(200);
    }


    @Step("Check status code when authorization with invalid date")
    public static void checkStatusCodeWhenInvalidData(ValidatableResponse response) {
        response.assertThat().statusCode(404);
    }

    @Step("Check body when authorization with invalid date")
    public static void checkBodyWhenInvalidDate(ValidatableResponse response) {
        response.body("message", is("Учетная запись не найдена"));
    }

    @Step("Change password")
    public static void changePasswordForLogin(CourierData courier) {
        courier.setPassword("");
    }

    @Step("Check status code when not enough data")
    public static void checkStatusWhenNotEnoughDateForInput(ValidatableResponse response) {
        response.assertThat().statusCode(400);
    }

    @Step("Check body when not enough data")
    public static void checkBodyWhenNotEnoughDateForInput(ValidatableResponse response) {
        response.body("message", is("Недостаточно данных для входа"));
    }
}
