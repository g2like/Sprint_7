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
import steps.OrderSteps;
import java.util.List;


@RunWith(Parameterized.class)
public class CreateOrdersTest {
    CourierClient courierClient;
    OrderDate orderDate;
    protected String[] colorArray;
    public int trackOrder;
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

    public CreateOrdersTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
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
        return new Object[][]{{"Sergey", "Tolkachev", "EKB", "2", "8922222222", 2, "2023", "Serg", List.of("BLACK")},
                {"Sergey", "Tolkachev", "EKB", "2", "8922222222", 3, "2024", "Serg", List.of("BLACK", "GREY")},
                {"Sergey", "Tolkachev", "EKB", "2", "8922222222", 4, "2025", "Serg", List.of("")}};
    }


    @Test
    @DisplayName("Check create orders when different data and check status code")
    @Description("Create different orders")
    public void createOrderTest() {
        convertListToArrayString();
        createDataOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colorArray);
        response = courierClient.createOrder(orderDate);
        getTrackNumber();
        OrderSteps.checkStatusCode(response);
        OrderSteps.checkBody(response, trackOrder);
    }

    @Step("Convert list to string array")
    public void convertListToArrayString() {
        colorArray = color.toArray(new String[0]);
    }

    @Step("Create data order")
    public void createDataOrder(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] colorArray) {
        orderDate = new OrderDate(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colorArray);
    }

    @Step("get track number for compare with body")
    public void getTrackNumber() {
        trackOrder = response.extract().body().path("track");
    }

}

