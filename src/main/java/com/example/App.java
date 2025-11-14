package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application {

  private static final Logger logger = LoggerFactory.getLogger(App.class);
    
    private static final String APPLICATION_TITLE = "Student Management System - MVC Demo";
    private static final String VERSION = "1.0.0";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private static final int MIN_WINDOW_WIDTH = 1000;
    private static final int MIN_WINDOW_HEIGHT = 600;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Starting Student Management Application v{}", VERSION);
            
            // Load the main FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StudentManagement.fxml"));
            Parent root = loader.load();
            
            // Create the scene
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            
            // Load CSS stylesheet
            String cssPath = getClass().getResource("/css/application.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
            logger.info("CSS stylesheet loaded: {}", cssPath);
            
            // Configure the primary stage
            primaryStage.setTitle(APPLICATION_TITLE);
            primaryStage.setScene(scene);
            
            // Set minimum window size
            primaryStage.setMinWidth(MIN_WINDOW_WIDTH);
            primaryStage.setMinHeight(MIN_WINDOW_HEIGHT);
            
            // Set application icon (if available)
            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/icon.png"));
                primaryStage.getIcons().add(icon);
                logger.info("Application icon loaded");
            } catch (Exception e) {
                logger.warn("Could not load application icon: {}", e.getMessage());
            }
            
            // Center the window on screen
            primaryStage.centerOnScreen();
            
            // Handle window close event
            primaryStage.setOnCloseRequest(event -> {
                logger.info("Application shutdown requested");
                handleApplicationShutdown();
            });
            
            // Show the primary stage
            primaryStage.show();
            
            logger.info("Student Management Application started successfully");
            
        } catch (Exception e) {
            logger.error("Failed to start application", e);
            showErrorAndExit("Startup Error", "Failed to start the application", e);
        }
    }
    
    @Override
    public void stop() throws Exception {
        logger.info("Application is stopping...");
        handleApplicationShutdown();
        super.stop();
    }
    
    /**
     * Handle application shutdown tasks
     */
    private void handleApplicationShutdown() {
        try {
            // Add any cleanup tasks here
            // For example: close database connections, save user preferences, etc.
            
            logger.info("Application shutdown completed successfully");
        } catch (Exception e) {
            logger.error("Error during application shutdown", e);
        }
    }
    
    /**
     * Show error dialog and exit application
     */
    private void showErrorAndExit(String title, String header, Exception exception) {
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(exception.getMessage());
            
            alert.showAndWait();
            javafx.application.Platform.exit();
        });
    }
    
    /**
     * Main method - entry point of the application
     */
    public static void main(String[] args) {
        // Set application properties
        System.setProperty("app.name", "Student Management System");
        System.setProperty("app.version", VERSION);
        
        logger.info("Launching Student Management Application...");
        logger.info("Java Version: {}", System.getProperty("java.version"));
        logger.info("JavaFX Version: {}", System.getProperty("javafx.version"));
        logger.info("Operating System: {} {}",
                   System.getProperty("os.name"), System.getProperty("os.version"));
        
        // Handle uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            logger.error("Uncaught exception in thread {}: {}", thread.getName(), exception.getMessage(), exception);
        });
        
        try {
            // Launch the JavaFX application
            launch(args);
        } catch (Exception e) {
            logger.error("Failed to launch application", e);
            System.exit(1);
        }
    }
}