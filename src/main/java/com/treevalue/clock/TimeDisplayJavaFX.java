package com.treevalue.clock;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimeDisplayJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Label to display the time
        Label timeLabel = new Label();
        timeLabel.setStyle("-fx-font-size: 20px;");

        // Format for displaying the time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Timeline to update the label every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalTime currentTime = LocalTime.now();
            timeLabel.setText(currentTime.format(formatter));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Setting up the stage
        VBox root = new VBox(10, timeLabel);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 300, 100);

        primaryStage.setTitle("Current Time");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
