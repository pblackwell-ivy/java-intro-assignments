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
 * Note:  I've included two approaches to size and place each flag in the GridPane - nested loops and a single loop
 * I discovered the single loop approach which seems more elegant, but kept the nested loop approach.
 * I commented out the nested loop approach which is how I originally sized and placed each flag.
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

        // call load() helper to create Image of each flag and place images in an array of flags
        Image us      = load("/image/us.png");
        Image china  = load("/image/china.png");
        Image france  = load("/image/france.png");
        Image germany = load("/image/germany.png");
        Image[] flags = { germany, china, france, us };     // create an array Image[[ to hold flags to cycle through

        // use nested loops to cycle through all flags, sizing and placing each in the grid
        /* - comment out nested loop approach
            int index = 0;                                      // tracks which flag we are using
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    ImageView view = new ImageView(flags[index++]);
                    view.setFitHeight(FLAG_HEIGHT);
                    view.setFitWidth(FLAG_WIDTH);
                    view.setPreserveRatio(true);
                    pane.add(view, col, row);                   // places the flag image at row and col
                }
            }
         */

        // use a "trick" single loop to cycle through all flags, sizing and placing each in the grid
        // cycles colums using col = i % 2 and when row is filled, step to next row with row = i / 2
        for (int i = 0; i < flags.length; i++) {
            int col = i % 2;                         // cycles through 0..cols-1
            int row = i / 2;                         // steps down when a row is filled
            ImageView view = new ImageView(flags[i]);
            view.setFitWidth(FLAG_WIDTH);
            view.setFitHeight(FLAG_HEIGHT);
            view.setPreserveRatio(true);
            pane.add(view, col, row);                   // places flags[i] in the pane
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