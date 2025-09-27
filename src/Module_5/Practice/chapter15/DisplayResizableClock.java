package Module_5.Practice.chapter15;


import Module_5.Practice.chapter14.ClockPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Code from book updated to animate the clock and update the clock label every second
 * Also, it formats the time label
 * Includes a helper method to format the time string
 */

public class DisplayResizableClock extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Clock + styled time label
        ClockPane clock = new ClockPane();
        Label lblCurrentTime = new Label(formatTime(clock));
        lblCurrentTime.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
        lblCurrentTime.setTextFill(Color.RED);

        // Tick every second: update clock + label in the same handler
        Timeline animation = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    clock.setCurrentTime();
                    lblCurrentTime.setText(formatTime(clock));
                })
        );
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();

        // Layout
        BorderPane pane = new BorderPane();
        pane.setCenter(clock);
        pane.setBottom(lblCurrentTime);
        BorderPane.setAlignment(lblCurrentTime, Pos.TOP_CENTER);
        BorderPane.setMargin(lblCurrentTime, new Insets(0, 10, 10, 10));

        Scene scene = new Scene(pane, 250, 250);
        primaryStage.setTitle("DisplayClock");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Keep clock resizable with the pane
        pane.widthProperty().addListener(ov -> clock.setWidth(pane.getWidth()));
        pane.heightProperty().addListener(ov -> clock.setHeight(pane.getHeight()));
    }

    // Helper: centralize formatting in one place
    private static String formatTime(ClockPane clock) {
        return String.format("%02d:%02d:%02d",
                clock.getHour(), clock.getMinute(), clock.getSecond());
    }

    public static void main(String[] args) {
        launch(args);
    }
}