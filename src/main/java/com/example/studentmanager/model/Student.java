package com.example.studentmanager.model;

import javafx.beans.property.*;

/**
 * Student Model Class
 * Represents a student entity in the MVC architecture
 * Contains business logic and data validation
 */
public class Student {
    private final IntegerProperty id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final IntegerProperty age;
    private final DoubleProperty gpa;
    private final StringProperty major;
    private final StringProperty phoneNumber;
    private final StringProperty domainName;
    private final StringProperty school;
    private final ObjectProperty<java.time.LocalDate> enrollmentDate;

    public Student() {
        this.id = new SimpleIntegerProperty();
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.age = new SimpleIntegerProperty();
        this.gpa = new SimpleDoubleProperty();
        this.major = new SimpleStringProperty();
        this.phoneNumber = new SimpleStringProperty();
        this.enrollmentDate = new SimpleObjectProperty<>();
        this.domainName = new SimpleStringProperty();
        this.school = new SimpleStringProperty();
    }

    public Student(int id, String firstName, String lastName, String email,
            int age, double gpa, String major, String phoneNumber,
            java.time.LocalDate enrollmentDate) {
        this();
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setAge(age);
        setGpa(gpa);
        setMajor(major);
        setPhoneNumber(phoneNumber);
        setEnrollmentDate(enrollmentDate);
    }

    // Business logic methods
    public boolean isHonorStudent() {
        return getGpa() >= 3.5;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public boolean isValidForGraduation() {
        return getGpa() >= 2.0 && getAge() >= 18;
    }

    public String getAcademicStatus() {
        double gpa = getGpa();
        if (gpa >= 3.7)
            return "Summa Cum Laude";
        if (gpa >= 3.5)
            return "Magna Cum Laude";
        if (gpa >= 3.3)
            return "Cum Laude";
        if (gpa >= 3.0)
            return "Dean's List";
        if (gpa >= 2.0)
            return "Good Standing";
        return "Academic Probation";
    }

    public boolean isEligibleForScholarship() {
        return getGpa() >= 3.5 && getAge() <= 25;
    }

    public int getYearsEnrolled() {
        if (enrollmentDate.get() == null)
            return 0;
        return java.time.Period.between(enrollmentDate.get(), java.time.LocalDate.now()).getYears();
    }

    // Validation methods
    public boolean isValidEmail() {
        String email = getEmail();
        if (email == null || email.trim().isEmpty())
            return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean isValidAge() {
        int age = getAge();
        return age >= 16 && age <= 100;
    }

    public boolean isValidGpa() {
        double gpa = getGpa();
        return gpa >= 0.0 && gpa <= 4.0;
    }

    public boolean isValidPhoneNumber() {
        String phone = getPhoneNumber();
        if (phone == null || phone.trim().isEmpty())
            return true; // Optional field
        return phone.matches("^[\\+]?[1-9]?[0-9]{7,15}$");
    }

    // Property getters for JavaFX binding
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public DoubleProperty gpaProperty() {
        return gpa;
    }

    public StringProperty majorProperty() {
        return major;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public ObjectProperty<java.time.LocalDate> enrollmentDateProperty() {
        return enrollmentDate;
    }

    // Getters and Setters
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName != null ? firstName.trim() : "");
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName != null ? lastName.trim() : "");
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email != null ? email.trim().toLowerCase() : "");
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public double getGpa() {
        return gpa.get();
    }

    public void setGpa(double gpa) {
        // Round to 2 decimal places
        this.gpa.set(Math.round(gpa * 100.0) / 100.0);
    }

    public String getMajor() {
        return major.get();
    }

    public void setMajor(String major) {
        this.major.set(major != null ? major.trim() : "");
    }

    public void setDomainName (String domainName) {
        // TODO
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber != null ? phoneNumber.trim() : "");
    }

    public java.time.LocalDate getEnrollmentDate() {
        return enrollmentDate.get();
    }

    public void setEnrollmentDate(java.time.LocalDate enrollmentDate) {
        this.enrollmentDate.set(enrollmentDate);
    }

    @Override
    public String toString() {
        return String.format("Student{id=%d, name='%s %s', email='%s', gpa=%.2f, major='%s'}",
                getId(), getFirstName(), getLastName(), getEmail(), getGpa(), getMajor());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Student student = (Student) obj;
        return getId() == student.getId();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getId());
    }
}