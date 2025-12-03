# 📘 Student Management System (JavaFX + MVVM)
## 📌 Overview

This project is a Student Management System built using JavaFX (FXML) and the MVVM architecture.

It supports:

* Add, edit, delete student
* Real-time search
* Sorting by First Name / Last Name / Email / Birthdate
* Beautiful ListView table layout
* JavaFX DatePicker for birthdate
* Live form validation
* Clean separation of View, ViewModel, Controller

This project runs on Java 17 and JavaFX 21.

---

# 🚀 How to Run the Application
## 1. Requirements
* Java JDK 17
* Maven 3.8+
* A system with:
   * macOS (M1/M2/M3/M4/M5 chips or Intel)
   * Windows 10 / 11
   * Linux (Ubuntu / Arch / Debian)
   
---

# 🍎 2. Running on macOS (M1/M2/M3/M4/M5 chips)
You don’t need to change anything — the pom.xml is already configured for mac-aarch64.

# 📌 Run using JavaFX Maven Plugin:
```sh
mvn javafx:run
```
If you get a PATH error:
```sh
export PATH_TO_FX=/Users/yourname/Downloads/javafx-sdk-21/lib
```
Then run:
```sh
mvn javafx:run -f pom.xml
```

---

# 🪟 3. How Windows Users Should Modify pom.xml
Windows uses a different JavaFX classifier:
## 🔄 Change THIS (your version for macOS)
```xml
<classifier>mac-aarch64</classifier>
```
## ➜ TO THIS (for Windows)
```xml
<classifier>win</classifier>
```

## ✔ Example (Windows-friendly dependency)
```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>${javafx.version}</version>
    <classifier>win</classifier>
</dependency>

<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>${javafx.version}</version>
    <classifier>win</classifier>
</dependency>
```

---

# 4. Full List of Classifiers (for all platforms)

| OS / Chip                            | Classifier    |
| ------------------------------------ | ------------- |
| macOS Apple Silicon (M1/M2/M3/M4/M5) | `mac-aarch64` |
| macOS Intel                          | `mac`         |
| Windows 10/11                        | `win`         |
| Linux (Ubuntu / Debian)              | `linux`       |

If someone clones your repo, they change only these two lines.

---

# 🏃 5. Windows Run Command
After switching the classifier:
```sh
mvn javafx:run
```
If JavaFX SDK is installed manually:
```sh
set PATH_TO_FX="C:\javafx-sdk-21\lib"
mvn javafx:run
```

---

# 🛠 6. Project Structure

```css
src/
 └─ main/
     ├─ java/com/example/
     │   ├─ Main.java
     │   ├─ controller/StudentController.java
     │   ├─ viewmodel/StudentViewModel.java
     │   └─ model/Student.java
     └─ resources/com/example/
         └─ student-view.fxml
```

---

# ✨ 7. Features Implemented

## ✔ MVVM Architecture
* View → FXML
* ViewModel → all state + logic
* Controller → UI actions & binding
## ✔ Add Students
* Name
* Email
* Birthdate (DatePicker only)
## ✔ Real-Time Search
* Filters full name, last name, and email
* Works together with sorting
## ✔ Sorting Options
* First Name
* Last Name
* Email
* Birthdate
* Ascending / Descending (checkbox)
## ✔ Custom ListView Layout
Displayed in 3-column table layout (Name | Email | Birthdate) using HBox.
## ✔ Delete & Edit
* Double-click to edit
* Delete button removes the selected item
* ListView auto-refreshes correctly

---

# 🔍 8. Troubleshooting
## ❗ Windows User Gets "No JavaFX modules found"
They forgot to change:
```xml
<classifier>mac-aarch64</classifier>
```
to:
```xml
<classifier>win</classifier>
```
## ❗ “Cannot find symbol lombok”
Run:
```sh
mvn clean install
```
or enable annotation processing in IntelliJ.

# 📄 9. Notes
* JavaFX does not support perfectly aligned text via spacing.

   That’s why the project uses custom ListCell with HBox.
* The project runs identically on macOS, Windows, and Linux.

# 🎉 10. How to Build JAR
```sh
mvn clean package
```
Run jar with:
```sh
java -jar target/mvvm-javafx-example-1.0.0.jar
```
(Windows users must include --module-path manually unless JavaFX is bundled.)

# 🧑‍💻 Maintainer
**Do Davin**

Software Engineering Student • Developer