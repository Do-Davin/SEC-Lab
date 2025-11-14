# Student Management System - JavaFX MVC

A comprehensive **Student Management System** built using the **Model-View-Controller (MVC)** architectural pattern with **JavaFX**, **SQLite**, and **Maven**. This project demonstrates practical implementation of MVC principles in a desktop application environment.

## 🚀 Features

### Core Functionality
- ✅ **CRUD Operations**: Create, Read, Update, Delete student records
- 👥 **Student Management**: Complete student profile management
- 🔍 **Search & Filter**: Advanced search by name, email, or major
- 📊 **Statistics Dashboard**: Real-time student analytics and metrics
- 📤 **Data Export**: Export student data to CSV format

### Advanced Features
- 🎯 **Major Filtering**: Filter students by academic major
- 🏆 **Honor Students**: Quick access to high-performing students (GPA ≥ 3.5)
- 📈 **Academic Status**: Automatic calculation of academic standing
- 🔄 **Real-time Updates**: Live data refresh and synchronization
- 📱 **Responsive Design**: Adaptive UI that works on different screen sizes
- ⌨️ **Keyboard Navigation**: Full keyboard accessibility support

### MVC Architecture Benefits
- 🏗️ **Separation of Concerns**: Clear division between data, presentation, and logic
- 🧪 **Testability**: Each layer can be tested independently
- 🔧 **Maintainability**: Easy to modify and extend functionality
- 👥 **Team Development**: Multiple developers can work on different layers
- 🔄 **Reusability**: Components can be reused across different parts of the application

## 📁 Project Structure

```
student-management-javafx/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/studentmanager/
│   │   │       ├── model/                    # Data layer (Model)
│   │   │       │   ├── Student.java         # Student entity with business logic
│   │   │       │   └── StudentDAO.java      # Data access operations
│   │   │       ├── controller/               # Business logic layer (Controller)
│   │   │       │   ├── StudentController.java        # Main UI controller
│   │   │       │   └── StudentFormController.java    # Form dialog controller
│   │   │       └── StudentManagementApplication.java # Main application class
│   │   └── resources/                        # View layer and assets
│   │       ├── StudentManagement.fxml       # Main window view
│   │       ├── StudentForm.fxml             # Student form dialog view
│   │       └── css/
│   │           └── application.css          # Application styling
│   └── test/                                # Test classes
├── pom.xml                                  # Maven configuration
├── students.db                              # SQLite database (auto-created)
└── README.md                                # This file
```

## 🛠️ Installation & Setup

### Prerequisites
- **Java 17** or higher (OpenJDK or Oracle JDK)
- **Maven 3.6.0** or higher
- **Git** (for cloning the repository)

### Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd student-management-javafx
   ```

2. **Compile the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

   **Alternative run methods:**
   ```bash
   # Using Maven exec plugin
   mvn clean javafx:run
   
   # Using Java directly (after compilation)
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp target/classes com.example.studentmanager.StudentManagementApplication
   ```

### Building Executable JAR

```bash
# Create executable JAR with dependencies
mvn clean package

# Run the JAR file
java -jar target/student-management-javafx-1.0.0-shaded.jar
```

### Development Mode

For development with automatic recompilation:

```bash
# Use Maven in watch mode (if available)
mvn compile exec:java -Dexec.mainClass="com.example.studentmanager.StudentManagementApplication"
```

## 🎯 Usage Guide

### Application Overview

The application features a modern, intuitive interface divided into several key areas:

#### Main Window Components
1. **Header Section**: Application title, statistics, and main action buttons
2. **Search & Filter Bar**: Quick search and filtering options
3. **Student Table**: Comprehensive list of all students with sortable columns
4. **Action Buttons**: Context-sensitive buttons for student operations
5. **Status Bar**: Application status and last update information

### Basic Operations

#### Adding Students
1. Click **"Add New Student"** button in the header
2. Fill in the required information in the form dialog:
   - **Personal Information**: First name, last name, email, phone, age
   - **Academic Information**: Major, GPA, enrollment date
3. Click **"Save Student"** to add the student to the database

#### Managing Students
- **View Details**: Select a student and click "View Details" or double-click any row
- **Edit Student**: Select a student and click "Edit Selected" or double-click
- **Delete Student**: Select a student and click "Delete Selected" (with confirmation)

#### Search & Filter Operations
- **Text Search**: Use the search bar to find students by name, email, or major
- **Major Filter**: Use the dropdown to filter students by specific major
- **Honor Students**: Click "Honor Students" to show only students with GPA ≥ 3.5
- **Clear Filters**: Click "Clear" to reset all search and filter criteria

#### Data Management
- **Refresh Data**: Click "Refresh" to reload data from the database
- **Export Data**: Click "Export" to save current view as CSV file
- **Statistics**: View real-time statistics in the header dashboard

### Student Form Fields

#### Required Fields (marked with *)
- **First Name**: Student's first name
- **Last Name**: Student's last name  
- **Email**: Valid email address (must be unique)
- **Age**: Student age (16-100 years)
- **Major**: Academic major or field of study
- **GPA**: Grade Point Average (0.0-4.0 scale)
- **Enrollment Date**: Date when student enrolled

#### Optional Fields
- **Phone Number**: Contact phone number
- **Academic Status**: Automatically calculated based on GPA

### Academic Status Levels

The system automatically calculates academic status based on GPA:

| GPA Range | Academic Status | Color Code |
|-----------|----------------|------------|
| 3.7 - 4.0 | Summa Cum Laude | Green |
| 3.5 - 3.69 | Magna Cum Laude | Green |
| 3.3 - 3.49 | Cum Laude | Green |
| 3.0 - 3.29 | Dean's List | Blue |
| 2.0 - 2.99 | Good Standing | Yellow |
| 0.0 - 1.99 | Academic Probation | Red |

### Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| `Ctrl+N` | Add new student |
| `Ctrl+E` | Edit selected student |
| `Ctrl+D` | Delete selected student |
| `Ctrl+R` | Refresh data |
| `Ctrl+F` | Focus search field |
| `Ctrl+H` | Show honor students |
| `Delete` | Delete selected student |
| `Enter` | Edit selected student |
| `Escape` | Close dialog/clear selection |

## 🏗️ MVC Architecture Explained

### Model Layer (`/model`)
**Responsibility**: Data management, business logic, and validation

#### `Student.java`
- **Entity Class**: Represents a student with all attributes
- **Business Logic**: Methods for academic status calculation, validation
- **JavaFX Properties**: Observable properties for UI binding
- **Validation**: Email format, age range, GPA bounds checking

**Key Features**:
```java
// Business logic methods
public boolean isHonorStudent()           // GPA >= 3.5
public String getAcademicStatus()         // Calculate academic standing
public boolean isValidForGraduation()    // Check graduation eligibility
public int getYearsEnrolled()            // Calculate enrollment duration

// Validation methods
public boolean isValidEmail()             // Email format validation
public boolean isValidAge()               // Age range validation (16-100)
public boolean isValidGpa()               // GPA range validation (0.0-4.0)
```

#### `StudentDAO.java`
- **Data Access Object**: Handles all database operations
- **CRUD Operations**: Create, Read, Update, Delete students
- **Search Functionality**: Advanced search and filtering
- **Statistics**: Generate academic statistics and metrics

**Key Features**:
```java
// Core CRUD operations
public boolean addStudent(Student student)
public boolean updateStudent(Student student)
public boolean deleteStudent(Student student)
public ObservableList<Student> getAllStudents()

// Advanced search and filtering
public List<Student> searchStudents(String searchTerm)
public List<Student> getStudentsByMajor(String major)
public List<Student> getHonorStudents()

// Statistics and analytics
public StudentStatistics getStatistics()
public boolean emailExists(String email, int excludeId)
```

### View Layer (`/resources`)
**Responsibility**: User interface definition and presentation

#### `StudentManagement.fxml`
- **Main Window**: Primary application interface
- **Data Binding**: Connected to JavaFX TableView for student display
- **Layout Management**: Responsive design using BorderPane and VBox

**Key Components**:
- **Header Section**: Title, statistics dashboard, action buttons
- **Search/Filter Bar**: Text search and major filtering
- **Student Table**: Multi-column sortable table with custom cell factories
- **Status Bar**: Application status and timestamp display

#### `StudentForm.fxml`
- **Dialog Form**: Add/edit student information
- **Form Validation**: Real-time field validation with error display
- **Input Controls**: Text fields, spinners, combo boxes, date picker

**Key Features**:
- **Responsive Layout**: Grid-based form layout
- **Validation Summary**: Comprehensive error reporting
- **Academic Status Preview**: Real-time GPA status calculation

#### `application.css`
- **Modern Styling**: Professional appearance with consistent theming
- **Responsive Design**: Adaptive layouts for different screen sizes
- **Accessibility**: High contrast mode and keyboard navigation support

### Controller Layer (`/controller`)
**Responsibility**: User interaction handling and application flow coordination

#### `StudentController.java`
- **Main Controller**: Handles primary window interactions
- **Event Handling**: Button clicks, table selection, search operations
- **Data Coordination**: Manages communication between Model and View

**Key Responsibilities**:
```java
// Student management operations
public void handleAddStudent()      // Open add student dialog
public void handleEditStudent()     // Open edit student dialog  
public void handleDeleteStudent()   // Delete with confirmation
public void handleViewDetails()     // Show detailed student information

// Search and filtering
public void handleSearch()          // Text-based search
public void handleMajorFilter()     // Filter by academic major
public void handleShowHonorStudents() // Show high-performing students

// Data operations
public void handleRefresh()         // Reload data from database
public void handleExport()          // Export to CSV file
```

#### `StudentFormController.java`
- **Form Controller**: Manages student add/edit dialog
- **Validation Logic**: Real-time and submit validation
- **Data Binding**: Two-way binding between form fields and student object

**Key Features**:
```java
// Form operations
public void setStudent(Student student)  // Populate form for editing
public void handleSave()                 // Validate and save student
public void handleCancel()               // Close without saving
public void handleReset()                // Reset form to original state

// Validation
private boolean validateForm()           // Comprehensive form validation
private void validateEmailField()       // Real-time email validation
private void updateAcademicStatus()      // Live GPA status calculation
```

## 🎨 Design Features

### Modern UI/UX
- **Material Design Inspired**: Clean, modern interface with consistent styling
- **Color-Coded Elements**: Visual indicators for academic status and priority
- **Smooth Animations**: Hover effects and transitions for better user experience
- **Professional Layout**: Organized information hierarchy with proper spacing

### Data Visualization
- **Statistics Dashboard**: Real-time metrics display with visual indicators
- **Color-Coded GPA**: Visual representation of academic performance levels
- **Sortable Columns**: Click any table header to sort data
- **Status Indicators**: Clear visual feedback for all operations

### Accessibility Features
- **Keyboard Navigation**: Full application control via keyboard
- **High Contrast Mode**: Support for users with visual impairments
- **Screen Reader Support**: Proper ARIA labels and semantic structure
- **Focus Indicators**: Clear visual feedback for keyboard navigation

### Performance Optimizations
- **Lazy Loading**: Efficient data loading strategies
- **Background Tasks**: Non-blocking database operations
- **Memory Management**: Proper resource cleanup and garbage collection
- **Database Indexing**: Optimized queries for fast search operations

## 🔧 Configuration

### Database Configuration

The application uses SQLite with automatic database creation. The database file (`students.db`) is created in the project root directory on first run.

**Key Database Features**:
- **Auto-initialization**: Tables created automatically on startup
- **Sample Data**: Pre-populated with example students for demonstration
- **Indexes**: Optimized for search performance
- **Data Integrity**: Foreign key constraints and validation rules

**Database Schema**:
```sql
CREATE TABLE students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    age INTEGER NOT NULL,
    gpa REAL NOT NULL,
    major TEXT NOT NULL,
    phone_number TEXT,
    enrollment_date TEXT NOT NULL
);
```

### Application Properties

You can configure various application settings:

```properties
# Database Configuration
database.path=students.db
database.auto_create=true
database.sample_data=true

# UI Configuration  
ui.theme=default
ui.window.width=1200
ui.window.height=800
ui.window.resizable=true

# Performance Configuration
performance.background_tasks=true
performance.cache_enabled=true
performance.max_search_results=1000

# Logging Configuration
logging.level=INFO
logging.file=application.log
```

### Custom Themes

The application supports custom CSS themes. To create a custom theme:

1. Create a new CSS file in `src/main/resources/css/`
2. Override the root color variables
3. Modify the application to load your custom theme

Example custom theme:
```css
.root {
    -primary-color: #6f42c1;        /* Purple theme */
    -success-color: #20c997;        /* Teal accents */
    -card-background: #f8f9fa;      /* Light gray background */
}
```

## 🧪 Testing

### Manual Testing Checklist

#### Student Management Operations
- [ ] Add new student with all required fields
- [ ] Add student with only required fields (optional fields empty)
- [ ] Edit existing student information
- [ ] Delete student with confirmation dialog
- [ ] View detailed student information

#### Validation Testing
- [ ] Submit form with empty required fields
- [ ] Enter invalid email format
- [ ] Enter duplicate email address
- [ ] Enter age outside valid range (16-100)
- [ ] Enter GPA outside valid range (0.0-4.0)
- [ ] Enter invalid phone number format

#### Search and Filter Testing
- [ ] Search by first name
- [ ] Search by last name  
- [ ] Search by email address
- [ ] Search by major
- [ ] Filter by specific major
- [ ] Show honor students only
- [ ] Clear search and filters

#### Data Operations
- [ ] Export students to CSV file
- [ ] Refresh data from database
- [ ] Verify statistics accuracy
- [ ] Test with large dataset (performance)

#### UI/UX Testing
- [ ] Keyboard navigation throughout application
- [ ] Table sorting by different columns
- [ ] Window resizing and responsive layout
- [ ] Dialog opening and closing
- [ ] Error message display and clearing

### Automated Testing Framework

The project is set up for automated testing with JUnit 5 and TestFX:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=StudentDAOTest

# Run with coverage report
mvn test jacoco:report
```

**Test Categories**:
- **Unit Tests**: Model and business logic testing
- **Integration Tests**: Database operations testing
- **UI Tests**: User interface and interaction testing
- **Performance Tests**: Load and stress testing

## 🚀 Deployment

### Desktop Application Packaging

#### Using Maven Assembly Plugin
```bash
# Create distribution package
mvn clean package assembly:single

# Creates a ZIP file with all dependencies
ls target/student-management-javafx-1.0.0-bin.zip
```

#### Using jpackage (Java 14+)
```bash
# Create native installer
jpackage --input target/libs \
         --name "Student Management System" \
         --main-class com.example.studentmanager.StudentManagementApplication \
         --main-jar student-management-javafx-1.0.0.jar \
         --type msi  # or dmg for macOS, deb for Linux

# Creates platform-specific installer
ls *.msi
```

#### Cross-Platform Distribution
```bash
# Windows (from Windows machine)
jpackage --type msi --dest dist/windows

# macOS (from macOS machine)  
jpackage --type dmg --dest dist/macos

# Linux (from Linux machine)
jpackage --type deb --dest dist/linux
```

### System Requirements

#### Minimum Requirements
- **Operating System**: Windows 10, macOS 10.14, or Linux with GTK 3
- **Java**: Java 17 or higher
- **Memory**: 512 MB RAM
- **Storage**: 100 MB available space
- **Display**: 1024x768 minimum resolution

#### Recommended Requirements  
- **Operating System**: Latest versions of supported platforms
- **Java**: Java 21 LTS
- **Memory**: 1 GB RAM or higher
- **Storage**: 500 MB available space
- **Display**: 1920x1080 or higher resolution

## 🔄 Future Enhancements

### Planned Features
- [ ] **User Authentication**: Multi-user support with role-based access
- [ ] **Course Management**: Track student enrollments and grades
- [ ] **Reporting System**: Generate detailed academic reports
- [ ] **Data Import**: Import student data from CSV/Excel files
- [ ] **Photo Management**: Add student photos to profiles
- [ ] **Advanced Analytics**: Trend analysis and predictive modeling
- [ ] **Cloud Sync**: Synchronize data across multiple instances
- [ ] **Mobile Companion**: Companion mobile app for basic operations

### Technical Improvements
- [ ] **Database Migration**: Support for PostgreSQL/MySQL
- [ ] **REST API**: Web service API for external integration
- [ ] **Caching Layer**: Redis/Hazelcast for improved performance
- [ ] **Microservices**: Split into microservice architecture
- [ ] **Real-time Updates**: WebSocket-based live updates
- [ ] **Internationalization**: Multi-language support
- [ ] **Accessibility**: Enhanced screen reader support
- [ ] **Performance Monitoring**: Application metrics and monitoring

## 🤝 Contributing

### Development Workflow

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/student-photo-upload
   ```
3. **Make your changes** following the coding standards
4. **Write/update tests** for your changes
5. **Test thoroughly** across different platforms
6. **Commit with descriptive messages**
   ```bash
   git commit -m "Add: Student photo upload functionality
   
   - Add photo field to Student model
   - Create photo upload dialog
   - Update database schema
   - Add image display in student table"
   ```
7. **Push to your fork**
   ```bash
   git push origin feature/student-photo-upload
   ```
8. **Create a Pull Request** with detailed description

### Code Style Guidelines

#### Java Code Standards
- **Naming Conventions**: Use camelCase for variables/methods, PascalCase for classes
- **Documentation**: All public methods must have JavaDoc comments
- **Error Handling**: Use appropriate exception types with meaningful messages
- **Logging**: Use SLF4J with appropriate log levels
- **Constants**: Use static final fields for constants

#### FXML Guidelines
- **Structure**: Use proper nesting and indentation
- **Naming**: Use descriptive fx:id names
- **Layout**: Prefer constraint-based layouts over absolute positioning
- **Accessibility**: Include appropriate labels and tooltips

#### CSS Standards
- **Organization**: Group related styles together
- **Naming**: Use BEM methodology for class naming
- **Variables**: Use CSS custom properties for colors and sizes
- **Comments**: Document complex styling decisions

### Issue Reporting

When reporting bugs or requesting features:

1. **Search existing issues** to avoid duplicates
2. **Use descriptive titles** that summarize the issue
3. **Provide detailed descriptions** with steps to reproduce
4. **Include system information** (OS, Java version, etc.)
5. **Add screenshots or logs** when relevant
6. **Label appropriately** (bug, enhancement, question, etc.)

**Bug Report Template**:
```markdown
## Bug Description
Brief description of the issue

## Steps to Reproduce
1. Open the application
2. Navigate to student form
3. Enter invalid email format
4. Click save

## Expected Behavior
Should show validation error message

## Actual Behavior
Application crashes with NullPointerException

## Environment
- OS: Windows 11
- Java: OpenJDK 17.0.1
- JavaFX: 21.0.1
- Application Version: 1.0.0

## Additional Context
Include any relevant logs or screenshots
```

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 Student Management System

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 🙏 Acknowledgments

### Technologies Used
- **JavaFX** - Rich client application platform
- **SQLite** - Lightweight embedded database
- **Maven** - Project management and build automation
- **SLF4J + Logback** - Comprehensive logging framework
- **JUnit 5** - Modern testing framework for Java
- **TestFX** - JavaFX testing framework

### Design Inspiration
- **Material Design** - Google's design language
- **Human Interface Guidelines** - Apple's UI design principles
- **Microsoft Fluent Design** - Modern Windows application design

### Educational Resources
- **Oracle JavaFX Documentation** - Comprehensive JavaFX guides
- **Martin Fowler's Patterns** - Enterprise application architecture
- **Clean Code by Robert Martin** - Software craftsmanship principles

## 📞 Support

### Getting Help

- **Documentation**: Check this README and inline code documentation
- **Issues**: Create a GitHub issue for bugs or feature requests
- **Discussions**: Use GitHub Discussions for questions and ideas
- **Wiki**: Check the project wiki for additional documentation

### Contact Information

- **Project Maintainer**: [Your Name]
- **Email**: [your.email@example.com]
- **GitHub**: [@yourusername]
- **LinkedIn**: [Your LinkedIn Profile]

### Community

- **Discord Server**: [Invite Link] - Real-time chat and support
- **Stack Overflow**: Tag your questions with `student-management-javafx`
- **Reddit**: r/JavaFX - Community discussions and help

---

## 📚 Learning Resources

### JavaFX Development
- [Oracle JavaFX Documentation](https://openjfx.io/)
- [JavaFX Tutorial by Oracle](https://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.htm)
- [Scene Builder User Guide](https://docs.oracle.com/javase/8/scene-builder-2/user-guide/jsbpub-user_guide.htm)

### MVC Pattern
- [MVC Architecture Pattern](https://developer.mozilla.org/en-US/docs/Glossary/MVC)
- [Martin Fowler - GUI Architectures](https://martinfowler.com/eaaDev/uiArchs.html)
- [MVC vs MVP vs MVVM](https://levelup.gitconnected.com/mvc-vs-mvp-vs-mvvm-35e0d4b933b4)

### Database Design
- [SQLite Documentation](https://sqlite.org/docs.html)
- [Database Design Best Practices](https://database.guide/database-design-best-practices/)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)

### Software Architecture
- [Clean Architecture by Robert Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Effective Java by Joshua Bloch](https://www.oracle.com/java/technologies/javase/effectivejava.html)
- [Design Patterns: Elements of Reusable Object-Oriented Software](https://en.wikipedia.org/wiki/Design_Patterns)

---

**Built with ❤️ using the MVC architectural pattern in JavaFX**

*This project serves as both a functional student management application and an educational resource for learning MVC architecture principles in desktop application development.*