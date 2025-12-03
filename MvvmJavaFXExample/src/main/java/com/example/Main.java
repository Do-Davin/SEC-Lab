package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class that launches the JavaFX MVVM example.
 * This class is responsible for setting up the primary stage and loading the FXML view.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student_view.fxml"));
        Parent root = loader.load();
        
        // Get the controller instance (optional, for demonstration)
        StudentController controller = loader.getController();
        
        // Set up the scene
        Scene scene = new Scene(root, 600, 500);
        
        // Configure the primary stage
        primaryStage.setTitle("JavaFX MVVM Example - Student Management");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(400);
        
        // Show the application
        primaryStage.show();
        
        // Optional: Print some information about the MVVM setup
        System.out.println("JavaFX MVVM Application Started");
        System.out.println("Controller: " + controller.getClass().getSimpleName());
        System.out.println("ViewModel: " + controller.getViewModel().getClass().getSimpleName());
    }

    /**
     * Main method to launch the JavaFX application.
     * This is the entry point of the application.
     */
    public static void main(String[] args) {
        System.out.println("Starting JavaFX MVVM Student Management Application...");
        
        // Launch the JavaFX application
        launch(args);
    }
}