import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.OrderDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class CreateOrdersTest {
    CourierClient courierClient;
    OrderDate orderDate;
    private String[] testArray;
    private int trackOrder;
    ValidatableResponse response;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;

    public CreateOrdersTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Before
    public void setup() {
        courierClient = new CourierClient();
    }

    @Parameterized.Parameters
    public static Object[][] dateOrder() {
        return new Object[][]{{"Sergey","Tolkachev","EKB","2","8922222222",2,"2023","Serg",List.of("BLACK")},
                {"Sergey","Tolkachev","EKB","2","8922222222",3,"2024","Serg",List.of("BLACK","GREY")},
                {"Sergey","Tolkachev","EKB","2","8922222222",4,"2025","Serg",List.of("")}};
    }


        @Test
        @DisplayName("Check create orders when different data and check status code")
        @Description("Create different orders")
        public void createOrderTest () {
            convertListToArrayString();
            createDataOrder();
            createOrder();
            getTrackNumber();
            checkStatusCode();
            checkBody();
        }


        @Step("Convert list to string array")
        public void convertListToArrayString(){
            testArray = color.toArray(new String[0]);
        }

        @Step("Create data order")
        public void createDataOrder(){
            orderDate = new OrderDate(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,testArray);
        }

        @Step("Create order")
        public ValidatableResponse createOrder(){
            response = courierClient.createOrder(orderDate);
            return  response;
        }

        @Step("get track number for compare with body")
        public void getTrackNumber(){
            trackOrder = response.extract().body().path("track");
        }

        @Step("Check status code")
        public void checkStatusCode(){
            response.assertThat().statusCode(201);
        }

        @Step("Check body")
        public void checkBody(){
            response.body("track",is(trackOrder));
        }
}

