package Module_5_Assignments;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Objects;       // used for a helper method

/**
 * 14.1 (Display images) Write a program that displays four images in a GridPane, as shown in Figure 14.43a
 * 4 flags - Germany, China, France, and US shown in a 2 x 2 grid pane
 * +---------+---------+
 * | Germany |  China  |   Row 0
 * +---------+---------+
 * | France  |   US    |   Row 1
 * +---------+---------+
 */
public class Exercise14_1 extends Application {
    // Centralize sizing so it’s easy to tweak later
    private static final double FLAG_WIDTH  = 150;
    private static final double FLAG_HEIGHT = 100;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Create a simple 2x2 grid pane centered with padding and gaps
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(12));
        pane.setHgap(10);
        pane.setVgap(10);

        // call load() helper to load flag images
        Image us      = load("/image/us.png");
        Image china  = load("/image/china.png");
        Image france  = load("/image/france.png");
        Image germany = load("/image/germany.png");

        // size each flag and place in the grid pane
        Image[] flags = { germany, china, france, us };
        for (int i = 0; i < flags.length; i++) {
            ImageView view = new ImageView(flags[i]);
            view.setFitWidth(FLAG_WIDTH);
            view.setFitHeight(FLAG_HEIGHT);
            view.setPreserveRatio(true);
            pane.add(view, i % 2, i / 2);
        }

        // Create a scene and place it in the stage
        primaryStage.setTitle("Flags – GridPane 2x2");
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }

    // helper method to load images and return error if image resource not found
    private Image load(String path) {
        return new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(path),
                "Missing resource: " + path));
    }
    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}