import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import prakticum.Courier;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import prakticum.StepsCourier;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

@Epic("Функционал курьера")
@Feature("Логин курьера в систему")
public class LoginCourierTest extends BasementTest{

    private StepsCourier stepsCourier = new StepsCourier();
    private Courier courier;
    Faker faker = new Faker();

    @Before
    public void setUp() {
        courier = new Courier();
        courier
                .setLogin(faker.regexify("[a-z]{8}"))
                .setPassword(faker.regexify("[a-zA-Z0-9]{8}"));

        stepsCourier.createCourier(courier);
    }


    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Успешная авторизация курьера при вводе валидного логина и пароля")
    public void loginCourierAllDataExisted() {
        stepsCourier
                .loginCourier(courier)
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация курьера при пустом логине")
    @Description("Проверка ответа системы, при авторизации курьера с пустым логином")
    public void courierNoLoginExisted() {
        courier.setLogin(null);
        stepsCourier
                .loginCourier(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера при пустом пароле")
    @Description("Проверка ответа системы, при авторизации курьера с пустым паролем")
    public void courierNoPasswordExisted() {
        courier.setPassword(null);
        stepsCourier
                .loginCourier(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера с невалидным логином")
    @Description("Проверка ответа системы, при авторизации курьера с невалидным логином")
    public void loginCourierUnexpectedDataLogin() {
        courier.setLogin(faker.regexify("[a-z]{6}"));
        stepsCourier
                .loginCourier(courier)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера с невалидным паролем")
    @Description("Проверка ответа системы, при авторизации курьера с невалидным паролем")
    public void passwordCourierUnexpectedDataPassword() {
        courier.setPassword(faker.regexify("[a-z0-9]{6}"));
        stepsCourier
                .loginCourier(courier)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        Integer id = null;
        try {
            id = stepsCourier.loginCourier(courier)
                    .extract().body().path("id");
        } catch (Exception e) {
        }

        if (id != null) {
            courier.setId(id);
            stepsCourier.deleteCourier(courier);
        }
    }
}