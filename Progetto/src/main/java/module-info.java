module org.prog3.project.muppetsmail {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens org.prog3.project.muppetsmail to javafx.fxml;
    exports org.prog3.project.muppetsmail;
}