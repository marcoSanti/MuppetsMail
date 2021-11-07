package org.prog3.project.muppetsmail.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class ClientApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        //Loading of main window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        stage.setScene(loader.load());
        stage.setTitle("Muppets Mail Client");
        stage.setResizable(false);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ClientIcon.png"))));
        stage.show();

        //show the login window
        Stage stage1 = new Stage();
        loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        stage1.setScene(loader.load());
        stage1.setTitle("Login to Muppets Mail Server");
        stage1.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ClientIcon.png"))));
        stage1.setResizable(false);
        stage1.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
