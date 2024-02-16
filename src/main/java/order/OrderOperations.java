package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.APIs;
import utils.BaseURI;

import static io.restassured.RestAssured.given;

public class OrderOperations {
    @Step("Create an order")
    public static Response createOrder(Order order) {
        Response response = given()
                .spec(BaseURI.requestSpecification())
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(APIs.ORDER_PATH);
        return response;
    }

    @Step("Cancel an order")
    public static void cancelOrder(String track) {
        if (track != null)
            given()
                    .spec(BaseURI.requestSpecification())
                    .header("Content-type", "application/json")
                    .and()
                    .body(track)
                    .when()
                    .delete(APIs.CANCEL_ORDER_PATH + track);
    }

    @Step("Get order List")
    public static Response getOrderList(){
        Response response = given()
                .spec(BaseURI.requestSpecification())
                .header("Content-type", "application/json")
                .when()
                .get(APIs.ORDER_PATH);
        return response;
    }

}

//
