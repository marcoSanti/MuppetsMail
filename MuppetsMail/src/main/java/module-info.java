module org.prog3.project.muppetsmail {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;


    opens org.prog3.project.muppetsmail.Client to javafx.fxml;
    opens org.prog3.project.muppetsmail.Server to javafx.fxml;

    exports org.prog3.project.muppetsmail.Client;
    exports org.prog3.project.muppetsmail.Client.Controller;
    exports org.prog3.project.muppetsmail.Client.Model;

    exports org.prog3.project.muppetsmail.Server;
    exports org.prog3.project.muppetsmail.Server.Controller;
    exports org.prog3.project.muppetsmail.Server.Model;

    exports org.prog3.project.muppetsmail.SharedModel;
    exports org.prog3.project.muppetsmail.SharedModel.Exceptions;
    exports org.prog3.project.muppetsmail.Client.ViewObjs;
}