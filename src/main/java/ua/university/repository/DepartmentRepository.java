package ua.university.repository;

import ua.university.domain.Department;
import ua.university.domain.Faculty;

import java.util.*;


public class DepartmentRepository implements Repository<Department, Integer> {

    private final Map<Integer, Department> departmentsByCode = new LinkedHashMap<>();

    @Override
    public List<Department> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(departmentsByCode.values()));
    }

    @Override
    public Optional<Department> findById(Integer code) {
        return Optional.ofNullable(departmentsByCode.get(code));
    }

    @Override
    public void add(Department department) {
        if (departmentsByCode.containsKey(department.getCode())) {
            throw new IllegalArgumentException("Кафедра з кодом " + department.getCode() + " вже існує.");
        }
        departmentsByCode.put(department.getCode(), department);
    }

    @Override
    public boolean deleteById(Integer code) {
        return departmentsByCode.remove(code) != null;
    }

    @Override
    public boolean update(Integer code, Department updatedDepartment) {
        if (!departmentsByCode.containsKey(code)) return false;
        departmentsByCode.put(code, updatedDepartment);
        return true;
    }

    public List<Department> findByFaculty(Faculty faculty) {
        List<Department> result = new ArrayList<>();
        for (Department department : departmentsByCode.values()) {
            if (department.getFaculty() != null &&
                    department.getFaculty().getCode() == faculty.getCode()) {
                result.add(department);
            }
        }
        return result;
    }

    public int getNextCode() {
        int maxCode = 0;
        for (int code : departmentsByCode.keySet()) {
            if (code > maxCode) maxCode = code;
        }
        return maxCode + 1;
    }

}
