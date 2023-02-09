import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Order;
import model.OrderResponseCarrier;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderRequests {

    public static void init(){
        RestAssured.baseURI = SiteAddresses.SITE_ADDRESS;
    }

    public static Response createOrder(Order order){
        return given()
                .header("Content-Type", "application/json")
                .body(order)
                .post(SiteAddresses.ORDER_CREATE);
    }

    public static List<Order> getAnyOrderFromOrders(){
        List<Order> list = given()
                .get(SiteAddresses.ORDER_LIST)
                .jsonPath().getList("orders", Order.class);

        return list;
    }

    public static Order getOrderByTrackNumber(int trackNumber){
        Response response = given()
                .header("Content-Type", "application/json")
                .queryParam("t", trackNumber)
                .get(SiteAddresses.ORDER_BY_NUMBER);

        //Вариант сериализации напрямую в объект по пути из Джейсона.
        //Order or = response.getBody().jsonPath().getObject("order", Order.class);

        //А вот этот не удается получить, сериализуется в ЛинкедХэшМап и всё...
        //Order orr = response.then().extract().path("order");

        return response.body().as(OrderResponseCarrier.class).getOrder();
    }
}
