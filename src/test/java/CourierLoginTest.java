import client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;

public class CourierLoginTest {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        String login = RandomStringUtils.randomAlphanumeric(10);
        String password = RandomStringUtils.randomAlphanumeric(10);
        String firstName = RandomStringUtils.randomAlphanumeric(10);
        courier = new Courier(login, password, firstName);

        courierClient.create(courier);
        Response loginResponse = courierClient.login(courier);
        courierId = loginResponse.jsonPath().getInt("id");
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Курьер может успешно авторизоваться")
    public void courierCanLogin() {
        Response response = courierClient.login(courier);
        response.then().assertThat().statusCode(200).and().body("id", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при авторизации с неверным паролем")
    public void loginWithWrongPasswordReturnsError() {
        Courier invalidCourier = new Courier(courier.getLogin(), "wrongPassword", null);
        Response response = courierClient.login(invalidCourier);
        response.then().assertThat().statusCode(404)
                .and().body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при авторизации под несуществующим пользователем")
    public void loginNonExistentUserReturnsError() {
        Courier invalidCourier = new Courier("nonExistentLogin123", "password", null);
        Response response = courierClient.login(invalidCourier);
        response.then().assertThat().statusCode(404)
                .and().body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при попытке логина без обязательного поля login")
    public void loginWithoutLoginReturnsError() {
        Courier invalidCourier = new Courier(null, courier.getPassword(), null);
        Response response = courierClient.login(invalidCourier);
        response.then().assertThat().statusCode(400)
                .and().body("message", is("Недостаточно данных для входа")); // Исправили текст ошибки
    }

    @Test
    @DisplayName("Ошибка при попытке логина без обязательного поля password")
    public void loginWithoutPasswordReturnsError() {
        Courier invalidCourier = new Courier(courier.getLogin(), null, null);
        Response response = courierClient.login(invalidCourier);

        response.then().assertThat()
                .statusCode(400)
                .and().body("message", is("Недостаточно данных для входа"));
    }
}