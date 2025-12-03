package com.davin.attendance;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AttendanceService service = new AttendanceService();

    public static void main(String[] args) {
        do {
            System.out.println("\n=== Attendance System ===");
            System.out.println("1) Add Student");
            System.out.println("2) Mark Attendance");
            System.out.println("3) View Records");
            System.out.println("4) Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addStudent();
                case "2" -> markAttendance();
                case "3" -> viewRecords();
                case "4" -> {
                    System.out.println("Thank you for using our system <3");
                    return;
                }
                default -> System.out.println("Invalid option.");
            } 
        } while (true);
    }

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty, try again...");
            return;
        }
        service.addStudent(name);
        System.out.println("Student NAME: " + name + " added.");
    }

    private static void markAttendance() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        if (!service.exists(name)) {
            System.out.println("Student NAME: " + name + " not found.");
            return;
        }
        System.out.print("Present? (y/n): ");
        String ans = scanner.nextLine().trim();
        Boolean present = ans.equalsIgnoreCase("y") ? Boolean.TRUE
                        : ans.equalsIgnoreCase("n") ? Boolean.FALSE : null;
        if (present == null) {
            System.out.println("Invalid input. Use y/n.");
            return;
        }
        service.mark(name, present);
        System.out.println("Marked " + name + " as " + (present ? "Present" : "Absent"));
    }

    private static void viewRecords() {
        System.out.println("\n--- Records ---");
        service.getAll().forEach((name, status) -> {
            String text = status == null ? "Not marked" : (status ? "Present" : "Absent");
            System.out.println(name + " : " + text);
        });
    }
}