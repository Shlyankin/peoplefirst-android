package rokolabs.com.peoplefirst.model;

import java.io.Serializable;
import java.util.Objects;

public class HarassmentReason implements Serializable {

    public int id;

    public String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HarassmentReason that = (HarassmentReason) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
