package rokolabs.com.peoplefirst.model.requests;

public class ValidateEmailRequest {
    public String email;
    public String token;

    public ValidateEmailRequest(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
