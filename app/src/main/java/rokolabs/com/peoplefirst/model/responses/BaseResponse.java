package rokolabs.com.peoplefirst.model.responses;

public class BaseResponse {

    public boolean success;

    public Error error;
    public static class Error {
        public int code;
        public String message;
    }
}
