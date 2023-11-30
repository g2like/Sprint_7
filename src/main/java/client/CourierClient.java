package client;

import data.CourierCredentials;
import data.CourierData;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import order.OrderDate;
import path.Path;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient implements Path {

    @Step("Create courier")
    public ValidatableResponse createCourier(CourierData courier) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Login courier")
    public ValidatableResponse loginCourier(CourierCredentials courierCredentials) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(courierCredentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Delete courier")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .spec(requestSpecification())
                .and()
                .when()
                .delete(DELETE_PATH + id)
                .then();
    }

    @Step("Create order")
    public ValidatableResponse createOrder(OrderDate orderDate) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(orderDate)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Accept order")
    public ValidatableResponse acceptOrder(int orderId, int courierId) {
        return given()
                .spec(requestSpecification())
                .and()
                .when()
                .put("api/v1/orders/accept/" + orderId + "?courierId=" + courierId)
                .then();
    }

    @Step("Get order")
    public ValidatableResponse getOrder(int track) {
        return given()
                .spec(requestSpecification())
                .and()
                .when()
                .get(GET_ORDER_PATH + track)
                .then();
    }

    @Step("Get list of orders")
    public ValidatableResponse getListOrder(int courierId) {
        return given()
                .spec(requestSpecification())
                .and()
                .when()
                .get(GET_LIST_ORDER_PATH + courierId)
                .then();
    }
}
