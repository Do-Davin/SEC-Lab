package com.davin.attendance;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class AttendanceService {

    private final Map<String, Boolean> records = new LinkedHashMap<>();

    public void addStudent(String name) {
        records.putIfAbsent(name, null);
        // Debug
        System.out.println("\nDEBUG" + records + "\n");
    }

    public boolean exists(String name) {
        // Debug
        System.out.println("\nDEBUG" + records + "\n");
        return records.containsKey(name);
    }

    public void mark(String name, boolean present) {
        if (!exists(name)) throw new IllegalArgumentException("Student not found: " + name);
        records.put(name, present);
        // Debug
        System.out.println("\nDEBUG" + records + "\n");
    }

    public Map<String, Boolean> getAll() {
        // Debug
        System.out.println("\nDEBUG" + records + "\n");
        return Collections.unmodifiableMap(records);
    }
}