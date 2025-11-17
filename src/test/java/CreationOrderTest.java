import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import static java.util.concurrent.TimeUnit.DAYS;
import static org.hamcrest.CoreMatchers.notNullValue;
import prakticum.Order;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import prakticum.StepsOrder;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(Parameterized.class)
@Epic("Функционал заказа")
@Feature("Создать заказ")
public class CreationOrderTest extends BasementTest {

    private StepsOrder stepsOrder = new StepsOrder();
    private Order order;
    Faker faker = new Faker(new Locale("ru"));

    private final String[] colors;

    @Before
    public void setUp() {
        order = new Order();
        order
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setAddress(faker.address().streetAddress())
                .setMetroStation(String.valueOf(ThreadLocalRandom.current().nextInt(1, 10)))
                .setPhone(faker.regexify("\\+7[0-9]{11}"))
                .setRentTime(ThreadLocalRandom.current().nextInt(1, 14))
                .setDeliveryDate(faker.date().future(14, DAYS, "yyyy-MM-dd"))
                .setComment(faker.regexify("[А-Яа-я]{50}"));
    }

    public CreationOrderTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Тестовые данные: цвет {0}")
    public static Object[][] getParameters() {
        return new Object[][]{
                {new String[] {"BLACK"}},
                {new String[] {"GREY"}},
                {new String[] {"BLACK", "GREY"}},
                {new String[] {}},
        };
    }

    @Test
    @DisplayName("Проверка создания заказа с возможными вариантами выбора цвета самоката")
    @Description("Проверка создания заказа с выбором цвета самоката: без цвета самоката, оба цвета сразу: черный и серый, только черный, только серый цвет")
    public void creationOrderDependOnColor() {
        order.setColor(colors);
        stepsOrder
                .createOrder(order)
                .statusCode(SC_CREATED)
                .body("track", notNullValue());
    }
    @After
    public void tearDown() {
        try {
            Integer track = stepsOrder
                    .createOrder(order)
                    .extract().body().path("track");

            if (track != null) {
                order.setTrack(track);
                stepsOrder.cancelOrder(order);
            }
        } catch (Exception ignored) {
        }
    }
}
