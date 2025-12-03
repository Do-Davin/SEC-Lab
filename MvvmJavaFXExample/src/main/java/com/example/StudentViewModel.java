package com.example;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UserViewModel class that acts as the ViewModel in the MVVM pattern.
 * This class exposes properties that the View can bind to and handles
 * the presentation logic.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentViewModel {
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> birthDate = new SimpleObjectProperty<>(null);
    private final StringProperty searchQuery = new SimpleStringProperty("");
    private final ObservableList<Student> filteredStudents = FXCollections.observableArrayList();

    ObservableList<Student> students = FXCollections.observableArrayList(
        new Student("Do", "Davin", "davin@gmail.com", LocalDate.of(2009, 1, 13)),
        new Student("Chin", "Hongnyheng", "chin.hongnyheng@gmail.com", LocalDate.of(2008, 3, 22)),
        new Student("Sam", "Sokleap", "sam.sokleap@yahoo.com", LocalDate.of(2008, 7, 15)),
        new Student("Thy", "Sethasarakvath", "thy.sethasarakvath@gmail.com", LocalDate.of(2008, 11, 4)),
        new Student("Srun", "Naieang", "srun.naieang@gmail.com", LocalDate.of(2009, 5, 18)),
        new Student("Virak", "Rith", "virak.rith@hotmail.com", LocalDate.of(2008, 9, 9)),
        new Student("Chea", "Panharith", "chea.panharith@gmail.com", LocalDate.of(2009, 2, 27)),
        new Student("Noch", "Munny Ratanak", "noch.ratanak@gmail.com", LocalDate.of(2009, 6, 3)),
        new Student("Tat", "Chansereyvong", "tat.chansereyvong@gmail.com", LocalDate.of(2008, 12, 13)),
        new Student("Huoth", "Sitha", "huoth.sitha@gmail.com", LocalDate.of(2009, 8, 20)),
        new Student("Kheang", "Ann", "kheang.ann@gmail.com", LocalDate.of(2008, 4, 11)),
        new Student("Nut", "Sopaphiirum", "nut.sopaphiirum@yahoo.com", LocalDate.of(2009, 10, 1)),
        new Student("Try", "Khemchhun", "try.khemchhun@gmail.com", LocalDate.of(2009, 3, 6)),
        new Student("Pon", "Pulprachgnar", "pon.pulprachgnar@gmail.com", LocalDate.of(2008, 5, 29))
    );

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

    public ObservableList<Student> getFilteredStudents() {
        return filteredStudents;
    }

    public StringProperty searchQueryProperty() {
        return searchQuery;
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
            filterStudents(criteria, ascending);
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

    public void filterStudents(String criteria, boolean ascending) {
        filteredStudents.clear();

        String query = searchQuery.get() == null ? "" : searchQuery.get().toLowerCase().trim();

        // Filter
        students.stream()
            .filter(s ->
                s.getFirstName().toLowerCase().contains(query) ||
                s.getLastName().toLowerCase().contains(query) ||
                s.getEmail().toLowerCase().contains(query))
            .forEach(filteredStudents::add);

        sortStudents(criteria, ascending);
    }

    public void sortStudents(String criteria, boolean ascending) {
        FXCollections.sort(filteredStudents, (s1, s2) -> {
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