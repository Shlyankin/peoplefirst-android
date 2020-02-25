package rokolabs.com.peoplefirst.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    public final static User EMPTY = new User();

    public int id = new Long(System.currentTimeMillis()).intValue();
    public int company_id;
    public String email;
    public String secondary_email;
    public String birthday;
    public String address;
    public String phone;
    public String role;
    public String first_name;
    public String last_name;
    public String department;
    public String created_at;
    public String updated_at;

    public String report_witness_status;
    public String report_aggressor_status;

    public Company company;

    public String address_unit_apt_suite;

    public Integer retail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public RetailUser convertToRetail() {
        RetailUser ru = new RetailUser();
        ru.email = email;
        ru.first_name = first_name;
        ru.last_name = last_name;
        return ru;
    }

}
