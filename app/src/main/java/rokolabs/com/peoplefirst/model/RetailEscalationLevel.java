package rokolabs.com.peoplefirst.model;

public class RetailEscalationLevel {
    
     public String type;
     public String first_name;
     public String last_name;
     public String email;

     public RetailEscalationLevel(String type, String first_name, String last_name, String email) {
          this.type = type;
          this.first_name = first_name;
          this.last_name = last_name;
          this.email = email;
     }
}
