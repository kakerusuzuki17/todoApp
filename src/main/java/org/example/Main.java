package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        TodoDAO.initializeDatabase();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("main.fxml"));

        Scene scene = new Scene(loader.load());
        // ウィンドウ設定
        stage.setTitle("Todo App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}