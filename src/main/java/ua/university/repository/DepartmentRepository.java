package ua.university.repository;

import ua.university.domain.Department;
import ua.university.domain.Faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentRepository {
    private Department[] departments;

    public DepartmentRepository() {
        departments = new Department[0];
    }

    public Department[] getDepartments() {
        return departments.clone();
    }

    public void setDepartments(Department[] initialDepartments) {
        this.departments = initialDepartments;
    }

    public Department[] findByFaculty(Faculty faculty) {
        List<Department> result = new ArrayList<>();
        for (Department department : departments) {
            if (department.getFaculty() != null &&
                    department.getFaculty().getCode() == faculty.getCode()) {
                result.add(department);
            }
        }
        return result.toArray(new Department[0]);
    }

    public Optional<Department> findByCode(int code) {
        for (Department department : departments) {
            if (department.getCode() == code) {
                return Optional.of(department);
            }
        }
        return Optional.empty();
    }

    public void addDepartment(Department department) {
        Department[] newArray = new Department[departments.length + 1];
        System.arraycopy(departments, 0, newArray, 0, departments.length);
        newArray[departments.length] = department;
        departments = newArray;
    }

    public boolean deleteDepartmentByCode(int code) {
        boolean found = false;
        for (Department department : departments) {
            if (department.getCode() == code) {
                found = true;
                break;
            }
        }
        if (!found) return false;

        Department[] newArray = new Department[departments.length - 1];
        int newIndex = 0;
        for (Department department : departments) {
            if (department.getCode() != code) {
                newArray[newIndex++] = department;
            }
        }
        departments = newArray;
        return true;
    }

    public boolean updateDepartment(int code, Department updatedDepartment) {
        for (int i = 0; i < departments.length; i++) {
            if (departments[i].getCode() == code) {
                departments[i] = updatedDepartment;
                return true;
            }
        }
        return false;
    }

    public int getNextCode() {
        int maxCode = 0;
        for (Department department : departments) {
            if (department.getCode() > maxCode) {
                maxCode = department.getCode();
            }
        }
        return maxCode + 1;
    }

}
