package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;

public class OrderClient extends RestAssuredClient {

    private final String ORDERMAKE = "/orders";
    private final String ORDERGET = "/orders";

    @Step("Making order")
    public ValidatableResponse makeOrder(String token, Order order) {
        return reqSpec
                .auth().oauth2(token)
                .body(order)
                .when()
                .post(ORDERMAKE)
                .then().log().all();
    }

    @Step("Get user orders")
    public ValidatableResponse getOrders(String token) {
        return reqSpec
                .auth().oauth2(token)
                .when()
                .get(ORDERGET)
                .then().log().all();
    }
}
