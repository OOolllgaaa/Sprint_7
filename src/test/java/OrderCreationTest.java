import client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {

    private final List<String> color;
    private final OrderClient orderClient = new OrderClient();

    public OrderCreationTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Collection<Object[]> getData() {
        return Arrays.asList(new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @Test
    @DisplayName("Создание заказа с различными вариантами цветов")
    public void createOrderWithDifferentColors() {
        Order order = new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 14 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2026-06-06",
                "Sasukeee!",
                color
        );

        Response response = orderClient.create(order);
        response.then().assertThat().statusCode(201)
                .and().body("track", notNullValue());
    }
}