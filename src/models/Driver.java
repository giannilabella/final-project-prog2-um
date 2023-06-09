package models;

import java.util.Objects;
import java.util.regex.Pattern;

public class Driver {
    private final int id;
    private final String fullName;
    private final Pattern driverPattern;

    public Driver(int id, String fullName, Pattern driverPattern) {
        this.id = id;
        this.fullName = fullName;
        this.driverPattern = driverPattern;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isMentioned(String text) {
        return driverPattern.matcher(text).find();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return id == driver.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
