package org.prog3.project.muppetsmail.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Client.Controller.HomeController;
import org.prog3.project.muppetsmail.Client.Controller.LoginController;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;

import java.util.Objects;

/**
 * This class is the main application of the client. we create the main stage and the login stage
 */
public class ClientApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        ClientModel appModel = new ClientModel();

        //Loading of main window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        stage.setScene(loader.load());
        stage.setTitle("Muppets Mail Client");
        stage.setResizable(false);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ClientIcon.png"))));
        HomeController homeController = loader.getController();
        homeController.setClientModel(appModel);
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.show();

        //show the login window
        Stage loginStage = new Stage();
        loginStage.setResizable(false);
        loginStage.setIconified(false);
        loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        loginStage.setScene(loader.load());
        loginStage.setTitle("Login to Muppets Mail Server");
        loginStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ClientIcon.png"))));
        loginStage.setResizable(false);
        LoginController loginController = loader.getController();
        loginController.setClientModel(appModel);
        homeController.setLoginStage(loginStage);
        loginStage.setOnCloseRequest(windowEvent -> System.exit(0));
        loginStage.show();

        //start of the client demon to refresh the view and to check for new emails
        (new ClientDemon(appModel, homeController, 1000)).start();
       

    }


    public static void main(String[] args) {
        launch();
    }
}
