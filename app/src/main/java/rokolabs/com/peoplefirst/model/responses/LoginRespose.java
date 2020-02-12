package rokolabs.com.peoplefirst.model.responses;

public class LoginRespose extends BaseResponse {

    public Data data;

    public static class Data {
        public String token;
    }

}
