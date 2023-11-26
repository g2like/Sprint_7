import client.CourierClient;
import client.RestClient;
import data.CourierCredentials;
import data.CourierData;
import data.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import order.ordersCourier;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import path.Path;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;


public class GetListOfOrdersTest extends RestClient implements Path {
    CourierClient courierClient;
    CourierData courier;
    ValidatableResponse response;
    private int courierId;
    private int numberTrack;
    private int orderId;
    File orderJson;
    ordersCourier ordersCourier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        orderJson = new File("src/test/resources/order.json");
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandomCourier();
        response = courierClient.createCourier(courier);
    }

    @After
    public void deleteDate(){
        response = courierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Get list of orders and check body")
    @Description("Check body when get list of orders")
    public void GetListOfOrdersTest(){
        loginCourierAndGet();
        createOrderAndGetTrack();
        getOrder();
        acceptOrder();
        getListOrder();
        getDate();
        checkBody();
    }

    @Step("Login courier and get courier ID")
    public void loginCourierAndGet(){
        response = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = response.extract().body().path("id");
        System.out.println("CourierId: " + courierId);
    }

    @Step("Create order and get track number for next get order ID")
    public void createOrderAndGetTrack() {
        response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orderJson)
                        .when()
                        .post(ORDER_PATH)
                        .then();

        numberTrack = response.extract().body().path("track");
        System.out.println("Number track: " + numberTrack);
    }

    @Step("Get order and order ID")
    public void getOrder(){
        response = courierClient.getOrder(numberTrack);
        orderId = response.extract().body().path("order.id");
        System.out.println("orderID: " + orderId);
    }

    @Step("Accept order for get list of orders couriers")
    public void acceptOrder(){
        response = courierClient.acceptOrder(orderId,courierId);
    }

    @Step("Get list of orders")
    public void getListOrder(){
        response = courierClient.getListOrder(courierId);
    }

    @Step("Record json date to java object with deserialization")
    public void getDate(){
        ordersCourier = given()
                .spec(requestSpecification())
                .get(GET_LIST_ORDER_PATH + courierId)
                .body().as(ordersCourier.class);
    }

    @Step("Check body after deserialization")
    public void checkBody(){
        MatcherAssert.assertThat(ordersCourier,notNullValue());
    }

}
