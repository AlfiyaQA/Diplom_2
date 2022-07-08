package model;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class User {
    private String email;
    private String password;
    private String name;

    public User (String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User getRandom() {
        String email = (RandomStringUtils.randomAlphanumeric(5)+"@"+ RandomStringUtils.randomAlphanumeric(5)+".ru").toLowerCase();
        String password = RandomStringUtils.randomAlphanumeric(10);
        String name = RandomStringUtils.randomAlphanumeric(10);
        return new User (email, password, name);
    }
}
