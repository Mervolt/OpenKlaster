package model;
import lombok.Data;


@Data
public class User implements Model {
    private String username;
    private String password;

}
