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

import static org.hamcrest.CoreMatchers.equalTo;


public class CourierTest  {
    private static String login;
    private static String password;
    private static String firstName;


    Courier courier;
    int id;

    @Before
    public void setUp() {
        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(8);
        firstName = RandomStringUtils.randomAlphabetic(8);
    }

    @After
    public void cleanUp(){
        if(id != 0) {
            CourierOperations.deleteCourier(id);
        }
    }

    @Test
    @DisplayName("Слздание курьера используя корректные данные")
    public void createNewCourierGetSuccessResponse() {
        courier = new Courier(login, password, firstName);
        Response response = CourierOperations.createCourier(courier);
        //id нужен для удаления курьера
        id = CourierOperations.signInCourier(courier).then().extract().jsonPath().getInt("id");
        response.then().assertThat().statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierWithoutLoginGetError() {
        courier = new Courier(password, firstName);
        CourierOperations.createCourier(courier)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierWithoutPasswordGetError() {
        courier = new Courier(login, firstName);
        CourierOperations.createCourier(courier)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Создание курьера без имени")
    public void createCourierWithoutFirstNameGetError() {
        courier = new Courier(login, password);
        CourierOperations.createCourier(courier)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    //Тут баг - комментарий не соответствует документаци
     @Test
    @DisplayName("Создание существующего курьера")
    public void createTwoSimilarCourierGetError() {
        courier = new Courier(login, password, firstName);
        CourierOperations.createCourier(courier);
        CourierOperations.createCourier(courier)
                .then().assertThat().statusCode(HttpStatus.SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
    } }

