import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;

import static io.restassured.RestAssured.given;

public class CourierRequests {

    public static void init(){
        RestAssured.baseURI = SiteAddresses.SITE_ADDRESS;
    }

    public static Response createCourier(Courier courier){
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post(SiteAddresses.COURIER_CREATE);
    }

    public static Response loginCourier(Courier courier){
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post(SiteAddresses.COURIER_LOGIN);
    }

    public static Response deleteCourier(Courier courier){
        String URIaddress = SiteAddresses.COURIER_DELETE + courier.getId();
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .delete(URIaddress);
    }

    public static Response deleteCourier(int courierID){
        Courier courier = new Courier(courierID);
        return deleteCourier(courier);
    }

    public static Response initAndDelete(Courier courier){
        init();
        return deleteCourier(courier);
    }

    public static Response initAndDelete(int id){
        Courier courier = new Courier(id);
        return initAndDelete(courier);
    }

    public static Response initAndLogin(Courier courier){
        init();
        return loginCourier(courier);
    }

    public static int getCourierID(Courier courier){
        return initAndLogin(courier)
                .then()
                .extract()
                .path("id");
    }
}
