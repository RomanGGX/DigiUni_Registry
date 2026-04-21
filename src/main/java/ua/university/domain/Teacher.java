package ua.university.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Teacher.class
)
public class Teacher extends Person{
    private String position;
    private Department department;
    private String academicDegree;
    private String academicTitle;
    private String employmentDate;
    private double workload;

    public Teacher() {}

    public Teacher(int id, String firstName, String middleName, String lastName, String birthDate, String email, String phoneNumber,
                   String position,  Department department, String academicDegree, String academicTitle, String employmentDate, double workload){
        super(id, firstName, middleName, lastName, birthDate, email, phoneNumber);
        this.position = position;
        this.department = department;
        this.academicDegree = academicDegree;
        this.academicTitle = academicTitle;
        this.employmentDate = employmentDate;
        this.workload = workload;
    }

    public String getPosition() {
        return position;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public double getWorkload() {
        return workload;
    }

    public String getEmploymentDate() {
        return employmentDate;
    }

    public Department getDepartment() {
        return department;
    }

    @JsonIgnore
    public Faculty getFaculty() {
        if (department != null) {
            return department.getFaculty();
        }
        return null;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public void setEmploymentDate(String employmentDate) {
        this.employmentDate = employmentDate;
    }

    public void setWorkload(double workload) {
        this.workload = workload;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + getId() +
                ", fullName='" + getFullName() + '\'' +
                ", position='" + position + '\'' +
                ", department='" + (department != null ? department.getName() : "не вказано") + '\'' +
                ", academicDegree='" + academicDegree + '\'' +
                ", academicTitle='" + academicTitle + '\'' +
                ", workload=" + workload + " годин" +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher other = (Teacher) o;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
