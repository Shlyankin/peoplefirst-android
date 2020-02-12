package rokolabs.com.peoplefirst.model;

import java.util.Objects;

public class RetailUser {
    public String email;
    public String first_name;
    public String last_name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RetailUser that = (RetailUser) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(first_name, that.first_name) &&
                Objects.equals(last_name, that.last_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, first_name, last_name);
    }
}
