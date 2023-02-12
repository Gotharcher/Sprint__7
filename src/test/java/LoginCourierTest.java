import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//Не получается красиво параметризированный тест на все варианты логина написать
public class LoginCourierTest {

    private Courier courier;

    @Before
    public void setUp(){
        CourierRequests.init();
        courier = Courier.createRandomCourier();
        CourierRequests.createCourier(courier);
    }

    @Test
    public void checkSuccessfulLogin(){
        Response response = CourierRequests.loginCourier(courier);
        response.then().statusCode(200);
        checkIDISPresentInAnswer(response);
    }

    @Test
    public void checkWrongPasswordLogin(){
        String oldPassword = courier.getPassword();
        courier.setPassword("00000");
        statusCodeAndMessageCorrect(404, "Учетная запись не найдена");
        courier.setPassword(oldPassword);
    }

    @Test
    public void checkEmptyLoginLogin(){
        String oldLogin = courier.getLogin();
        courier.setLogin("");
        statusCodeAndMessageCorrect(400, "Недостаточно данных для входа");
        courier.setLogin(oldLogin);
    }

    @Test
    public void checkEmptyPasswordLogin(){
        String oldPassword = courier.getPassword();
        courier.setPassword("");
        statusCodeAndMessageCorrect(400, "Недостаточно данных для входа");
        courier.setPassword(oldPassword);
    }

    @Step("Check error code and message with broken credentials")
    public void statusCodeAndMessageCorrect(int expectedCode, String expectedMessage){
        Response response = CourierRequests.loginCourier(courier);
        response.then().statusCode(expectedCode);
        Assert.assertEquals("Текст в ответе совпадает с указанным", expectedMessage, response.path("message"));

    }

    @Step("If login successful, ID is returned in answer")
    public void checkIDISPresentInAnswer(Response response){
        int answer = response.then().extract().path("id");
        Assert.assertNotEquals("Какой-то ID должен вернуться, если курьер залогинен", 0, answer);
    }

    @After
    public void tearDown(){
        CourierRequests.deleteCourier(CourierRequests.getCourierID(courier));
    }
}