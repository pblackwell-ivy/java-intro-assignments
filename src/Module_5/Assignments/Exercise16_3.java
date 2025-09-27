package Module_5.Assignments;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * **16.3 (Traffic lights) Write a program that simulates a traffic light.
 * The program lets the user select one of three lights: red, yellow, or green.
 * When a radio button is selected, the light is turned on.
 * Only one light can be on at a time (see Figure 16.37a).
 * No light is on when the program starts.
 */
public class Exercise16_3 extends Application {
    private static final int lightRadius = 30;
    @Override
    public void start(Stage primaryStage) {
        //  define lightArea that will hold the lights (housing and circles)
        StackPane lightArea = new StackPane();
        lightArea.setPadding(new Insets(16));

        // Housing (white rectangle behind the circles)
        Rectangle housing = new Rectangle(100, 260);
        housing.setFill(Color.WHITE);
        housing.setStroke(Color.BLACK);

        // Make three lights that are turned off (white fill, black outline)
        Circle redLight = makeOffCircle(lightRadius);
        Circle yellowLight = makeOffCircle(lightRadius);
        Circle greenLight = makeOffCircle(lightRadius);

        VBox lights = new VBox(20, redLight, yellowLight, greenLight);
        lights.setAlignment(Pos.CENTER);
        lights.setPadding(new Insets(16));

        // Order matters: lights are in front of the housing
        lightArea.getChildren().addAll(housing, lights);

        // Define radio buttons
        RadioButton rbRed    = new RadioButton("Red");
        RadioButton rbYellow = new RadioButton("Yellow");
        RadioButton rbGreen  = new RadioButton("Green");

        // Define the lightChoices ToggleGroup and assign the radio buttons to it
        ToggleGroup lightChoices = new ToggleGroup();
        rbRed.setToggleGroup(lightChoices);
        rbYellow.setToggleGroup(lightChoices);
        rbGreen.setToggleGroup(lightChoices);

        // All lights are initially off
        lightChoices.selectToggle(null);

        // When selection changes, turn all off, then the chosen one on
        lightChoices.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            setOff(redLight); setOff(yellowLight); setOff(greenLight);
            if (newT == rbRed) {
                redLight.setFill(Color.RED);
            } else if (newT == rbYellow) {
                yellowLight.setFill(Color.GOLD);
            } else if (newT == rbGreen) {
                greenLight.setFill(Color.LIMEGREEN);
            }
        });

        // Define "controls" to hold the lightChoices
        HBox controls = new HBox(18, rbRed, rbYellow, rbGreen);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(20, 0, 20, 0));

        // place the lightArea and controls in a BorderPane called "root"
        BorderPane root = new BorderPane();
        root.setCenter(lightArea);
        root.setBottom(controls);

        // Clicking the background moves the focus off the radio buttons
        root.setOnMouseClicked(e -> root.requestFocus());

        // Create a scene and place it in the stage
        Scene scene = new Scene(root, 420, 520);
        primaryStage.setTitle("Traffic Lights");
        primaryStage.setScene(scene);
        primaryStage.show();
        // At startup, give focus to the root so no control shows a halo
        root.requestFocus();
    }

    // creates a circle with the given radius and turns it off (white fill, black outline)
    private static Circle makeOffCircle(double radius) {
        Circle c = new Circle(radius);
        setOff(c);
        c.setStrokeWidth(2);
        return c;
    }

    // turns a circle off (white fill, black outline)
    private static void setOff(Circle c) {
        c.setFill(Color.WHITE);
        c.setStroke(Color.BLACK);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
