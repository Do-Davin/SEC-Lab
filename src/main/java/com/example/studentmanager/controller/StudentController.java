package com.example.studentmanager.controller;

import com.example.studentmanager.model.Student;
import com.example.studentmanager.model.StudentDAO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Main Controller for Student Management System
 * Handles the primary UI interactions and coordinates between Model and View
 * Part of the Controller layer in MVC architecture
 */
public class StudentController implements Initializable {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    
    // FXML injected controls
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> firstNameColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, Integer> ageColumn;
    @FXML private TableColumn<Student, Double> gpaColumn;
    @FXML private TableColumn<Student, String> majorColumn;
    @FXML private TableColumn<Student, String> phoneColumn;
    @FXML private TableColumn<Student, String> enrollmentDateColumn;
    @FXML private TableColumn<Student, String> statusColumn;
    
    @FXML private TextField searchField;
    @FXML private ComboBox<String> majorFilterComboBox;
    
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button viewDetailsButton;
    @FXML private Button refreshButton;
    @FXML private Button exportButton;
    @FXML private Button clearSearchButton;
    @FXML private Button showHonorButton;
    
    @FXML private Label totalStudentsLabel;
    @FXML private Label averageGpaLabel;
    @FXML private Label honorStudentsLabel;
    @FXML private Label honorPercentageLabel;
    @FXML private Label selectedStudentLabel;
    @FXML private Label statusLabel;
    @FXML private Label lastUpdatedLabel;

    // Model layer
    private StudentDAO studentDAO;
    private FilteredList<Student> filteredStudents;
    private SortedList<Student> sortedStudents;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing StudentController");
        
        // Initialize the data access layer
        initializeDAO();
        
        // Setup table columns
        setupTableColumns();
        
        // Setup table data and filtering
        setupTableData();
        
        // Setup event handlers
        setupEventHandlers();
        
        // Setup major filter combo box
        setupMajorFilter();
        
        // Update statistics
        updateStatistics();
        
        // Update button states
        updateButtonStates();
        
        // Set initial status
        updateStatusBar("Application loaded successfully");
        
        logger.info("StudentController initialized successfully");
    }
    
    /**
     * Initialize the Data Access Object
     */
    private void initializeDAO() {
        try {
            studentDAO = new StudentDAO();
            logger.info("StudentDAO initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize StudentDAO", e);
            showErrorAlert("Database Error", "Failed to initialize database connection", e.getMessage());
        }
    }
    
    /**
     * Setup table column bindings
     */
    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        gpaColumn.setCellValueFactory(new PropertyValueFactory<>("gpa"));
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        
        // Custom cell value factory for enrollment date
        enrollmentDateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getEnrollmentDate() != null) {
                return new SimpleStringProperty(cellData.getValue().getEnrollmentDate().toString());
            }
            return new SimpleStringProperty("");
        });
        
        // Custom cell value factory for academic status
        statusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getAcademicStatus()));
        
        // Format GPA column to show 2 decimal places
        gpaColumn.setCellFactory(column -> new TableCell<Student, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                    
                    // Color code based on GPA
                    if (item >= 3.5) {
                        setStyle("-fx-text-fill: #28a745;"); // Green for honor students
                    } else if (item >= 3.0) {
                        setStyle("-fx-text-fill: #007bff;"); // Blue for good standing
                    } else if (item >= 2.0) {
                        setStyle("-fx-text-fill: #ffc107;"); // Yellow for warning
                    } else {
                        setStyle("-fx-text-fill: #dc3545;"); // Red for probation
                    }
                }
            }
        });
        
        // Make ID column not resizable and smaller
        idColumn.setResizable(false);
        idColumn.setPrefWidth(60);
    }
    
    /**
     * Setup table data and filtering
     */
    private void setupTableData() {
        // Create filtered list
        filteredStudents = new FilteredList<>(studentDAO.getAllStudents());
        
        // Create sorted list
        sortedStudents = new SortedList<>(filteredStudents);
        sortedStudents.comparatorProperty().bind(studentTable.comparatorProperty());
        
        // Set the table items
        studentTable.setItems(sortedStudents);
        
        // Setup search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredStudents.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                return student.getFirstName().toLowerCase().contains(lowerCaseFilter) ||
                       student.getLastName().toLowerCase().contains(lowerCaseFilter) ||
                       student.getEmail().toLowerCase().contains(lowerCaseFilter) ||
                       student.getMajor().toLowerCase().contains(lowerCaseFilter);
            });
            updateStatistics();
        });
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Table selection handler
        studentTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                updateButtonStates();
                updateSelectedStudentLabel(newValue);
            });
        
        // Double-click to edit
        studentTable.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    handleEditStudent();
                }
            });
            return row;
        });
    }
    
    /**
     * Setup major filter combo box
     */
    private void setupMajorFilter() {
        // Get unique majors from all students
        List<String> majors = studentDAO.getAllStudents().stream()
            .map(Student::getMajor)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        
        majors.add(0, "All Majors");
        majorFilterComboBox.setItems(FXCollections.observableArrayList(majors));
        majorFilterComboBox.setValue("All Majors");
    }

    /**
     * Setup domain name filter combo box
     */
    
    /**
     * Handle add new student
     */
    @FXML
    private void handleAddStudent() {
        logger.info("Opening add student dialog");
        openStudentForm(null, "Add New Student");
    }
    
    /**
     * Handle edit selected student
     */
    @FXML
    private void handleEditStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            logger.info("Opening edit dialog for student: {}", selectedStudent);
            openStudentForm(selectedStudent, "Edit Student");
        }
    }
    
    /**
     * Handle delete selected student
     */
    @FXML
    private void handleDeleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            // Confirm deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Student");
            alert.setContentText("Are you sure you want to delete " + selectedStudent.getFullName() + "?\\n" +
                                "This action cannot be undone.");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Perform deletion in background thread
                Task<Boolean> deleteTask = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return studentDAO.deleteStudent(selectedStudent);
                    }
                    
                    @Override
                    protected void succeeded() {
                        if (getValue()) {
                            Platform.runLater(() -> {
                                updateStatistics();
                                updateStatusBar("Student deleted successfully");
                                logger.info("Student deleted: {}", selectedStudent);
                            });
                        } else {
                            Platform.runLater(() -> {
                                showErrorAlert("Delete Error", "Failed to delete student", 
                                             "An error occurred while deleting the student.");
                            });
                        }
                    }
                    
                    @Override
                    protected void failed() {
                        Platform.runLater(() -> {
                            showErrorAlert("Delete Error", "Failed to delete student", getException().getMessage());
                        });
                    }
                };
                
                new Thread(deleteTask).start();
            }
        }
    }
    
    /**
     * Handle view student details
     */
    @FXML
    private void handleViewDetails() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            showStudentDetails(selectedStudent);
        }
    }
    
    /**
     * Handle refresh data
     */
    @FXML
    private void handleRefresh() {
        logger.info("Refreshing student data");
        
        Task<Void> refreshTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                studentDAO.refreshStudents();
                return null;
            }
            
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    setupMajorFilter();
                    updateStatistics();
                    updateStatusBar("Data refreshed successfully");
                    logger.info("Student data refreshed");
                });
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    showErrorAlert("Refresh Error", "Failed to refresh data", getException().getMessage());
                });
            }
        };
        
        new Thread(refreshTask).start();
    }
    
    /**
     * Handle export to CSV
     */
    @FXML
    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Students to CSV");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("students_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv");
        
        Stage stage = (Stage) exportButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            exportToCSV(file);
        }
    }
    
    /**
     * Handle clear search
     */
    @FXML
    private void handleClearSearch() {
        searchField.clear();
        majorFilterComboBox.setValue("All Majors");
        filteredStudents.setPredicate(null);
        updateStatistics();
        updateStatusBar("Search cleared");
    }
    
    /**
     * Handle search
     */
    @FXML
    private void handleSearch() {
        // Search is handled automatically by the text property listener
        updateStatistics();
    }
    
    /**
     * Handle major filter
     */
    @FXML
    private void handleMajorFilter() {
        String selectedMajor = majorFilterComboBox.getValue();
        if (selectedMajor == null || selectedMajor.equals("All Majors")) {
            filteredStudents.setPredicate(student -> {
                String searchText = searchField.getText();
                if (searchText == null || searchText.isEmpty()) {
                    return true;
                }
                return matchesSearchCriteria(student, searchText);
            });
        } else {
            filteredStudents.setPredicate(student -> {
                boolean majorMatches = student.getMajor().equals(selectedMajor);
                String searchText = searchField.getText();
                if (searchText == null || searchText.isEmpty()) {
                    return majorMatches;
                }
                return majorMatches && matchesSearchCriteria(student, searchText);
            });
        }
        updateStatistics();
        updateStatusBar("Filtered by major: " + selectedMajor);
    }
    
    /**
     * Handle show honor students
     */
    @FXML
    private void handleShowHonorStudents() {
        filteredStudents.setPredicate(Student::isHonorStudent);
        searchField.clear();
        majorFilterComboBox.setValue("All Majors");
        updateStatistics();
        updateStatusBar("Showing honor students only (GPA ≥ 3.5)");
    }
    
    /**
     * Helper method to check if student matches search criteria
     */
    private boolean matchesSearchCriteria(Student student, String searchText) {
        String lowerCaseFilter = searchText.toLowerCase();
        return student.getFirstName().toLowerCase().contains(lowerCaseFilter) ||
               student.getLastName().toLowerCase().contains(lowerCaseFilter) ||
               student.getEmail().toLowerCase().contains(lowerCaseFilter) ||
               student.getMajor().toLowerCase().contains(lowerCaseFilter);
    }
    
    /**
     * Open student form for add/edit operations
     */
    private void openStudentForm(Student student, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StudentForm.fxml"));
            Parent root = loader.load();
            
            StudentFormController controller = loader.getController();
            controller.setStudentDAO(studentDAO);
            controller.setParentController(this);
            
            if (student != null) {
                controller.setStudent(student);
            }
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addButton.getScene().getWindow());
            stage.setResizable(false);
            
            stage.showAndWait();
            
        } catch (IOException e) {
            logger.error("Failed to open student form", e);
            showErrorAlert("Form Error", "Failed to open student form", e.getMessage());
        }
    }
    
    /**
     * Show student details in a dialog
     */
    private void showStudentDetails(Student student) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Student Details");
        alert.setHeaderText(student.getFullName());
        
        String details = String.format(
            "ID: %d\\n" +
            "Name: %s %s\\n" +
            "Email: %s\\n" +
            "Phone: %s\\n" +
            "Age: %d\\n" +
            "Major: %s\\n" +
            "GPA: %.2f\\n" +
            "Academic Status: %s\\n" +
            "Enrollment Date: %s\\n" +
            "Years Enrolled: %d\\n" +
            "Honor Student: %s\\n" +
            "Eligible for Graduation: %s",
            student.getId(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            student.getPhoneNumber().isEmpty() ? "Not provided" : student.getPhoneNumber(),
            student.getAge(),
            student.getMajor(),
            student.getGpa(),
            student.getAcademicStatus(),
            student.getEnrollmentDate() != null ? student.getEnrollmentDate().toString() : "Not set",
            student.getYearsEnrolled(),
            student.isHonorStudent() ? "Yes" : "No",
            student.isValidForGraduation() ? "Yes" : "No"
        );
        
        alert.setContentText(details);
        alert.showAndWait();
    }
    
    /**
     * Export students to CSV file
     */
    private void exportToCSV(File file) {
        Task<Void> exportTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try (FileWriter writer = new FileWriter(file)) {
                    // Write CSV header
                    writer.append("ID,First Name,Last Name,Email,Age,GPA,Major,Phone,Enrollment Date,Academic Status\\n");
                    
                    // Write student data
                    for (Student student : sortedStudents) {
                        writer.append(String.format("%d,\\\"%s\\\",\\\"%s\\\",\\\"%s\\\",%d,%.2f,\\\"%s\\\",\\\"%s\\\",\\\"%s\\\",\\\"%s\\\"\\n",
                            student.getId(),
                            student.getFirstName(),
                            student.getLastName(),
                            student.getEmail(),
                            student.getAge(),
                            student.getGpa(),
                            student.getMajor(),
                            student.getPhoneNumber(),
                            student.getEnrollmentDate() != null ? student.getEnrollmentDate().toString() : "",
                            student.getAcademicStatus()
                        ));
                    }
                }
                return null;
            }
            
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    updateStatusBar("Students exported to " + file.getName());
                    logger.info("Students exported to {}", file.getAbsolutePath());
                });
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    showErrorAlert("Export Error", "Failed to export students", getException().getMessage());
                });
            }
        };
        
        new Thread(exportTask).start();
    }
    
    /**
     * Update statistics labels
     */
    private void updateStatistics() {
        StudentDAO.StudentStatistics stats = studentDAO.getStatistics();
        
        totalStudentsLabel.setText(String.valueOf(filteredStudents.size()));
        
        if (stats.getTotalCount() > 0) {
            averageGpaLabel.setText(String.format("%.2f", stats.getAverageGpa()));
            honorStudentsLabel.setText(String.valueOf(stats.getHonorStudentCount()));
            honorPercentageLabel.setText(String.format("%.1f%%", stats.getHonorStudentPercentage()));
        } else {
            averageGpaLabel.setText("0.00");
            honorStudentsLabel.setText("0");
            honorPercentageLabel.setText("0%");
        }
    }
    
    /**
     * Update button states based on selection
     */
    private void updateButtonStates() {
        boolean hasSelection = studentTable.getSelectionModel().getSelectedItem() != null;
        editButton.setDisable(!hasSelection);
        deleteButton.setDisable(!hasSelection);
        viewDetailsButton.setDisable(!hasSelection);
    }
    
    /**
     * Update selected student label
     */
    private void updateSelectedStudentLabel(Student student) {
        if (student != null) {
            selectedStudentLabel.setText(student.getFullName() + " (" + student.getMajor() + ")");
        } else {
            selectedStudentLabel.setText("None");
        }
    }
    
    /**
     * Update status bar
     */
    private void updateStatusBar(String message) {
        statusLabel.setText(message);
        lastUpdatedLabel.setText("Last updated: " + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    /**
     * Show error alert dialog
     */
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Called by form controller when student is saved
     */
    public void onStudentSaved() {
        updateStatistics();
        setupMajorFilter();
        updateStatusBar("Student saved successfully");
    }
}