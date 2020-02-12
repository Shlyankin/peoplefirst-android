package rokolabs.com.peoplefirst.model;

public class RetailWitnessTestimonyRequest {
    public String type;
    public RetailWitnessTestimony data;

    public RetailWitnessTestimonyRequest(String type, RetailWitnessTestimony data) {
        this.type = type;
        this.data = data;
    }
}
