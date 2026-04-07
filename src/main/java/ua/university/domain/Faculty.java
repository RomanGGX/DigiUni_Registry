package ua.university.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "code",
        scope = Faculty.class
)
public class Faculty {
    private int code;
    private String name;
    private String shortName;
    private Teacher dean;
    private String contacts;

    public Faculty() {}

    public Faculty(int code, String name, String shortName, Teacher dean, String contacts) {
        this.code = code;
        this.name = name;
        this.shortName = shortName;
        this.dean = dean;
        this.contacts = contacts;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public Teacher getDean() {
        return dean;
    }

    public String getContacts() {
        return contacts;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setDean(Teacher dean) {
        this.dean = dean;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", dean=" + (dean != null ? dean.getFullName() : "не призначено") +
                ", contacts='" + contacts + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Faculty)) return false;
        Faculty other = (Faculty) o;
        return this.code == other.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
