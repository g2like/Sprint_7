import client.CourierClient;
import data.CourierCredentials;
import data.CourierData;
import data.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.LoginSteps;

import static org.hamcrest.CoreMatchers.is;

public class LoginCourierTest {
    CourierClient courierClient;
    CourierData courier;
    private int courierId;
    ValidatableResponse response;
    CourierCredentials courierCredentialsTest;


    @Before
    public void setup() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);
    }

    @After
    public void deleteCourier() {
        if (response.extract().statusCode() == 200) {
            response = courierClient.deleteCourier(courierId);
            response.assertThat()
                    .statusCode(200);
        }
    }


    @Test
    @DisplayName("Check login courier and status code")
    @Description("Check logging courier")

    public void loginCourierTest() {
        authorizationCourier();
        getCourierId();
        LoginSteps.checkStatusCode(response);
        checkBodyWhenAuthorization();
    }

    @Test
    @DisplayName("Check login who missing in database")
    @Description("Check invalid login and status code")
    public void notHaveUserInDataBaseTest() {
        authorizationWithInvalidDate();
        LoginSteps.checkStatusCodeWhenInvalidData(response);
        LoginSteps.checkBodyWhenInvalidDate(response);
    }

    @Test
    @DisplayName("Check login courier with not full date")
    @Description("Check logging without data")
    public void notEnoughDateForInputTest() {
        LoginSteps.changePasswordForLogin(courier);
        authorizationCourier();
        LoginSteps.checkStatusWhenNotEnoughDateForInput(response);
        LoginSteps.checkBodyWhenNotEnoughDateForInput(response);
    }


    @Step("check authorization login courier")
    public ValidatableResponse authorizationCourier() {
        response = courierClient.loginCourier(CourierCredentials.from(courier));
        return response;
    }

    @Step("Get courier id for check body")
    public void getCourierId() {
        courierId = response.extract().body().path("id");
    }


    @Step
    public void checkBodyWhenAuthorization() {
        response.body("id", is(courierId));
    }

    @Step("Check login who missing in database")
    public void authorizationWithInvalidDate() {
        courierCredentialsTest = new CourierCredentials("Vigman", "dasdasd");
        response = courierClient.loginCourier(courierCredentialsTest);
    }
}
