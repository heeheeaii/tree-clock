package com.treevalue.clock;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp1 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("AlarmClockUI.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/com/treevalue/clock/clock.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("闹钟设置");
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
