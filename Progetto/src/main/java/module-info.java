module org.prog3.project.muppetsmail {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens org.prog3.project.muppetsmail.Client to javafx.fxml;
    opens org.prog3.project.muppetsmail.Server to javafx.fxml;
    exports org.prog3.project.muppetsmail.Client;
    exports org.prog3.project.muppetsmail.Server;
}