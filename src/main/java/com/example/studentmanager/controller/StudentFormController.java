package com.example.studentmanager.controller;

import com.example.studentmanager.model.Student;
import com.example.studentmanager.model.StudentDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;

/**
 * Controller for Student Form Dialog
 * Handles add/edit operations for students
 * Part of the Controller layer in MVC architecture
 */
public class StudentFormController implements Initializable {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentFormController.class);
    
    // FXML injected controls
    @FXML private Label formTitleLabel;
    
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private Spinner<Integer> ageSpinner;
    @FXML private DatePicker enrollmentDatePicker;
    
    @FXML private ComboBox<String> majorComboBox;
    @FXML private Spinner<Double> gpaSpinner;
    @FXML private Label academicStatusLabel;
    
    @FXML private Label firstNameErrorLabel;
    @FXML private Label lastNameErrorLabel;
    @FXML private Label emailErrorLabel;
    @FXML private Label phoneErrorLabel;
    @FXML private Label ageErrorLabel;
    @FXML private Label enrollmentDateErrorLabel;
    @FXML private Label majorErrorLabel;
    @FXML private Label gpaErrorLabel;
    
    @FXML private VBox validationSummary;
    @FXML private TextArea validationTextArea;
    
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button resetButton;
    
    // Model and parent controller
    private StudentDAO studentDAO;
    private StudentController parentController;
    private Student currentStudent;
    private boolean isEditMode = false;
    
    // Available majors
    private final List<String> availableMajors = Arrays.asList(
        "Computer Science", "Mathematics", "Physics", "Biology", "Chemistry",
        "History", "English", "Psychology", "Economics", "Business Administration",
        "Engineering", "Medicine", "Law", "Art", "Music", "Philosophy",
        "Political Science", "Sociology", "Anthropology", "Environmental Science"
    );
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing StudentFormController");
        
        setupSpinners();
        setupMajorComboBox();
        setupValidation();
        setupEventHandlers();
        
        // Set default values
        enrollmentDatePicker.setValue(LocalDate.now());
        updateAcademicStatus();
        
        logger.info("StudentFormController initialized successfully");
    }
    
    /**
     * Setup spinner controls
     */
    private void setupSpinners() {
        // Age spinner (16-100)
        SpinnerValueFactory<Integer> ageValueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(16, 100, 20);
        ageSpinner.setValueFactory(ageValueFactory);
        ageSpinner.setEditable(true);
        
        // GPA spinner (0.0-4.0)
        SpinnerValueFactory<Double> gpaValueFactory = 
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 4.0, 3.0, 0.1);
        gpaSpinner.setValueFactory(gpaValueFactory);
        gpaSpinner.setEditable(true);
        
        // Format GPA spinner to show 2 decimal places
        gpaSpinner.getValueFactory().setConverter(new javafx.util.StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                return value != null ? String.format("%.2f", value) : "0.00";
            }
            
            @Override
            public Double fromString(String string) {
                try {
                    double value = Double.parseDouble(string);
                    return Math.max(0.0, Math.min(4.0, value));
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        });
    }
    
    /**
     * Setup major combo box
     */
    private void setupMajorComboBox() {
        majorComboBox.setItems(FXCollections.observableArrayList(availableMajors));
        majorComboBox.setValue("Computer Science");
    }
    
    /**
     * Setup validation
     */
    private void setupValidation() {
        // Hide validation summary initially
        validationSummary.setVisible(false);
        
        // Clear all error labels
        clearErrorLabels();
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Update academic status when GPA changes
        gpaSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateAcademicStatus();
        });
        
        // Real-time validation
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> 
            validateField(firstNameField, firstNameErrorLabel, "First name is required"));
        
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> 
            validateField(lastNameField, lastNameErrorLabel, "Last name is required"));
        
        emailField.textProperty().addListener((observable, oldValue, newValue) -> 
            validateEmailField());
        
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> 
            validatePhoneField());
        
        // Commit spinner values on focus lost
        ageSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                ageSpinner.increment(0); // Force commit
            }
        });
        
        gpaSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                gpaSpinner.increment(0); // Force commit
                updateAcademicStatus();
            }
        });
    }
    
    /**
     * Set the DAO and parent controller
     */
    public void setStudentDAO(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }
    
    public void setParentController(StudentController parentController) {
        this.parentController = parentController;
    }
    
    /**
     * Set student for editing
     */
    public void setStudent(Student student) {
        this.currentStudent = student;
        this.isEditMode = true;
        
        formTitleLabel.setText("Edit Student");
        saveButton.setText("Update Student");
        
        // Populate fields
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        emailField.setText(student.getEmail());
        phoneField.setText(student.getPhoneNumber());
        ageSpinner.getValueFactory().setValue(student.getAge());
        gpaSpinner.getValueFactory().setValue(student.getGpa());
        majorComboBox.setValue(student.getMajor());
        
        if (student.getEnrollmentDate() != null) {
            enrollmentDatePicker.setValue(student.getEnrollmentDate());
        }
        
        updateAcademicStatus();
    }
    
    /**
     * Handle save button click
     */
    @FXML
    private void handleSave() {
        if (validateForm()) {
            saveStudent();
        }
    }
    
    /**
     * Handle cancel button click
     */
    @FXML
    private void handleCancel() {
        closeDialog();
    }
    
    /**
     * Handle reset button click
     */
    @FXML
    private void handleReset() {
        if (isEditMode && currentStudent != null) {
            // Reset to original values
            setStudent(currentStudent);
        } else {
            // Clear all fields
            clearForm();
        }
        clearErrorLabels();
        validationSummary.setVisible(false);
    }
    
    /**
     * Validate the entire form
     */
    private boolean validateForm() {
        List<String> errors = new ArrayList<>();
        
        // Clear previous error labels
        clearErrorLabels();
        
        // Validate required fields
        if (firstNameField.getText() == null || firstNameField.getText().trim().isEmpty()) {
            errors.add("First name is required");
            firstNameErrorLabel.setText("Required");
        }
        
        if (lastNameField.getText() == null || lastNameField.getText().trim().isEmpty()) {
            errors.add("Last name is required");
            lastNameErrorLabel.setText("Required");
        }
        
        if (emailField.getText() == null || emailField.getText().trim().isEmpty()) {
            errors.add("Email is required");
            emailErrorLabel.setText("Required");
        } else if (!isValidEmail(emailField.getText())) {
            errors.add("Email format is invalid");
            emailErrorLabel.setText("Invalid format");
        } else if (isEmailDuplicate(emailField.getText())) {
            errors.add("Email already exists");
            emailErrorLabel.setText("Already exists");
        }
        
        if (majorComboBox.getValue() == null || majorComboBox.getValue().trim().isEmpty()) {
            errors.add("Major is required");
            majorErrorLabel.setText("Required");
        }
        
        if (enrollmentDatePicker.getValue() == null) {
            errors.add("Enrollment date is required");
            enrollmentDateErrorLabel.setText("Required");
        } else if (enrollmentDatePicker.getValue().isAfter(LocalDate.now())) {
            errors.add("Enrollment date cannot be in the future");
            enrollmentDateErrorLabel.setText("Cannot be future date");
        }
        
        // Validate age
        try {
            int age = ageSpinner.getValue();
            if (age < 16 || age > 100) {
                errors.add("Age must be between 16 and 100");
                ageErrorLabel.setText("Invalid range");
            }
        } catch (Exception e) {
            errors.add("Age is invalid");
            ageErrorLabel.setText("Invalid");
        }
        
        // Validate GPA
        try {
            double gpa = gpaSpinner.getValue();
            if (gpa < 0.0 || gpa > 4.0) {
                errors.add("GPA must be between 0.0 and 4.0");
                gpaErrorLabel.setText("Invalid range");
            }
        } catch (Exception e) {
            errors.add("GPA is invalid");
            gpaErrorLabel.setText("Invalid");
        }
        
        // Validate phone (optional)
        if (phoneField.getText() != null && !phoneField.getText().trim().isEmpty()) {
            if (!isValidPhone(phoneField.getText())) {
                errors.add("Phone number format is invalid");
                phoneErrorLabel.setText("Invalid format");
            }
        }
        
        // Show validation summary if there are errors
        if (!errors.isEmpty()) {
            validationTextArea.setText(String.join("\\n", errors));
            validationSummary.setVisible(true);
            return false;
        }
        
        validationSummary.setVisible(false);
        return true;
    }
    
    /**
     * Save the student
     */
    private void saveStudent() {
        // Create or update student object
        Student student = isEditMode ? currentStudent : new Student();
        
        student.setFirstName(firstNameField.getText().trim());
        student.setLastName(lastNameField.getText().trim());
        student.setEmail(emailField.getText().trim());
        student.setPhoneNumber(phoneField.getText().trim());
        student.setAge(ageSpinner.getValue());
        student.setGpa(gpaSpinner.getValue());
        student.setMajor(majorComboBox.getValue());
        student.setEnrollmentDate(enrollmentDatePicker.getValue());
        
        // Save in background thread
        Task<Boolean> saveTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                if (isEditMode) {
                    return studentDAO.updateStudent(student);
                } else {
                    return studentDAO.addStudent(student);
                }
            }
            
            @Override
            protected void succeeded() {
                if (getValue()) {
                    Platform.runLater(() -> {
                        logger.info("Student saved successfully: {}", student);
                        if (parentController != null) {
                            parentController.onStudentSaved();
                        }
                        closeDialog();
                    });
                } else {
                    Platform.runLater(() -> {
                        showErrorAlert("Save Error", "Failed to save student", 
                                     "An error occurred while saving the student.");
                    });
                }
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    showErrorAlert("Save Error", "Failed to save student", getException().getMessage());
                });
            }
        };
        
        // Disable save button during save operation
        saveButton.setDisable(true);
        new Thread(saveTask).start();
    }
    
    /**
     * Validate individual field
     */
    private void validateField(TextField field, Label errorLabel, String errorMessage) {
        if (field.getText() == null || field.getText().trim().isEmpty()) {
            errorLabel.setText("Required");
        } else {
            errorLabel.setText("");
        }
    }
    
    /**
     * Validate email field
     */
    private void validateEmailField() {
        String email = emailField.getText();
        if (email == null || email.trim().isEmpty()) {
            emailErrorLabel.setText("Required");
        } else if (!isValidEmail(email)) {
            emailErrorLabel.setText("Invalid format");
        } else if (isEmailDuplicate(email)) {
            emailErrorLabel.setText("Already exists");
        } else {
            emailErrorLabel.setText("");
        }
    }
    
    /**
     * Validate phone field
     */
    private void validatePhoneField() {
        String phone = phoneField.getText();
        if (phone != null && !phone.trim().isEmpty() && !isValidPhone(phone)) {
            phoneErrorLabel.setText("Invalid format");
        } else {
            phoneErrorLabel.setText("");
        }
    }
    
    /**
     * Check if email is valid
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Check if phone is valid
     */
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[\\+]?[1-9]?[0-9]{7,15}$");
    }
    
    /**
     * Check if email is duplicate
     */
    private boolean isEmailDuplicate(String email) {
        if (studentDAO == null) return false;
        
        int excludeId = (isEditMode && currentStudent != null) ? currentStudent.getId() : -1;
        return studentDAO.emailExists(email, excludeId);
    }
    
    /**
     * Update academic status label
     */
    private void updateAcademicStatus() {
        if (gpaSpinner.getValue() != null) {
            Student tempStudent = new Student();
            tempStudent.setGpa(gpaSpinner.getValue());
            academicStatusLabel.setText(tempStudent.getAcademicStatus());
            
            // Color code the status
            double gpa = gpaSpinner.getValue();
            if (gpa >= 3.5) {
                academicStatusLabel.setStyle("-fx-text-fill: #28a745;"); // Green
            } else if (gpa >= 3.0) {
                academicStatusLabel.setStyle("-fx-text-fill: #007bff;"); // Blue
            } else if (gpa >= 2.0) {
                academicStatusLabel.setStyle("-fx-text-fill: #ffc107;"); // Yellow
            } else {
                academicStatusLabel.setStyle("-fx-text-fill: #dc3545;"); // Red
            }
        }
    }
    
    /**
     * Clear all error labels
     */
    private void clearErrorLabels() {
        firstNameErrorLabel.setText("");
        lastNameErrorLabel.setText("");
        emailErrorLabel.setText("");
        phoneErrorLabel.setText("");
        ageErrorLabel.setText("");
        enrollmentDateErrorLabel.setText("");
        majorErrorLabel.setText("");
        gpaErrorLabel.setText("");
    }
    
    /**
     * Clear the form
     */
    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        ageSpinner.getValueFactory().setValue(20);
        gpaSpinner.getValueFactory().setValue(3.0);
        majorComboBox.setValue("Computer Science");
        enrollmentDatePicker.setValue(LocalDate.now());
        updateAcademicStatus();
    }
    
    /**
     * Close the dialog
     */
    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Show error alert
     */
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        
        // Re-enable save button
        saveButton.setDisable(false);
    }
}