import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import org.junit.Assert;
import org.junit.Test;

//@RunWith(Parameterized.class)
public class CreateCourierParams {

    private final String login, password, firstName, expectedMessage;
    private final int expectedAnswerCode;
    private final boolean deleteAfter;

    private int createdID;

    public CreateCourierParams(String login, String password, String firstName, String expectedMessage, int expectedAnswerCode, boolean deleteAfter) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.expectedMessage = expectedMessage;
        this.expectedAnswerCode = expectedAnswerCode;
        this.deleteAfter = deleteAfter;
    }


    //@Parameterized.Parameters
    public static Object[][] creationData() {
        String password = "12345";
        String username = "Gotharchertestuser";
         //Стоит ли генерировать рандомный 15-символьник?
        //предполагается, что тест по завершению удалит и пользователя не станет.
        String firstName = "Arsenytest";
        return new Object[][] {
                {username, password, firstName, "", 201, false}, //Created
                {username, password, firstName, "Этот логин уже используется", 409, true}, //created again!
                {username, password, "", "Недостаточно данных для создания учетной записи", 400, true},
                {username, "", firstName, "Недостаточно данных для создания учетной записи", 400, true},
                {"", password, firstName, "Недостаточно данных для создания учетной записи", 400, true},
        };

    }

    @Test
    public void createNewCourier(){
        String username = "Gotharchertestuser";
        String password = "12345";
        String firstName = "Arsenytest";

        Courier courier = new Courier(username, password, firstName);
        Response response = createCourier(courier);
        checkResponseBody(response);
        int courierID = checkCourierCreated(courier);
        CourierRequests.initAndDelete(courierID);
    }

    @Step("Sending data to create new Courier")
    public Response createCourier(Courier courier){
        Response response = CourierRequests.createCourier(courier);
        Assert.assertEquals("Вернулся Двухсотый, курьер создан", 200, response.then().extract().statusCode());
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
}
