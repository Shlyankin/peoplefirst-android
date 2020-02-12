package rokolabs.com.peoplefirst.model;

public class EscalationLevel {

    public String created_at;
    public String updated_at;
    public int id;
    public int company_id;
    public String name;
    public int days;
    public String contact;

    public EscalationLevel() {
    }

    public EscalationLevel(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }
}
