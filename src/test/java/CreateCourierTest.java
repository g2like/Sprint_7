import client.CourierClient;
import data.CourierCredentials;
import data.CourierData;
import data.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

public class CreateCourierTest {
    CourierClient courierClient;
    CourierData courier;
    private int courierId;
    ValidatableResponse response;


    @Before
    public void setup() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandomCourier();
    }

    @After
    public void deleteCourier() {
        if (response.extract().statusCode() == 200) {
            response = courierClient.loginCourier(CourierCredentials.from(courier));
            courierId = response.extract().body().path("id");
            response = courierClient.deleteCourier(courierId);
            response.assertThat()
                    .statusCode(200);
        }
    }

    @Test
    @DisplayName("Check create courier and status code")
    @Description("Create courier")
    public void createCourierTest() {
        response = courierClient.createCourier(courier);
        CourierSteps.checkStatusCodeWhenCreateCourier(response);
        CourierSteps.checkBodyWhenCreateCourier(response);

    }

    @Test
    @DisplayName("Check create similar login and status code")
    @Description("login verification when creating courier")
    public void similarLoginTest() {
        response = courierClient.createCourier(courier);
        response = courierClient.createCourier(courier);
        CourierSteps.checkStatusCodeWhenCreateSameLogin(response);
        CourierSteps.checkBodyWhenCreateSameLogin(response);
    }

    @Test
    @DisplayName("Check field when creating courier")
    @Description("create courier without data")
    public void notHaveOneFieldTest() {
        CourierSteps.changePasswordWhenCreateCourier(courier);
        response = courierClient.createCourier(courier);
        CourierSteps.checkStatusCodeWhenNotPassword(response);
        CourierSteps.checkBodyWhenNotPassword(response);
    }


}
