package rokolabs.com.peoplefirst.model.requests;

public class PushNotificationsTokenRequest {

	public String device_id;
	public String device_token;
	public String platform = "android";

	public PushNotificationsTokenRequest(String device_id, String device_token) {
		this.device_id = device_id;
		this.device_token = device_token;
	}
}
