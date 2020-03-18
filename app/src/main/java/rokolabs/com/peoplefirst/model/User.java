package rokolabs.com.peoplefirst.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    public final static User EMPTY = new User();

    public Integer id;
    public int company_id;
    public String email = "";
    public String secondary_email;
    public String birthday;
    public String address;
    public String phone;
    public String role;
    public String first_name = "";
    public String last_name;
    public String department = "";
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
        return id.equals(user.id);
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

    public Boolean isFullyFilled() {
        if (first_name == null || last_name == null || email == null ||
                secondary_email == null || birthday == null || address == null ||
                department == null || phone == null) return false;
        return first_name.length() != 0 &&
                last_name.length() != 0 &&
                email.length() != 0 &&
                secondary_email.length() != 0 &&
                birthday.length() != 0 &&
                address.length() != 0 &&
                department.length() != 0 &&
                phone.length() != 0;
    }

}
