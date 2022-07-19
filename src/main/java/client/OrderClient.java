package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;

public class OrderClient extends RestAssuredClient {

    private final String ORDER = "/orders";

    @Step("Making order")
    public ValidatableResponse makeOrder(String token, Order order) {
        return reqSpec
                .auth().oauth2(token)
                .body(order)
                .when()
                .post(ORDER)
                .then().log().all();
    }

    @Step("Get user orders")
    public ValidatableResponse getOrders(String token) {
        return reqSpec
                .auth().oauth2(token)
                .when()
                .get(ORDER)
                .then().log().all();
    }
}
