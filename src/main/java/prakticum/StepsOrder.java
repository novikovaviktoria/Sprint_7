package prakticum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class StepsOrder {
    public static final String ORDER = "/api/v1/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .body(order)
                .when()
                .post(ORDER)
                .then();
    }

    public ValidatableResponse getListOfOrders() {
        return given()
                .get(ORDER)
                .then();
    }
}
