package Module_5_Assignments;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/// 15.7 (Change color using a mouse) Write a program that displays the color of a circle as black when
/// the mouse button is pressed, and as white when the mouse button is released.
///
/// This program is built using lambda expressions to reduce the code instead of defining the whole class or
/// an anonymous inner class for EventHandler.
public class Exercise15_7 extends Application {

    @Override
    public void start(Stage primaryStage) {
        // create a pane to hold the circle
        Pane pane = new Pane();

        // Create a circle with radius 50
        Circle circle = new Circle(50);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);

        // bind circle center to half of pane's width and height
        circle.centerXProperty().bind(pane.widthProperty().divide(2));
        circle.centerYProperty().bind(pane.heightProperty().divide(2));

        // Register event handlers with lambdas
        circle.setOnMousePressed(e -> circle.setFill(Color.BLACK));
        circle.setOnMouseReleased(e -> circle.setFill(Color.WHITE));

        // Add the circle to the pane
        pane.getChildren().add(circle);

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane, 300, 250);
        primaryStage.setTitle("Circle Color Change - Lambda (Centered)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)  {
        launch(args);
    }
}

