import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreateCourierTest {
    String login = "Gotharchertestuser";
    String password = "12345";
    String firstName = "Arsenytest";
    Courier courier;

    @Before
    public void setUp() {
        CourierRequests.init();
        courier = new Courier(login, password, firstName);
    }

    @Test
    public void createNewCourier(){
        Response response = createCourier(courier);
        checkResponseBody(response);
        int courierID = checkCourierCreated(courier);
        CourierRequests.deleteCourier(courierID);
    }

    @Test
    @Description("Create courier, if it succeed, create same courier once more and expect bad code.")
    public void createSameLoginCourier(){
        Response response = createCourier(courier);
        if(response.then().extract().statusCode() == 201){
            Response secondResponse = CourierRequests.createCourier(courier);
            checkSameLoginCreatedError(secondResponse);
            int courierID = checkCourierCreated(courier);
            CourierRequests.deleteCourier(courierID);
        }
    }

    @Test
    public void createWithoutPassword(){
        Courier badCourier = new Courier(login, "", firstName);
        Response response = CourierRequests.createCourier(badCourier);
        Assert.assertEquals("Получили код ошибки", 400, response.then().extract().statusCode());
    }

    @Test
    public void createWithoutLogin(){
        Courier badCourier = new Courier("", password, firstName);
        Response response = CourierRequests.createCourier(badCourier);
        Assert.assertEquals("Получили код ошибки", 400, response.then().extract().statusCode());
    }

    //В документации поле не указано, как "небязательное". Но поведение ошибки не описано.
    //Наставник сказал, этот пункт ревью пропустить.
    @Test
    public void createWithoutFirstName(){
        Courier badCourier = new Courier(login, password, "");
        Response response = CourierRequests.createCourier(badCourier);
        Assert.assertEquals("Получили код ошибки", 400, response.then().extract().statusCode());
    }

    @Step("Check for response if same courier tried to be created")
    public void checkSameLoginCreatedError(Response response){
        Assert.assertEquals("Получили код ошибки 409", 409, response.then().extract().statusCode());
    }

    @Step("Sending data to create new Courier")
    public Response createCourier(Courier courier){
        Response response = CourierRequests.createCourier(courier);
        Assert.assertEquals("Вернулся 201, курьер создан", 201, response.then().extract().statusCode());
        return response;
    }

    @Step("Check for OK in response body")
    public void checkResponseBody(Response response){
        Assert.assertTrue("В теле вернулась Истина", response.then().extract().path("ok"));
    }

    @Step("We can have ID - so courier created.")
    public int checkCourierCreated(Courier courier){
        int courierID = CourierRequests.getCourierID(courier);
        Assert.assertNotEquals("Курьер создался - у него есть ID.",0, courierID);
        return courierID;
    }

    @After
    public void tearDown() {
        Response loginResponse = CourierRequests.loginCourier(courier);
        if(loginResponse.then().extract().statusCode() == 200){
            CourierRequests.deleteCourier((int) loginResponse.then().extract().path("id"));
        }
    }
}
