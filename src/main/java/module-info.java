module com.example.treevisualizer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.treevisualizer to javafx.fxml;
    exports com.example.treevisualizer;
}