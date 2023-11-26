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
    public void deleteCourier(){
        if(response.extract().statusCode() == 200){
            response = courierClient.deleteCourier(courierId);
            response.assertThat()
                    .statusCode(200);
        }
    }


    @Test
    @DisplayName("Check login courier and status code")
    @Description("Check logging courier")

    public void loginCourierTest(){
        authorizationCourier();
        getCourierId();
        checkStatusCode();
        checkBodyWhenAuthorization();
    }

    @Test
    @DisplayName("Check login who missing in database")
    @Description("Check invalid login and status code")
    public void notHaveUserInDataBaseTest(){
        authorizationWithInvalidDate();
        checkStatusCodeWhenInvalidData();
        checkBodyWhenInvalidDate();
    }

    @Test
    @DisplayName("Check login courier with not full date")
    @Description("Check logging without data")
    public void notEnoughDateForInputTest(){
        changePasswordForLogin();
        authorizationCourier();
        checkStatusWhenNotEnoughDateForInput();
        checkBodyWhenNotEnoughDateForInput();
    }


    @Step("check authorization login courier")
    public ValidatableResponse authorizationCourier(){
        response = courierClient.loginCourier(CourierCredentials.from(courier));
                return response;
    }

    @Step("Get courier id for check body")
    public void getCourierId(){
        courierId = response.extract().body().path("id");
    }

    @Step("Check status code")
    public void checkStatusCode(){
        response.assertThat().statusCode(200);
    }

    @Step
    public void checkBodyWhenAuthorization(){
        response.body("id",is(courierId));
    }

    @Step("Check login who missing in database")
    public void authorizationWithInvalidDate(){
        courierCredentialsTest = new CourierCredentials("Vigman","dasdasd");
        response = courierClient.loginCourier(courierCredentialsTest);
    }

    @Step("Check status code when authorization with invalid date")
    public void checkStatusCodeWhenInvalidData(){
        response.assertThat().statusCode(404);
    }

    @Step("Check body when authorization with invalid date")
    public void checkBodyWhenInvalidDate(){
        response.body("message",is("Учетная запись не найдена"));
    }

    @Step("Change password")
    public void changePasswordForLogin(){
        courier.setPassword("");
    }

    @Step("Check status code when not enough data")
    public void checkStatusWhenNotEnoughDateForInput(){
        response.assertThat().statusCode(400);
    }

    @Step("Check body when not enough data")
    public void checkBodyWhenNotEnoughDateForInput(){
        response.body("message",is("Недостаточно данных для входа"));
    }

}
