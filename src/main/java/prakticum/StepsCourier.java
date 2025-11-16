package prakticum;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import prakticum.Courier;
import static io.restassured.RestAssured.given;

public class StepsCourier {
    public static final String COURIER_CREATION = "/api/v1/courier";
    public static final String COURIER_LOGIN = "/api/v1/courier/login";
    public static final String COURIER_ID = "/api/v1/courier/";

    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .body(courier)
                .when()
                .post(COURIER_CREATION)
                .then();
    }

    @Step("Логин курьера")
    public ValidatableResponse loginCourier(Courier courier) {
        return given()
                .body(courier)
                .when()
                .post(COURIER_LOGIN)
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(Courier courier) {
        return  given()
                .delete(COURIER_ID + (courier.getId() != null ? courier.getId() : ""))
                .then();
    }
}
