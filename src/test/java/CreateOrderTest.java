import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    @Before
    public void setUp(){
        OrderRequests.init();
    }

    private List<String> colors;

    public CreateOrderTest(List<String> colors){
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] creationData() {
        return new Object[][] {
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("GREY", "BLACK")},
                {List.of()},
        };
    }

    @Test
    @Step("Place an order and check HTTP-code")
    public void placeOrder(){
        Order order = Order.fakeOrderWithColors(colors);
        Response response = OrderRequests.createOrder(order);
        Assert.assertEquals("Получили код подтверждения", 201, response.then().extract().statusCode());
        checkTrackNumber(response);
    }

    @Step("Check track number for succeed order")
    public void checkTrackNumber(Response response){
        int trackNumber = response.then().extract().path("track");
        Assert.assertNotEquals("Получен реальный трек-номер", 0, trackNumber);
    }
}
