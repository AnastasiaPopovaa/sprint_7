import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.OrderOperations;
import org.apache.http.HttpStatus;
import org.junit.Test;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;



public class GetOrderListTest {
    @Test
    @DisplayName("Получение спискa заказов")
    public void getOrderListTest() {
        Response response = OrderOperations.getOrderList();
                response.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("orders", hasSize(greaterThan(0)));
    }
}