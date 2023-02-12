import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class LoginCourierTest {
    private final String login, password, expectedMessage;
    private final int expectedAnswerCode;

    @Before
    public void setUp(){

    }


    public LoginCourierTest(String login, String password, String expectedMessage, int expectedAnswerCode) {
        this.login = login;
        this.password = password;
        this.expectedMessage = expectedMessage;
        this.expectedAnswerCode = expectedAnswerCode;
    }

    @Parameterized.Parameters
    public static Object[][] creationData() {
        //Этот курьер создан вручную через постман.
        return new Object[][] {
                {"Gotharcher", "12345", "", 200},
                {"Gotharcher", "00000", "Учетная запись не найдена", 404},
                {"Gotharcher", "", "Недостаточно данных для входа", 400},
                {"", "12345", "Недостаточно данных для входа", 400}
        };
    }

    @Test
    public void loginVarianceTest(){
        Courier courier = new Courier(this.login, this.password);
        Response response = CourierRequests.initAndLogin(courier);
        int responseCode = checkAnswerCode(response);
        if(responseCode == 400 || responseCode == 404) {
            checkAnswerCodeMessage(response);
        } else if (responseCode == 200) {
            checkIDISPresentInAnswer(response);
        }
    }

    @Step("Check answer code in acceptable range")
    public int checkAnswerCode(Response response){
        int responseCode = response.then().extract().statusCode();
        Assert.assertEquals("Получен ожидаемый код ответа", expectedAnswerCode, responseCode);
        return responseCode;
    }

    @Step("If answer code accepted but not OK, check for message")
    public void checkAnswerCodeMessage(Response response){
        response
                .then()
                .assertThat()
                .body("message", equalTo(expectedMessage));
    }

    @Step("If login successful, ID is returned in answer")
    public void checkIDISPresentInAnswer(Response response){
        int answer = response.then().extract().path("id");
        Assert.assertNotEquals("Какой-то ID вернулся", 0, answer);
    }
}
