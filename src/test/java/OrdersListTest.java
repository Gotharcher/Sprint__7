import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class OrdersListTest {

    @Test
    public void checkListOfOrdersReturns() {
       RestAssured.baseURI = SiteAddresses.SITE_ADDRESS;
        Assert.assertTrue("Вернулся список заказов.",
                given()
               .get(SiteAddresses.ORDER_LIST)
               .then()
               .extract()
               .path("orders")
               .getClass()
               .equals(ArrayList.class));
    }
}
