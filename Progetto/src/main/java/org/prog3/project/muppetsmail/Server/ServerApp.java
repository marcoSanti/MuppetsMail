package org.prog3.project.muppetsmail.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Server.Controller.ServerController;
import org.prog3.project.muppetsmail.Server.Model.ServerModel;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNotFoundException;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ServerApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Server.fxml"));
        stage.setScene(loader.load());
        stage.setTitle("Muppets Mail Server");
        stage.setResizable(false);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ServerIcon.png"))));
        ServerController controller = loader.getController();
        controller.setModel(new ServerModel());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
