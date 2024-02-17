import courier.Courier;
import courier.CourierOperations;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.BaseURI;

import static org.hamcrest.CoreMatchers.*;

public class LoginCourierTest {
    private static String login;
    private static String password;
    private static String firstName;


    Courier courier;
    String id;

    @Before
    public void setUp() {
        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(8);
        firstName = RandomStringUtils.randomAlphabetic(8);
    }

    @After
    public void cleanUp(){
        try {
            id = CourierOperations.signInCourier(courier).then().extract().path("id").toString();
            CourierOperations.deleteCourier(id);
        } catch (NullPointerException e) {
            System.out.println("Невозможно удалить несуществующего курьера");
        }
    }

    @Test
    @DisplayName("Авторизация с правильными данными")
    public void signInGetSuccessResponse() {
        courier = new Courier(login, password, firstName);
        CourierOperations.createCourier(courier);
        Response response = CourierOperations.signInCourier(courier);
        response.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация без логина")
    public void signInWithoutLoginGetError() {
        courier = new Courier(password, firstName);
        CourierOperations.createCourier(courier);
        CourierOperations.signInCourier(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    public void signInWithoutPasswordGetError() {
        courier = new Courier(login, firstName);
        CourierOperations.createCourier(courier);
        CourierOperations.signInCourier(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация несуществующего")
    public void signInNonExistentUserGetError() {
        courier = new Courier(login, password, firstName);
        CourierOperations.signInCourier(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неправильным паролем")
    public void signInWithIncorrectPasswordGetError() {
        courier = new Courier(login, password, firstName);
        CourierOperations.createCourier(courier);
        Courier incorrectCourier = new Courier(courier.getLogin(), RandomStringUtils.randomAlphabetic(10), courier.getFirstName());
        CourierOperations.signInCourier(incorrectCourier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неправильным логинoм")
    public void signInWithIncorrectLoginGetError() {
        courier = new Courier(login, password, firstName);
        CourierOperations.createCourier(courier);
        Courier incorrectCourier = new Courier(RandomStringUtils.randomAlphabetic(10), courier.getPassword(), courier.getFirstName());
        CourierOperations.signInCourier(incorrectCourier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
