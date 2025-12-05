package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/screen.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle("Prime & Fibonacci Generator");
        stage.setScene(scene);

        stage.setMinWidth(600);
        stage.setMinHeight(500);
        stage.setMaxWidth(600);
        stage.setMaxHeight(500);
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
