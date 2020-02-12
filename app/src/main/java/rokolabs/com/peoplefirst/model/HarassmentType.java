package rokolabs.com.peoplefirst.model;

import java.io.Serializable;
import java.util.Objects;

public class HarassmentType implements Serializable {

    public int id;

    public String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HarassmentType that = (HarassmentType) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
