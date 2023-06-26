package za.co.invoke.solutions.naturalgasprices.dto;

import lombok.Data;

@Data
public class UserDto {

    private String username;
    private String password;

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", password=***********" +
                '}';
    }
}
