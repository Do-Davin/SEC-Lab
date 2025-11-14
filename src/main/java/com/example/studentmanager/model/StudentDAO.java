package com.example.studentmanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Student Data Access Object (DAO)
 * Handles all database operations for Student entities
 * Part of the Model layer in MVC architecture
 */
public class StudentDAO {
    private static final Logger logger = LoggerFactory.getLogger(StudentDAO.class);
    private static final String DB_URL = "jdbc:sqlite:students.db";
    private ObservableList<Student> students;

    public StudentDAO() {
        students = FXCollections.observableArrayList();
        initializeDatabase();
        loadStudents();
    }

    /**
     * Initialize the database and create tables if they don't exist
     */
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String createTable = """
                CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    age INTEGER NOT NULL,
                    gpa REAL NOT NULL,
                    major TEXT NOT NULL,
                    phone_number TEXT,
                    enrollment_date TEXT NOT NULL
                )
                """;
            
            Statement stmt = conn.createStatement();
            stmt.execute(createTable);
            
            // Create index for better search performance
            String createEmailIndex = "CREATE INDEX IF NOT EXISTS idx_student_email ON students(email)";
            String createNameIndex = "CREATE INDEX IF NOT EXISTS idx_student_name ON students(last_name, first_name)";
            String createMajorIndex = "CREATE INDEX IF NOT EXISTS idx_student_major ON students(major)";
            
            stmt.execute(createEmailIndex);
            stmt.execute(createNameIndex);
            stmt.execute(createMajorIndex);
            
            // Insert sample data if table is empty
            insertSampleDataIfEmpty(conn);
            
            logger.info("Database initialized successfully");
        } catch (SQLException e) {
            logger.error("Error initializing database", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    /**
     * Insert sample data if the database is empty
     */
    private void insertSampleDataIfEmpty(Connection conn) throws SQLException {
        String countQuery = "SELECT COUNT(*) FROM students";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(countQuery);
        
        if (rs.next() && rs.getInt(1) == 0) {
            logger.info("Inserting sample data...");
            
            String insertSample = """
                INSERT INTO students (first_name, last_name, email, age, gpa, major, phone_number, enrollment_date) VALUES
                ('John', 'Doe', 'john.doe@university.edu', 20, 3.7, 'Computer Science', '+1234567890', '2022-09-01'),
                ('Jane', 'Smith', 'jane.smith@university.edu', 19, 3.9, 'Mathematics', '+1234567891', '2023-09-01'),
                ('Mike', 'Johnson', 'mike.johnson@university.edu', 21, 3.2, 'Physics', '+1234567892', '2021-09-01'),
                ('Sarah', 'Williams', 'sarah.williams@university.edu', 20, 3.8, 'Biology', '+1234567893', '2022-09-01'),
                ('David', 'Brown', 'david.brown@university.edu', 22, 3.1, 'Chemistry', '+1234567894', '2020-09-01'),
                ('Emily', 'Davis', 'emily.davis@university.edu', 19, 4.0, 'Computer Science', '+1234567895', '2023-09-01'),
                ('Robert', 'Miller', 'robert.miller@university.edu', 21, 2.8, 'History', '+1234567896', '2021-09-01'),
                ('Lisa', 'Wilson', 'lisa.wilson@university.edu', 20, 3.6, 'English', '+1234567897', '2022-09-01')
                """;
            
            stmt.execute(insertSample);
            logger.info("Sample data inserted successfully");
        }
    }

    /**
     * Get all students as an observable list for UI binding
     */
    public ObservableList<Student> getAllStudents() {
        return students;
    }

    /**
     * Add a new student to the database
     */
    public boolean addStudent(Student student) {
        String sql = """
            INSERT INTO students (first_name, last_name, email, age, gpa, major, phone_number, enrollment_date) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getEmail());
            pstmt.setInt(4, student.getAge());
            pstmt.setDouble(5, student.getGpa());
            pstmt.setString(6, student.getMajor());
            pstmt.setString(7, student.getPhoneNumber());
            pstmt.setString(8, student.getEnrollmentDate() != null ? 
                           student.getEnrollmentDate().toString() : LocalDate.now().toString());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    student.setId(rs.getInt(1));
                    students.add(student);
                    logger.info("Student added successfully: {}", student);
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("Error adding student: {}", student, e);
        }
        return false;
    }

    /**
     * Update an existing student in the database
     */
    public boolean updateStudent(Student student) {
        String sql = """
            UPDATE students SET first_name=?, last_name=?, email=?, age=?, gpa=?, 
            major=?, phone_number=?, enrollment_date=? WHERE id=?
            """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getEmail());
            pstmt.setInt(4, student.getAge());
            pstmt.setDouble(5, student.getGpa());
            pstmt.setString(6, student.getMajor());
            pstmt.setString(7, student.getPhoneNumber());
            pstmt.setString(8, student.getEnrollmentDate() != null ? 
                           student.getEnrollmentDate().toString() : LocalDate.now().toString());
            pstmt.setInt(9, student.getId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Student updated successfully: {}", student);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error updating student: {}", student, e);
        }
        return false;
    }

    /**
     * Delete a student from the database
     */
    public boolean deleteStudent(Student student) {
        String sql = "DELETE FROM students WHERE id=?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, student.getId());
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                students.remove(student);
                logger.info("Student deleted successfully: {}", student);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error deleting student: {}", student, e);
        }
        return false;
    }

    /**
     * Search students by various criteria
     */
    public List<Student> searchStudents(String searchTerm) {
        List<Student> results = new ArrayList<>();
        String sql = """
            SELECT * FROM students 
            WHERE first_name LIKE ? OR last_name LIKE ? OR email LIKE ? OR major LIKE ?
            ORDER BY last_name, first_name
            """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm.toLowerCase() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(createStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error searching students with term: {}", searchTerm, e);
        }
        
        return results;
    }

    /**
     * Get students by major
     */
    public List<Student> getStudentsByMajor(String major) {
        List<Student> results = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE major = ? ORDER BY gpa DESC, last_name, first_name";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, major);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                results.add(createStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error getting students by major: {}", major, e);
        }
        
        return results;
    }

    /**
     * Get honor students (GPA >= 3.5)
     */
    public List<Student> getHonorStudents() {
        List<Student> results = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE gpa >= 3.5 ORDER BY gpa DESC, last_name, first_name";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                results.add(createStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error getting honor students", e);
        }
        
        return results;
    }

    /**
     * Get statistics about students
     */
    public StudentStatistics getStatistics() {
        String sql = """
            SELECT 
                COUNT(*) as total_count,
                AVG(gpa) as avg_gpa,
                MIN(gpa) as min_gpa,
                MAX(gpa) as max_gpa,
                COUNT(CASE WHEN gpa >= 3.5 THEN 1 END) as honor_count,
                AVG(age) as avg_age
            FROM students
            """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return new StudentStatistics(
                    rs.getInt("total_count"),
                    rs.getDouble("avg_gpa"),
                    rs.getDouble("min_gpa"),
                    rs.getDouble("max_gpa"),
                    rs.getInt("honor_count"),
                    rs.getDouble("avg_age")
                );
            }
        } catch (SQLException e) {
            logger.error("Error getting statistics", e);
        }
        
        return new StudentStatistics(0, 0.0, 0.0, 0.0, 0, 0.0);
    }

    /**
     * Load all students from database into the observable list
     */
    private void loadStudents() {
        students.clear();
        String sql = "SELECT * FROM students ORDER BY last_name, first_name";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(createStudentFromResultSet(rs));
            }
            
            logger.info("Loaded {} students from database", students.size());
        } catch (SQLException e) {
            logger.error("Error loading students from database", e);
        }
    }

    /**
     * Helper method to create Student object from ResultSet
     */
    private Student createStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setEmail(rs.getString("email"));
        student.setAge(rs.getInt("age"));
        student.setGpa(rs.getDouble("gpa"));
        student.setMajor(rs.getString("major"));
        student.setPhoneNumber(rs.getString("phone_number"));
        
        String enrollmentDateStr = rs.getString("enrollment_date");
        if (enrollmentDateStr != null) {
            student.setEnrollmentDate(LocalDate.parse(enrollmentDateStr));
        }
        
        return student;
    }

    /**
     * Refresh the students list from database
     */
    public void refreshStudents() {
        loadStudents();
    }

    /**
     * Check if email already exists (for validation)
     */
    public boolean emailExists(String email, int excludeId) {
        String sql = "SELECT COUNT(*) FROM students WHERE email = ? AND id != ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email.toLowerCase());
            pstmt.setInt(2, excludeId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking email existence: {}", email, e);
        }
        
        return false;
    }

    /**
     * Inner class for student statistics
     */
    public static class StudentStatistics {
        private final int totalCount;
        private final double averageGpa;
        private final double minGpa;
        private final double maxGpa;
        private final int honorStudentCount;
        private final double averageAge;

        public StudentStatistics(int totalCount, double averageGpa, double minGpa, 
                               double maxGpa, int honorStudentCount, double averageAge) {
            this.totalCount = totalCount;
            this.averageGpa = averageGpa;
            this.minGpa = minGpa;
            this.maxGpa = maxGpa;
            this.honorStudentCount = honorStudentCount;
            this.averageAge = averageAge;
        }

        // Getters
        public int getTotalCount() { return totalCount; }
        public double getAverageGpa() { return averageGpa; }
        public double getMinGpa() { return minGpa; }
        public double getMaxGpa() { return maxGpa; }
        public int getHonorStudentCount() { return honorStudentCount; }
        public double getAverageAge() { return averageAge; }
        public double getHonorStudentPercentage() { 
            return totalCount > 0 ? (honorStudentCount * 100.0 / totalCount) : 0.0; 
        }
    }
}