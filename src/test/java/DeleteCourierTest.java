import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.Courier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class DeleteCourierTest {

    Courier courier;

    @Before
    public void setUp() throws Exception {
        CourierRequests.init();
        courier = new Courier("Gotharcherdelete", "12345", "Deleteme");
    }

    @Test
    public void createCourierAndDelete(){
        CourierRequests.createCourier(courier);
        int courierID = CourierRequests.getCourierID(courier);
        Response deleteResponse = CourierRequests.deleteCourier(courierID);
        Assert.assertEquals("Получено сообщение удаления", true, deleteResponse.then().extract().path("ok"));
    }

    @Test
    @Description("No ID sent at all")
    public void noIDErrorCheck(){
        String URIaddress = SiteAddresses.COURIER_DELETE;
        Response deleteResponse = given()
                .header("Content-Type", "application/json")
                .body("")
                .delete(URIaddress);
        Assert.assertEquals("Вернулся код ошибки", 400, deleteResponse.statusCode());
    }

    @Test
    @Description("ID in requirements mentioned as String, check letters-type ID")
    public void wrongIDErrorCheck(){
        String id = "notAnID";
        String URIaddress = SiteAddresses.COURIER_DELETE+id;
        Response deleteResponse = given()
                .header("Content-Type", "application/json")
                .body("")
                .delete(URIaddress);
        Assert.assertEquals("Вернулся код ошибки", 404, deleteResponse.statusCode());
    }

    @Test
    @Description("As ID has to be integer, lets check MaxInt, suppose no such ID ever")
    public void noSuchIDErrorCheck(){
        int id = Integer.MAX_VALUE;
        String URIaddress = SiteAddresses.COURIER_DELETE+id;
        Response deleteResponse = given()
                .header("Content-Type", "application/json")
                .body("")
                .delete(URIaddress);
        Assert.assertEquals("Вернулся код ошибки", 404, deleteResponse.statusCode());
    }
}
