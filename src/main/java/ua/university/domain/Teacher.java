package ua.university.domain;

import java.time.LocalDate;

public class Teacher extends Person{
    private String position;
    private String academicDegree;
    private String academicTitle;
    private String employmentDate;
    private double workload;

    public Teacher(int id, String firstName, String middleName, String lastName, String birthDate, String email, String phoneNumber,
                   String position, String academicDegree, String academicTitle, String employmentDate, double workload){
        super(id, firstName, middleName, lastName, birthDate, email, phoneNumber);
        this.position = position;
        this.academicDegree = academicDegree;
        this.academicTitle = academicTitle;
        this.employmentDate = birthDate;
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

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + getId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", position='" + position + '\'' +
                ", academicDegree=" + academicDegree +
                ", academicTitle='" + academicTitle + '\'' +
                ", employmentDate=" + employmentDate +
                ", workload=" + workload +
                '}';
    }
}
