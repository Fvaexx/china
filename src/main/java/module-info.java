module org.example.china {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens org.example.china to javafx.fxml;
    exports org.example.china;
}