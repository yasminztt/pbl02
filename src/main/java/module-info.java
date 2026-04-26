module com.example.treevisualizer {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;

    opens com.example.treevisualizer to javafx.fxml;
    exports com.example.treevisualizer;
}