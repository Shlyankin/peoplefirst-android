package rokolabs.com.peoplefirst.model;

import java.io.Serializable;

public class Person implements Serializable {


    public String email ="a@a.com";
    public String first_name;
    public String last_name;

    public Person(String firstName, String last_name) {
        this.first_name = firstName;
        this.last_name = last_name;
    }
}
