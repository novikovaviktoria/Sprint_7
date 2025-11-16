import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import static java.util.concurrent.TimeUnit.DAYS;
import static org.hamcrest.CoreMatchers.notNullValue;
import prakticum.Order;
import net.datafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import prakticum.StepsOrder;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Epic("Функционал заказа")
@Feature("Получить список заказов")
public class ListOfOrdersTest extends BasementTest {

    private StepsOrder stepsOrder = new StepsOrder();
    private Order order;
    Faker faker = new Faker(new Locale("ru"));

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

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверить, что система возвращает в тело ответа - список заказов")
    public void getFullListOfOrders() {
        stepsOrder.createOrder(order);

        stepsOrder.getListOfOrders()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}