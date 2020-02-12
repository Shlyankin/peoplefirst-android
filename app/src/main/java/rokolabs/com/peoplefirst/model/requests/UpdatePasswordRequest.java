package rokolabs.com.peoplefirst.model.requests;

public class UpdatePasswordRequest {
    public String token;
    public String password;

    public UpdatePasswordRequest(String token, String password) {
        this.token = token;
        this.password = password;
    }
}
