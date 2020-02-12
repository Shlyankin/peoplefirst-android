package rokolabs.com.peoplefirst.model;

public class VictimTestimonyRequest {
    public String type;
    public VictimTestimony data;

    public VictimTestimonyRequest(String type, VictimTestimony data) {
        this.type = type;
        this.data = data;
    }
}
