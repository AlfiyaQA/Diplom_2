import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateUserWithoutRequiredFieldsTest {

    private UserClient userClient;
    private String token;
    User user = User.getRandom();

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void teardown() {
        if (token != null) {
            userClient.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Create user without email")
    public void createUserWithoutEmail() {
        user.setEmail(null);
        ValidatableResponse createResponse = userClient.createUser(user);

        token = userClient.getUserToken(createResponse);
        assertNull(token);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_FORBIDDEN, statusCode);
        boolean result = createResponse.extract().path("success");
        assertFalse(result);
    }

    @Test
    @DisplayName("Create user without password")
    public void createUserWithoutPassword() {
        user.setPassword(null);
        ValidatableResponse createResponse = userClient.createUser(user);

        token = userClient.getUserToken(createResponse);
        assertNull(token);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_FORBIDDEN, statusCode);
        boolean result = createResponse.extract().path("success");
        assertFalse(result);
    }

    @Test
    @DisplayName("Create user without name")
    public void createUserWithoutName() {
        user.setName(null);
        ValidatableResponse createResponse = userClient.createUser(user);

        token = userClient.getUserToken(createResponse);
        assertNull(token);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_FORBIDDEN, statusCode);
        boolean result = createResponse.extract().path("success");
        assertFalse(result);
    }
}
