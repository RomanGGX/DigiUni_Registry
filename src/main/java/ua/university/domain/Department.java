package ua.university.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "code",
        scope = Department.class
)
public class Department {
    private int code;
    private String name;
    private Faculty faculty;
    private Teacher head;
    private String cabinet;

    public Department() {}

    public Department(int code, String name, Faculty faculty, Teacher head, String cabinet) {
        this.code = code;
        this.name = name;
        this.faculty = faculty;
        this.head = head;
        this.cabinet = cabinet;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public Teacher getHead() {
        return head;
    }

    public String getCabinet() {
        return cabinet;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setHead(Teacher head) {
        this.head = head;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    @Override
    public String toString() {
        String facultyName = (faculty != null) ? faculty.getShortName() : "не вказано";
        String headName = (head != null) ? head.getFullName() : "не призначено";

        return String.format(
                "[Код: %d] %s | Факультет: %s | Завідувач: %s | Кабінет: %s",
                code,
                name,
                facultyName,
                headName,
                cabinet
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department other = (Department) o;
        return this.code == other.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
