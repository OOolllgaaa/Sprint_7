import client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class CourierCreateTest {

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
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        } else {
            Response loginResponse = courierClient.login(courier);
            if (loginResponse.statusCode() == 200) {
                courierId = loginResponse.jsonPath().getInt("id");
                courierClient.delete(courierId);
            }
        }
    }

    @Test
    @DisplayName("Курьера можно успешно создать")
    public void courierCanBeCreated() {
        Response response = courierClient.create(courier);
        response.then().assertThat().statusCode(201).and().body("ok", is(true));

        Response loginResponse = courierClient.login(courier);
        courierId = loginResponse.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьера")
    public void cannotCreateTwoIdenticalCouriers() {
        courierClient.create(courier);
        Response responseLogin = courierClient.login(courier);
        courierId = responseLogin.jsonPath().getInt("id");

        Response secondResponse = courierClient.create(courier);
        secondResponse.then().assertThat().statusCode(409)
                .and().body("message", is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без обязательного поля login")
    public void cannotCreateCourierWithoutLogin() {
        Courier invalidCourier = new Courier(null, "password", "firstName");
        Response response = courierClient.create(invalidCourier);
        response.then().assertThat().statusCode(400)
                .and().body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без обязательного поля password")
    public void cannotCreateCourierWithoutPassword() {
        Courier invalidCourier = new Courier("login", null, "firstName");
        Response response = courierClient.create(invalidCourier);
        response.then().assertThat().statusCode(400)
                .and().body("message", is("Недостаточно данных для создания учетной записи"));
    }
}
