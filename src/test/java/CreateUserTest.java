import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateUserTest {

    private UserClient userClient;
    private String token;
    User user = User.getRandom();

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void teardown() {
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Create new user")
    public void createNewUser() {
        ValidatableResponse createResponse = userClient.createUser(user);
        token = userClient.getUserToken(createResponse);

        int statusCode = createResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_OK, statusCode);
        boolean result = createResponse.extract().path("success");
        assertTrue(result);
    }

    @Test
    @DisplayName("Create the same user")
    public void createTheSameUser() {
        ValidatableResponse createResponse = userClient.createUser(user);
        token = userClient.getUserToken(createResponse);
        ValidatableResponse createResponse2 = userClient.createUser(user);

        int statusCode = createResponse2.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_FORBIDDEN, statusCode);
        boolean result = createResponse2.extract().path("success");
        assertFalse(result);
    }
}
