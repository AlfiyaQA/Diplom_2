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
    User user = User.getRandom();
    Order order = Order.getOrder();

    @Before
    public void setUp() {
        userClient = new UserClient();
        ValidatableResponse createResponse = userClient.createUser(user);
        token = userClient.getUserToken(createResponse);
        orderClient = new OrderClient();
        orderClient.makeOrder(token, order);
    }

    @After
    public void teardown() {
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Get user orders after authorization")
    public void getUserOrdersAfterAuth() {
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
        ValidatableResponse ordersListResponse = orderClient.getOrders("");

        int statusCode = ordersListResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_UNAUTHORIZED, statusCode);
        boolean result = ordersListResponse.extract().path("success");
        assertFalse(result);
    }
}
