package rokolabs.com.peoplefirst.model;

public class WitnessTestimonyRequest {
    public String type;
    public WitnessTestimony data;

    public WitnessTestimonyRequest(String type, WitnessTestimony data) {
        this.type = type;
        this.data = data;
    }
}
