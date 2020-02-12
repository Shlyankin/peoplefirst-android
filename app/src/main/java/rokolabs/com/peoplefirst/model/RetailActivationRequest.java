package rokolabs.com.peoplefirst.model;

public class RetailActivationRequest {
    
    public String email;
    public int code;

    public RetailActivationRequest(String email, int code) {
        this.email = email;
        this.code = code;
    }
}
