import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class MakeOrderTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @After
    public void teardown() {
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Make order after authorization")
    public void makeOrderWithIngredientsAfterAuth() {
        User user = User.getRandom();
        ValidatableResponse createResponse = userClient.createUser(user);
        String accessToken = createResponse.extract().path("accessToken");
        String[] split = accessToken.split(" ");
        token = split[1];

        Order order = Order.getOrder();
        ValidatableResponse makeOrderResponse = orderClient.makeOrder(token, order);

        int statusCode = makeOrderResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_OK, statusCode);
        boolean result = makeOrderResponse.extract().path("success");
        assertTrue(result);
        String email = makeOrderResponse.extract().path("order.owner.email");
        assertEquals("Полученный email отличается от ожидаемого", user.getEmail(), email);
        makeOrderResponse.assertThat().body("order.ingredients._id", notNullValue());
    }

    @Test
    @DisplayName("Make order without authorization")
    public void makeOrderWithoutAuth() {
        User user = User.getRandom();
        ValidatableResponse createResponse = userClient.createUser(user);
        String accessToken = createResponse.extract().path("accessToken");
        String[] split = accessToken.split(" ");
        token = split[1];

        Order order = Order.getOrder();
        ValidatableResponse makeOrderResponse = orderClient.makeOrder("", order);

        int statusCode = makeOrderResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_OK, statusCode);
        boolean result = makeOrderResponse.extract().path("success");
        assertTrue(result);
        makeOrderResponse.assertThat().body("order.owner.email", nullValue());
    }

    @Test
    @DisplayName("Make order without ingredients")
    public void makeOrderWithoutIngredients() {
        User user = User.getRandom();
        ValidatableResponse createResponse = userClient.createUser(user);
        String accessToken = createResponse.extract().path("accessToken");
        String[] split = accessToken.split(" ");
        token = split[1];

        Order order = Order.getOrder();
        order.setIngredients(null);
        ValidatableResponse makeOrderResponse = orderClient.makeOrder(token, order);

        int statusCode = makeOrderResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_BAD_REQUEST, statusCode);
        boolean result = makeOrderResponse.extract().path("success");
        assertFalse(result);
    }

    @Test
    @DisplayName("Make order with wrong ingredients hash")
    public void makeOrderWithWrongIngredientsHash() {
        User user = User.getRandom();
        ValidatableResponse createResponse = userClient.createUser(user);
        String accessToken = createResponse.extract().path("accessToken");
        String[] split = accessToken.split(" ");
        token = split[1];

        Order order = Order.getOrder();
        order.setIngredients(new String[]{RandomStringUtils.randomAlphanumeric(24), RandomStringUtils.randomAlphanumeric(24), RandomStringUtils.randomAlphanumeric(24)});
        ValidatableResponse makeOrderResponse = orderClient.makeOrder(token, order);

        int statusCode = makeOrderResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_INTERNAL_SERVER_ERROR, statusCode);
    }
}
