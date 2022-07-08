import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class GetUserOrdersTest {

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
    @DisplayName("Get user orders after authorization")
    public void getUserOrdersAfterAuth() {
        User user = User.getRandom();
        ValidatableResponse createResponse = userClient.createUser(user);
        String accessToken = createResponse.extract().path("accessToken");
        String[] split = accessToken.split(" ");
        token = split[1];

        Order order = Order.getOrder();
        orderClient.makeOrder(token, order);
        ValidatableResponse ordersListResponse = orderClient.getOrders(token);

        int statusCode = ordersListResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_OK, statusCode);
        boolean result = ordersListResponse.extract().path("success");
        assertTrue(result);
        int total = ordersListResponse.extract().path("total");
        assertNotEquals("Количество заказов равно 0", 0, total);
    }

    @Test
    @DisplayName("Get user orders without authorization")
    public void getUserOrdersWithoutAuth() {
        User user = User.getRandom();
        ValidatableResponse createResponse = userClient.createUser(user);
        String accessToken = createResponse.extract().path("accessToken");
        String[] split = accessToken.split(" ");
        token = split[1];

        Order order = Order.getOrder();
        orderClient.makeOrder(token, order);
        ValidatableResponse ordersListResponse = orderClient.getOrders("");

        int statusCode = ordersListResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_UNAUTHORIZED, statusCode);
        boolean result = ordersListResponse.extract().path("success");
        assertFalse(result);
    }
}
