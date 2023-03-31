module com.example.interpole {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens com.example.interpole to javafx.fxml;
    exports com.example.interpole;
}