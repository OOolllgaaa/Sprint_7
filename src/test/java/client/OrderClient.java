package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;
import static io.restassured.RestAssured.given;
import static api.ScooterClient.BASE_URL;

public class OrderClient {

    @Step("Создать заказ")
    public Response create(Order order) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Получить список заказов")
    public Response getOrders() {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .when()
                .get("/api/v1/orders");
    }
}
