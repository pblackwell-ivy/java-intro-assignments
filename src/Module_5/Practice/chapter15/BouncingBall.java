package Module_5.Practice.chapter15;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BouncingBall extends Application {
    @Override
    public void start(Stage primaryStage) {
        BallPane ballPane = new BallPane();

        Button btPause  = new Button("Pause");
        Button btPlay   = new Button("Play");
        Button btSlower = new Button("-");
        Button btFaster = new Button("+");

        btPause.setOnAction(e -> ballPane.pause());
        btPlay.setOnAction(e -> ballPane.play());
        btSlower.setOnAction(e -> ballPane.decreaseSpeed());
        btFaster.setOnAction(e  -> ballPane.increaseSpeed());

        HBox controls = new HBox(10, btPause, btPlay, btSlower, btFaster);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane(ballPane, null, null, controls, null);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Bouncing Ball");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
class BallPane extends Pane {
    public final double radius = 20;
    private double x = radius, y = radius;
    private double dx = 1, dy = 1;
    private Circle circle = new Circle(x, y, radius);
    private Timeline animation;

    public BallPane() {
        setPrefSize(400, 250);              // to provide space on first show
        circle.setFill(Color.GREEN);
        circle.setStroke(Color.BLACK);
        getChildren().add(circle);

        // create an animation for moving the ball
        animation = new Timeline(new KeyFrame(Duration.millis(10), e -> moveBall()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();

        // if the pane is resized, keep the ball insides bounds immediately:
        widthProperty().addListener((obs, oldVal, newVal) -> clampInside());
        heightProperty().addListener((obs, oldV, newV) -> clampInside());
    }

    public void play() {
        animation.play();
    }
    public void pause() {
        animation.pause();
    }

    public void increaseSpeed() {
        animation.setRate(animation.getRate() + 2.0);
    }

    public void decreaseSpeed() {
        animation.setRate(Math.max(0.1, animation.getRate() - 2.0));
    }

    public DoubleProperty rateProperty() {
        return animation.rateProperty();
    }

    protected void moveBall() {
        // check boundaries
        if (x < radius || x >  getWidth() - radius) {
            dx *= -1; // change ball direction
        }
        if (y < radius || y > getHeight() - radius) {
            dy *= -1; // change ball move direction
        }

        // Adjust ball position
        x += dx;
        y += dy;
        circle.setCenterX(x);
        circle.setCenterY(y);
    }

    // verbose clampInside method to make it easier to understand.
    private void clampInside() {
        double w = getWidth();
        double h = getHeight();

        // --- Clamp X ---
        double leftBound  = radius;
        double rightBound = w - radius;

        // If the pane is too narrow (smaller than ball diameter),
        // collapse both bounds to at least the radius.
        if (rightBound < radius) {
            rightBound = radius;
        }

        // Clamp the x coordinate between the bounds
        if (x < leftBound) {
            x = leftBound;
        } else if (x > rightBound) {
            x = rightBound;
        }

        // --- Clamp Y ---
        double topBound    = radius;
        double bottomBound = h - radius;

        if (bottomBound < radius) {
            bottomBound = radius;
        }

        if (y < topBound) {
            y = topBound;
        } else if (y > bottomBound) {
            y = bottomBound;
        }

        // Update the circle's position
        circle.setCenterX(x);
        circle.setCenterY(y);
    }
    /* non verbose clampInside

    private void clampInside() {
        // Ensure the ball stays inside after resizes
        x = Math.min(Math.max(x, radius), Math.max(radius, getWidth() - radius));
        y = Math.min(Math.max(y, radius), Math.max(radius, getHeight() - radius));
        circle.setCenterX(x);
        circle.setCenterY(y);
    }

     */
}
