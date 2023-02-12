import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class GetOrderByNumberTest {
    @Before
    public void setUp(){
        OrderRequests.init();
    }

    @Test
    public void getOrderByNumber(){
        int trackNumber = getAnyOrderFromOrders();
        Order order = OrderRequests.getOrderByTrackNumber(trackNumber);
        Assert.assertNotNull(order);
    }

    @Step("Get random Order from list")
    public int getAnyOrderFromOrders(){
        List<Order> list = OrderRequests.getAnyOrderFromOrders();

        int randOrderIndex = new Random().nextInt(list.size());
        return list.get(randOrderIndex).getTrack();
    }

    @Test
    public void getOrderWithoutNumber(){
        Response response = given()
                .header("Content-Type", "application/json")
                .get(SiteAddresses.ORDER_BY_NUMBER);
        Assert.assertEquals("Получили код ошибки", 400, response.statusCode());
    }

    @Test
    public void getOrderWithUnexpectedNumber(){
        Response response = given()
                .header("Content-Type", "application/json")
                .queryParam("t", Integer.MAX_VALUE)
                .get(SiteAddresses.ORDER_BY_NUMBER);
        Assert.assertEquals("Получили код ошибки", 404, response.statusCode());
    }
}