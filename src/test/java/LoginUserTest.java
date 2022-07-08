import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredentials;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class LoginUserTest {

    private UserClient userClient;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void teardown() {
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Login using correct data")
    public void loginUsingCorrectData() {
        User user = User.getRandom();
        ValidatableResponse createResponse = userClient.createUser(user);
        String accessToken = createResponse.extract().path("accessToken");
        String[] split = accessToken.split(" ");
        token = split[1];

        UserCredentials creds = UserCredentials.from(user);
        ValidatableResponse loginResponse = userClient.loginUser(creds);

        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_OK, statusCode);
        boolean result = loginResponse.extract().path("success");
        assertTrue(result);
    }

    @Test
    @DisplayName("Login using incorrect data")
    public void loginUsingIncorrectData() {
        User user = User.getRandom();
        ValidatableResponse createResponse = userClient.createUser(user);
        String accessToken = createResponse.extract().path("accessToken");
        String[] split = accessToken.split(" ");
        token = split[1];

        UserCredentials creds = UserCredentials.from(user);
        ValidatableResponse loginResponse = userClient.loginUser(new UserCredentials(RandomStringUtils.randomAlphanumeric(5)+"@"+ RandomStringUtils.randomAlphanumeric(5)+".ru", RandomStringUtils.randomAlphanumeric(10)));

        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_UNAUTHORIZED, statusCode);
        boolean result = loginResponse.extract().path("success");
        assertFalse(result);
    }
}
