package rokolabs.com.peoplefirst.model.requests;

public class UserActivastionRequest {

    public String email;
    public String password;
    public String first_name;
    public String last_name;

    public UserActivastionRequest(String email, String password, String first_name, String last_name) {
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
    }
}
