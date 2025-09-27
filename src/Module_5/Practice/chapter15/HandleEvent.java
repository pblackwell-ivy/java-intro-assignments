package Module_5.Practice.chapter15;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class HandleEvent extends Application {
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Create a pane and set its properties
        HBox pane = new HBox(10);
        pane.setAlignment(Pos.CENTER);

        // Create two buttons
        Button btOK = new Button("OK");
        Button btCancel = new Button("Cancel");

        // STEP 1:  Create handler objects (classes that implement EventHandler)
        OKHandlerClass handler1 = new OKHandlerClass();
        CancelHandlerClass handler2 = new CancelHandlerClass();

        // STEP 2: Register the handlers with the buttons
        // setOnAction() tells JavaFX which handler should respond to button click events
        btOK.setOnAction(handler1);
        btCancel.setOnAction(handler2);

        // Add the buttons to the pane
        pane.getChildren().addAll(btOK, btCancel);

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane);
        primaryStage.setTitle("HandleEvent");   // Set the stage title
        primaryStage.setScene(scene);           // Place the scene in the stage
        primaryStage.show();                    // Display the stage
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

// STEP 3: Define a handler class for the OK button
// - Must implement EventHandler<T>
// - T is the event type (ActionEvent for button clicks)
class OKHandlerClass implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e) {
        System.out.println("OK button clicked");
    }
}

// STEP 3 (again): Define a handler class for the Cancel button
class CancelHandlerClass implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e) {
        System.out.println("Cancel button clicked");
    }
}