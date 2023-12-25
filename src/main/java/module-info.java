module com.treevalue.clock {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jlayer;
    requires javafx.media;


    opens com.treevalue.clock to javafx.fxml;
    exports com.treevalue.clock;
    exports com.treevalue.clock.compoent;
    opens com.treevalue.clock.compoent to javafx.fxml;
    exports com.treevalue.clock.controller;
    opens com.treevalue.clock.controller to javafx.fxml;
}
