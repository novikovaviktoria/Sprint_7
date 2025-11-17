package prakticum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class StepsOrder {
    public static final String ORDER = "/api/v1/orders";
    private final static String CANCEL_ORDER = "/api/v1/orders/cancel";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .body(order)
                .when()
                .post(ORDER)
                .then();
    }

    @Step("Получение списка заказа")
    public ValidatableResponse getListOfOrders() {
        return given()
                .get(ORDER)
                .then();
    }

    @Step("Закрытие заказа")
    public ValidatableResponse cancelOrder(Order order) {
        return given()
                .body(order)
                .when()
                .put(CANCEL_ORDER)
                .then();
    }
}
