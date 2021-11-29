package org.prog3.project.muppetsmail.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Server.Controller.ServerController;
import org.prog3.project.muppetsmail.Server.Model.ServerModel;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class ServerApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        ServerModel serverModel = new ServerModel();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Server.fxml"));
        stage.setScene(loader.load());
        stage.setTitle("Muppets Mail Server");
        stage.setResizable(false);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ServerIcon.png"))));

        //This thre lines are here to set mac os icon of the dock. Check if it works on windows!
        final Taskbar tb = Taskbar.getTaskbar();
        URL url = getClass().getResource("ServerIcon.png");
        tb.setIconImage(Toolkit.getDefaultToolkit().getImage(url));

        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        ServerController controller = loader.getController();
        controller.setModel(serverModel);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
