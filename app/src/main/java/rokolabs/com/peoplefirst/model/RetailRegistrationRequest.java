package rokolabs.com.peoplefirst.model;

public class RetailRegistrationRequest {
    public String first_name;
    public String last_name;
    public String email;
    public String password;

    public RetailRegistrationRequest(String first_name, String last_name, String email, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }
}
