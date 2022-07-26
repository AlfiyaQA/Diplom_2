import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class ChangeUserTest {

    private UserClient userClient;
    private String token;
    User user = User.getRandom();

    @Before
    public void setUp() {
        userClient = new UserClient();
        ValidatableResponse createResponse = userClient.createUser(user);
        token = userClient.getUserToken(createResponse);
    }

    @After
    public void teardown() {
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Change email with auth")
    public void changeEmailWithAuth() {
        userClient.getUser(token);
        UserInfo newInfo = new UserInfo(RandomStringUtils.randomAlphanumeric(5) + "@" + RandomStringUtils.randomAlphanumeric(5) + ".ru", user.getName());
        ValidatableResponse changeResponse = userClient.changeUser(token, newInfo);

        int statusCode = changeResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_OK, statusCode);
        boolean result = changeResponse.extract().path("success");
        assertTrue(result);
    }

    @Test
    @DisplayName("Change name with auth")
    public void changeNameWithAuth() {
        userClient.getUser(token);
        UserInfo newInfo = new UserInfo(user.getEmail(), RandomStringUtils.randomAlphanumeric(10));
        ValidatableResponse changeResponse = userClient.changeUser(token, newInfo);

        int statusCode = changeResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_OK, statusCode);
        boolean result = changeResponse.extract().path("success");
        assertTrue(result);
    }

    @Test
    @DisplayName("Change data without auth")
    public void changeDataWithoutAuth() {
        userClient.getUser(token);
        UserInfo newInfo = new UserInfo(RandomStringUtils.randomAlphanumeric(5) + "@" + RandomStringUtils.randomAlphanumeric(5) + ".ru", RandomStringUtils.randomAlphanumeric(10));
        ValidatableResponse changeResponse = userClient.changeUser("", newInfo);

        int statusCode = changeResponse.extract().statusCode();
        assertEquals("Код статуса отличается от ожидаемого результата", SC_UNAUTHORIZED, statusCode);
        boolean result = changeResponse.extract().path("success");
        assertFalse(result);
    }
}
