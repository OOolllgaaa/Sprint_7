package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import static io.restassured.RestAssured.given;
import static api.ScooterClient.BASE_URL;

public class CourierClient {
    @Step("Создать курьера")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логин курьера в системе")
    public Response login(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удалить курьера")
    public Response delete(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .when()
                .delete("/api/v1/courier/" + courierId);
    }
}
