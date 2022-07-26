package model;

import lombok.Data;

@Data
public class Order {

    private String[] ingredients;

    public Order (String[] ingredients) {
        this.ingredients = ingredients;
    }

    public static Order getOrder() {
        return new Order(
                new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa72"});
    }
}
