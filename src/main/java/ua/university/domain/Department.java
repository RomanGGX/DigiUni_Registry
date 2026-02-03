package ua.university.domain;

public class Department {
    private int code;
    private String name;
    private Faculty faculty;
    private Teacher head;
    private String cabinet;

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
        return "Department{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", faculty=" + faculty +
                ", head=" + head +
                ", cabinet='" + cabinet + '\'' +
                '}';
    }
}
