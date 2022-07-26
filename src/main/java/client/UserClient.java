package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredentials;
import model.UserInfo;
import static org.apache.http.HttpStatus.*;

public class UserClient extends RestAssuredClient {

    private final String REGISTRATION = "/auth/register";
    private final String LOGIN = "/auth/login";
    private final String USER = "/auth/user";

   @Step("Create new user")
    public ValidatableResponse createUser(User user) {
        return reqSpec
                .body(user)
                .when()
                .post(REGISTRATION)
                .then().log().all();
    }

    @Step("Login using credentials")
    public ValidatableResponse loginUser(UserCredentials creds) {
        return reqSpec
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all();
    }

    @Step("Get user")
    public ValidatableResponse getUser(String token) {
        return reqSpec
                .auth().oauth2(token)
                .when()
                .get(USER)
                .then().log().all();
    }

    @Step("Change user")
    public ValidatableResponse changeUser(String token, UserInfo newInfo) {
        return reqSpec
                .auth().oauth2(token)
                .body(newInfo)
                .when()
                .patch(USER)
                .then().log().all();
    }

    @Step("Delete user")
    public void deleteUser(String token) {
        reqSpec
               .auth().oauth2(token)
               .when()
               .delete(USER)
               .then().log().all().assertThat()
               .statusCode(SC_ACCEPTED);
    }

    @Step("Get user token")
    public String getUserToken(ValidatableResponse createResponse) {
        String accessToken = createResponse.extract().path("accessToken");
        if (accessToken != null) {
            String[] split = accessToken.split(" ");
            return split[1];
        } else {
            return null;
        }
    }
}
