package Module_5.Practice.chapter14;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ClockAnimation extends Application {
    @Override
    public void start(Stage primaryStage) {
        ClockPane clock = new ClockPane();

        // Use lambda approach
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> clock.setCurrentTime()));

        // alternative, longer approach instead of lambda
        /* EventHandler<ActionEvent> eventHandler = e -> {
            clock.setCurrentTime();
        };
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));

         */
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();

        Scene scene = new Scene(clock, 250, 250);
        primaryStage.setTitle("ClockAnimation");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
