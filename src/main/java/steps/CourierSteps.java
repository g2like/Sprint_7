package steps;

import data.CourierData;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.CoreMatchers.is;

public class CourierSteps {


    @Step("When courier created")
    public static void checkStatusCodeWhenCreateCourier(ValidatableResponse response) {
        response.assertThat().statusCode(201);
    }

    @Step("Check body when courier created")
    public static void checkBodyWhenCreateCourier(ValidatableResponse response) {
        response.assertThat().body("ok", is(true));
    }

    @Step("Crate when password equal null")
    public static void changePasswordWhenCreateCourier(CourierData courier) {
        courier.setPassword("");
    }

    @Step("Check status code when not have password")
    public static void checkStatusCodeWhenNotPassword(ValidatableResponse response) {
        response.assertThat().statusCode(400);
    }

    @Step("Check body when not have password")
    public static void checkBodyWhenNotPassword(ValidatableResponse response) {
        response.assertThat().body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Step("Check status code when created same login")
    public static void checkStatusCodeWhenCreateSameLogin(ValidatableResponse response) {
        response.assertThat().statusCode(409);
    }

    @Step("Check body when created same login")
    public static void checkBodyWhenCreateSameLogin(ValidatableResponse response) {
        response.assertThat().body("message", is("Этот логин уже используется. Попробуйте другой."));
    }


}
