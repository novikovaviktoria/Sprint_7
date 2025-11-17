import static org.hamcrest.CoreMatchers.equalTo;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import prakticum.Courier;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import prakticum.StepsCourier;
import java.util.Locale;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

@Epic("Функционал курьера")
@Feature("Создать курьера")
public class CreationCourierTest extends BasementTest{

    private StepsCourier stepsCourier = new StepsCourier();
    private Courier courier;
    Faker faker = new Faker(new Locale("ru"));

    @Before
    public void setUp() {
        courier = new Courier()
          .setLogin(generateRandomLogin())
          .setPassword(generateRandomPassword())
          .setFirstName(generateRandomFirstName());
    }

    private String generateRandomLogin() {
        return faker.regexify("[a-z]{8}"); // Увеличил длину для надежности
    }

    private String generateRandomPassword() {
        return faker.regexify("[a-zA-Z0-9]{8}"); // Добавил большие буквы
    }

    private String generateRandomFirstName() {
        return faker.name().firstName();
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера: переданы все обязательные поля")
    public void createCourierAllDataExisted() {
        stepsCourier
                .createCourier(courier)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Создание двух курьеров с одинаковыми логинами, одинаковыми паролями")
    public void createCourierSameDataExistedTwice() {
        stepsCourier
                .createCourier(courier);

        stepsCourier
                .createCourier(courier)
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера при незаполненном логине")
    @Description("Создание курьера при пустом обязательном поле - логине")
    public void createCourierNoLoginExisted() {
        courier.setLogin(null);
        stepsCourier
                .createCourier(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера при незаполненном пароле")
    @Description("Создание курьера при пустом обязательном поле - пароле")
    public void createCourierNoPasswordExisted() {
        courier.setPassword(null);
        stepsCourier
                .createCourier(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
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