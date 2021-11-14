package org.prog3.project.muppetsmail.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Server.Controller.ServerController;
import org.prog3.project.muppetsmail.Server.Model.ServerModel;
import org.prog3.project.muppetsmail.SharedModel.MailboxSaveDaemon;

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
        ServerController controller = loader.getController();
        controller.setModel(serverModel);
        stage.show();

        //starting demon to update local mailboxes to file
        MailboxSaveDaemon saveDemon  = new MailboxSaveDaemon(serverModel.getMailBoxes(), 3000);
        serverModel.addLog("Started mailbox file synchronization daemon");
    }

    public static void main(String[] args) {
        launch();
    }
}
