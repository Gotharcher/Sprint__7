import model.Courier;
import model.Order;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class AcceptOrderTest {

    int courierID, orderID;

    @Before
    public void setUp() throws Exception {
        OrderRequests.init();
        Courier courier = Courier.createRandomCourier();
        CourierRequests.createCourier(courier);
        courierID = CourierRequests.loginCourier(courier).path("id");
        Order order = Order.fakeOrderWithColors(List.of());
        int orderTrack = OrderRequests.createOrder(order).then().extract().path("track");
        orderID = OrderRequests.getOrderByTrackNumber(orderTrack).getId();

    }

    @Test
    public void checkSuccessfulOrderAccept() {
        int statusCode = given()
                .header("Content-Type", "application/json")
                .queryParam("courierId", courierID)
                .put(SiteAddresses.ORDER_ACCEPT + orderID).statusCode();
        Assert.assertEquals("Получили код успешного ответа", 200, statusCode);
    }

    @Test
    public void checkNoCourierIDAccept() {
        int statusCode = given()
                .header("Content-Type", "application/json")
                //.queryParam("courierId", courierID)
                .put(SiteAddresses.ORDER_ACCEPT + orderID).statusCode();
        Assert.assertEquals("Получили код ошибки", 400, statusCode);
    }

    @Test
    public void checkWrongCourierIDAccept() {
        int statusCode = given()
                .header("Content-Type", "application/json")
                .queryParam("courierId", Integer.MAX_VALUE)
                .put(SiteAddresses.ORDER_ACCEPT + orderID).statusCode();
        Assert.assertEquals("Получили код ошибки", 404, statusCode);
    }

    @Test
    public void checkNoOrderIDAccept() {
        int statusCode = given()
                .header("Content-Type", "application/json")
                .queryParam("courierId", courierID)
                .put(SiteAddresses.ORDER_ACCEPT).statusCode();
        Assert.assertEquals("Получили код ошибки", 400, statusCode);
    }

    @Test
    public void checkWrongOrderIDAccept() {
        int statusCode = given()
                .header("Content-Type", "application/json")
                .queryParam("courierId", courierID)
                .put(SiteAddresses.ORDER_ACCEPT+Integer.MAX_VALUE).statusCode();
        Assert.assertEquals("Получили код ошибки", 404, statusCode);
    }

    @After
    public void tearDown() throws Exception {
        CourierRequests.deleteCourier(courierID);
    }
}
