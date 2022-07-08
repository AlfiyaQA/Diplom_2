import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CreateUserWithoutRequiredFieldsTest {

    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Create user without email")
    public void createUserWithoutEmail() {
        User user = User.getRandom();
        user.setEmail(null);
        ValidatableResponse createResponse = userClient.createUser(user);

        int statusCode = createResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_FORBIDDEN, statusCode);
        boolean result = createResponse.extract().path("success");
        assertFalse(result);
    }

    @Test
    @DisplayName("Create user without password")
    public void createUserWithoutPassword() {
        User user = User.getRandom();
        user.setPassword(null);
        ValidatableResponse createResponse = userClient.createUser(user);

        int statusCode = createResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_FORBIDDEN, statusCode);
        boolean result = createResponse.extract().path("success");
        assertFalse(result);
    }

    @Test
    @DisplayName("Create user without name")
    public void createUserWithoutName() {
        User user = User.getRandom();
        user.setName(null);
        ValidatableResponse createResponse = userClient.createUser(user);

        int statusCode = createResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_FORBIDDEN, statusCode);
        boolean result = createResponse.extract().path("success");
        assertFalse(result);
    }
}
