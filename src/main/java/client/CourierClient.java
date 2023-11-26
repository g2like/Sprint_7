package client;

import data.CourierCredentials;
import data.CourierData;
import io.restassured.response.ValidatableResponse;
import order.OrderDate;
import path.Path;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient implements Path {

    public ValidatableResponse createCourier(CourierData courier){
        return given()
                .spec(requestSpecification())
                .and()
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }
    public ValidatableResponse loginCourier(CourierCredentials courierCredentials){
        return given()
                .spec(requestSpecification())
                .and()
                .body(courierCredentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    public ValidatableResponse deleteCourier(int id){
        return given()
                .spec(requestSpecification())
                .and()
                .when()
                .delete(DELETE_PATH + id)
                .then();
    }

    public ValidatableResponse createOrder(OrderDate orderDate){
        return given()
                .spec(requestSpecification())
                .and()
                .body(orderDate)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    public ValidatableResponse acceptOrder(int orderId,int courierId){
        return given()
                .spec(requestSpecification())
                .and()
                .when()
                .put("api/v1/orders/accept/" + orderId + "?courierId="+ courierId)
                .then();
    }

    public ValidatableResponse getOrder(int track){
        return given()
                .spec(requestSpecification())
                .and()
                .when()
                .get(GET_ORDER_PATH + track)
                .then();
    }

    public ValidatableResponse getListOrder(int courierId){
        return given()
                .spec(requestSpecification())
                .and()
                .when()
                .get(GET_LIST_ORDER_PATH + courierId)
                .then();
    }
}
