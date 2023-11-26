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
    public void deleteCourier(){
        if(response.extract().statusCode() == 200){
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
    public void createCourierTest(){
        createCourier();
        checkStatusCodeWhenCreateCourier();
        checkBodyWhenCreateCourier();

    }

    @Test
    @DisplayName("Check create similar login and status code")
    @Description("login verification when creating courier")
    public void similarLoginTest(){
        response = courierClient.createCourier(courier);
        ValidatableResponse response1 = courierClient.createCourier(courier);
        response1.assertThat().statusCode(409)
                .body("message",is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Check field when creating courier")
    @Description("create courier without data")
    public void notHaveOneFieldTest(){
        changePasswordWhenCreateCourier();
        createCourier();
        checkStatusCodeWhenNotPassword();
        checkBodyWhenNotPassword();
    }

    @Step("Create courier")
    public ValidatableResponse createCourier(){
        response = courierClient.createCourier(courier);
        return response;
    }

    @Step("When courier created")
    public void checkStatusCodeWhenCreateCourier(){
        response.assertThat().statusCode(201);
    }

    @Step("Check body when courier created")
    public void checkBodyWhenCreateCourier(){
        response.assertThat().body("ok",is(true));
    }

    @Step("Crate when password equal null")
    public void changePasswordWhenCreateCourier(){
        courier.setPassword("");
    }

    @Step("Check status code when not have password")
    public void checkStatusCodeWhenNotPassword(){
        response.assertThat().statusCode(400);
    }

    @Step("Check body when not have password")
    public void checkBodyWhenNotPassword(){
        response.assertThat().body("message",is("Недостаточно данных для создания учетной записи"));
    }

}
