package rokolabs.com.peoplefirst.model;

public class RetailRegistrationRequestOld {
    public String first_name;
    public String last_name;
    public String email;
    public String company_external_id;
    public String password;

    public RetailRegistrationRequestOld(String first_name, String last_name, String email, String company_external_id, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.company_external_id = company_external_id;
        this.password = password;
    }
}
