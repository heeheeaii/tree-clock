package com.treevalue.clock;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MP3Player extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String mediaSource = "C:\\Users\\wyhee\\Downloads\\111\\clock\\src\\main\\resources\\music\\我在那一角落患过伤风.mp3";
        Media media = new Media(new File(mediaSource).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        Slider slider = new Slider(0, 100, 0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(25);
        slider.setBlockIncrement(10);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.seek(Duration.seconds(newValue.doubleValue() / 100 * mediaPlayer.getTotalDuration().toSeconds()));
        });

        VBox root = new VBox(slider);
        Scene scene = new Scene(root, 300, 100);

        primaryStage.setScene(scene);
        primaryStage.show();

        mediaPlayer.play();
    }
}
