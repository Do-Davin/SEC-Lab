package com.example;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

/**
 * UserViewModel class that acts as the ViewModel in the MVVM pattern.
 * This class exposes properties that the View can bind to and handles
 * the presentation logic.
 */
@Data
public class StudentViewModel {
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> birthDate = new SimpleObjectProperty<>(null);
    private final ObservableList<Student> students = FXCollections.observableArrayList();

    // public StudentViewModel() {
    //     // Initialize with empty values
    // }

    // Property accessors for data binding
    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public ObjectProperty<LocalDate> birthDateProperty() {
        return birthDate;
    }

    public ObservableList<Student> getStudents() {
        return students;
    }

    // Getter and setter methods for convenience
    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public LocalDate getBirthDate() {
        return birthDate.get();
    }

    public void setBirthDate(LocalDate date) {
        birthDate.set(date);
    }

    /**
     * Computed property that returns the full name by combining first and last name.
     * This is called whenever the first or last name changes.
     */
    public String getFullName() {
        String first = firstName.get();
        String last = lastName.get();
        
        if (first == null) first = "";
        if (last == null) last = "";
        
        return (first + " " + last).trim();
    }

    /**
     * Adds a new user to the collection based on current first and last name values.
     */
    public void addStudent(String criteria, boolean ascending) {
        if (isValidStudent()) {
            Student newStudent = new Student(getFirstName(), getLastName(), getEmail(), getBirthDate());
            students.add(newStudent);
            sortStudents(criteria, ascending);
            clearForm();
        }
    }

    /**
     * Removes a user from the collection.
     */
    public void removeStudent(Student student) {
        if (student != null) {
            students.remove(student);
        }
    }

    /**
     * Clears the form by resetting first and last name properties.
     */
    public void clearForm() {
        setFirstName("");
        setLastName("");
        setEmail("");
        setBirthDate(null);
    }

    /**
     * Validates if the current first and last name form a valid user.
     */
    public boolean isValidStudent() {
        return getFirstName() != null && !getFirstName().trim().isEmpty() &&
               getLastName() != null && !getLastName().trim().isEmpty() &&
               getEmail() != null && !getEmail().trim().isEmpty() &&
               getBirthDate() != null;
    }

    /**
     * Gets the count of users in the collection.
     */
    public int getStudentCount() {
        return students.size();
    }

    public void sortStudents(String criteria, boolean ascending) {
        FXCollections.sort(students, (s1, s2) -> {
            int result = 0;

            switch (criteria) {
                case "First Name":
                    result = s1.getFirstName().compareToIgnoreCase(s2.getFirstName());
                    break;

                case "Last Name":
                    result = s1.getLastName().compareToIgnoreCase(s2.getLastName());
                    break;

                case "Email":
                    result = s1.getEmail().compareToIgnoreCase(s2.getEmail());
                    break;

                case "Birthdate":
                    // null-safe compare
                    if (s1.getBirthDate() == null && s2.getBirthDate() == null) result = 0;
                    else if (s1.getBirthDate() == null) result = -1;
                    else if (s2.getBirthDate() == null) result = 1;
                    else result = s1.getBirthDate().compareTo(s2.getBirthDate());
                    break;
            }

            return ascending ? result : -result;
        });
    }
}