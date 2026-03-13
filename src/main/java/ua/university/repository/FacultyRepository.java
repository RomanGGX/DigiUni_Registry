package ua.university.repository;

import ua.university.domain.Faculty;

import java.util.*;
import java.util.Optional;

public class FacultyRepository implements Repository<Faculty, Integer> {

    private final Map<Integer, Faculty> facultiesByCode = new LinkedHashMap<>();

    @Override
    public List<Faculty> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(facultiesByCode.values()));
    }

    @Override
    public Optional<Faculty> findById(Integer code) {
        return Optional.ofNullable(facultiesByCode.get(code));
    }

        @Override
    public void add(Faculty faculty) {
        if (facultiesByCode.containsKey(faculty.getCode())) {
            throw new IllegalArgumentException("Факультет з кодом " + faculty.getCode() + " вже існує.");
        }
        facultiesByCode.put(faculty.getCode(), faculty);
    }

    @Override
    public boolean deleteById(Integer code) {
        return facultiesByCode.remove(code) != null;
    }

    @Override
    public boolean update(Integer code, Faculty updatedFaculty) {
        if (!facultiesByCode.containsKey(code)) return false;
        facultiesByCode.put(code, updatedFaculty);
        return true;
    }

    public int getNextCode() {
        int maxCode = 0;
        for (int code : facultiesByCode.keySet()) {
            if (code > maxCode)
                maxCode = code;
        }
        return maxCode + 1;
    }
}
