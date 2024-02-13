import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import utils.APIs;
import utils.BaseURI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;



public class GetOrderListTest {


    @Test
    @DisplayName("Получение спискa заказов")
    public void getOrderListTest() {
        given()
                .spec(BaseURI.requestSpecification())
                .header("Content-type", "application/json")
                .when()
                .get(APIs.ORDER_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("orders", hasSize(greaterThan(0)));;
    }
}