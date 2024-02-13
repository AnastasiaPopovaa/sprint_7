package courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.APIs;
import utils.BaseURI;

import static io.restassured.RestAssured.given;

public class CourierOperations {

    @Step("Создание курьера")
    public static Response createCourier(Courier courier) {
        Response response = given()
                .spec(BaseURI.requestSpecification())
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(APIs.COURIER_PATH);
        return response;
    }

    @Step("Авторизация курьера")
    public static Response signInCourier(Courier courier) {
        Response response = given()
                .spec(BaseURI.requestSpecification())
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(APIs.LOGIN_PATH);
        return response;
    }

    @Step("Удаление курьера")
    public static void deleteCourier(int id) {
        if (id != 0)
            given()
                    .spec(BaseURI.requestSpecification())
                    .header("Content-type", "application/json")
                    .and()
                    .body(id)
                    .when()
                    .delete(APIs.COURIER_PATH + id);
    }
}
//
