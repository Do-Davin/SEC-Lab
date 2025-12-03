package com.example;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * UserController class that acts as the Controller in the MVVM pattern.
 * This class handles the interaction between the View (FXML) and the ViewModel.
 */
public class StudentController implements Initializable {
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker birthDatePicker;
    
    @FXML
    private Label fullNameLabel;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private ListView<Student> studentListView;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Label studentCountLabel;

    @FXML
    private ComboBox<String> sortCriteriaComboBox;

    @FXML
    private CheckBox sortAscendingCheckBox;

    private StudentViewModel viewModel;

    public StudentController() {
        this.viewModel = new StudentViewModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupDataBinding();
        setupEventHandlers();
        populateSortingOptions();
        setupSortingBinding();
        setupListView();
    }

    /**
     * Sets up the data binding between View controls and ViewModel properties.
     * This is the key aspect of the MVVM pattern in JavaFX.
     */
    private void setupDataBinding() {
        // Bidirectional binding for text fields
        firstNameField.textProperty().bindBidirectional(viewModel.firstNameProperty());
        lastNameField.textProperty().bindBidirectional(viewModel.lastNameProperty());
        emailField.textProperty().bindBidirectional(viewModel.emailProperty());
        birthDatePicker.valueProperty().bindBidirectional(viewModel.birthDateProperty());

        
        // Bind full name label to computed property from ViewModel
        fullNameLabel.textProperty().bind(Bindings.createStringBinding(
            () -> {
                String fullName = viewModel.getFullName();
                return fullName.isEmpty() ? "Full Name: " : "Full Name: " + fullName;
            },
            viewModel.firstNameProperty(),
            viewModel.lastNameProperty()
        ));
        
        // Bind save button enabled state to form validation
        saveButton.disableProperty().bind(Bindings.createBooleanBinding(
            () -> !viewModel.isValidStudent(),
            viewModel.firstNameProperty(),
            viewModel.lastNameProperty(),
            viewModel.emailProperty(),
            viewModel.birthDateProperty()
        ));
        
        // Bind delete button enabled state to list selection
        deleteButton.disableProperty().bind(
            studentListView.getSelectionModel().selectedItemProperty().isNull()
        );
        
        // Bind user count label
        studentCountLabel.textProperty().bind(Bindings.createStringBinding(
            () -> "Total Students: " + viewModel.getStudentCount(),
            viewModel.getStudents()
        ));
    }

    /**
     * Sets up event handlers for buttons and other controls.
     */
    private void setupEventHandlers() {
        // Save button handler
        saveButton.setOnAction(event -> handleSaveStudent());
        
        // Clear button handler
        clearButton.setOnAction(event -> handleClearForm());
        
        // Delete button handler
        deleteButton.setOnAction(event -> handleDeleteStudent());
        
        // Double-click on list item to edit
        studentListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Student selectedStudent = studentListView.getSelectionModel().getSelectedItem();
                if (selectedStudent != null) {
                    handleEditStudent(selectedStudent);
                }
            }
        });
    }

    /**
     * Sets up the ListView to display users from the ViewModel.
     */
    private void setupListView() {
        studentListView.setItems(viewModel.getStudents());
        
        // Add listener to update UI when list changes
        viewModel.getStudents().addListener((ListChangeListener<Student>) change -> {
            // This will trigger the binding update for user count
        });
    }

    /**
     * Handles the Save User button action.
     * This method is referenced in the FXML file.
     */
    @FXML
    private void handleSaveStudent() {
        if (viewModel.isValidStudent()) {

            viewModel.addStudent(
                sortCriteriaComboBox.getValue(),
                sortAscendingCheckBox.isSelected()
            );

            System.out.println("Student Saved: " + viewModel.getFullName());
            showSuccessMessage("Student saved successfully!");
        }
    }

    /**
     * Handles the Clear Form button action.
     */
    @FXML
    private void handleClearForm() {
        viewModel.clearForm();
        studentListView.getSelectionModel().clearSelection();
        birthDatePicker.setValue(null);
        System.out.println("Form cleared");
    }

    /**
     * Handles the Delete User button action.
     */
    @FXML
    private void handleDeleteStudent() {
        Student selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            viewModel.removeStudent(selectedStudent);
            System.out.println("Student deleted: " + selectedStudent.getFullName());
            showSuccessMessage("Student deleted successfully!");
        }
    }

    /**
     * Handles editing a user by populating the form with their data.
     */
    private void handleEditStudent(Student student) {
        if (student != null) {
            viewModel.setFirstName(student.getFirstName());
            viewModel.setLastName(student.getLastName());
            viewModel.setEmail(student.getEmail());
            viewModel.setBirthDate(student.getBirthDate());
            
            // Remove the user from the list so it can be re-added when saved
            viewModel.removeStudent(student);
            
            System.out.println("Editing student: " + student.getFullName());
            viewModel.sortStudents(
                    sortCriteriaComboBox.getValue(),
                    sortAscendingCheckBox.isSelected()
            );
        }
    }

    /**
     * Shows a success message (in a real application, this might show a toast or status bar message).
     */
    private void showSuccessMessage(String message) {
        // In a real application, you might show this in a status bar, toast, or dialog
        System.out.println("Success: " + message);
    }

    /**
     * Gets the ViewModel instance (useful for testing or external access).
     */
    public StudentViewModel getViewModel() {
        return viewModel;
    }

    /**
     * Sets a custom ViewModel (useful for testing or dependency injection).
     */
    public void setViewModel(StudentViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private void populateSortingOptions() {
            sortCriteriaComboBox.getItems().addAll(
                    "First Name",
                    "Last Name",
                    "Email",
                    "Birthdate"
            );
            sortCriteriaComboBox.getSelectionModel().selectFirst();

            // default ascending = true
            sortAscendingCheckBox.setSelected(true);
        }

        private void setupSortingBinding() {
        sortCriteriaComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            viewModel.sortStudents(
                    newVal,
                    sortAscendingCheckBox.isSelected()
            );
        });

        sortAscendingCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            viewModel.sortStudents(
                    sortCriteriaComboBox.getValue(),
                    newVal
            );
        });
    }
}