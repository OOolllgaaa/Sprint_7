import client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {

    @Test
    @DisplayName("Получение списка заказов возвращает непустой список")
    public void getOrdersListReturnsOrders() {
        OrderClient orderClient = new OrderClient();
        Response response = orderClient.getOrders();

        response.then().assertThat().statusCode(200)
                .and().body("orders", notNullValue());
    }
}