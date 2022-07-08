package model;

import lombok.Data;

@Data
public class UserInfo {

    private String email;
    private String name;

    public UserInfo(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UserInfo(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
    }

    public static UserInfo from(User user) {
        return new UserInfo(user);
    }
}
