package Module_5.Practice.chapter15;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HboxDemo extends Application {
    @Override
    public void start(Stage primaryStage) {

        HBox hbox = new HBox(50);
        hbox.setPrefSize(400, 200);
        hbox.setStyle("-fx-border-color: blackwe;");
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);

        Button b1 = new Button("One");
        Button b2 = new Button("Two");

        hbox.getChildren().addAll(b1, b2);
        Scene scene = new Scene(hbox);
        primaryStage.setTitle("HandleEvent");   // Set the stage title
        primaryStage.setScene(scene);           // Place the scene in the stage
        primaryStage.show();                    // Display the stage
    }
    public static void main(String[] args)  {
        launch(args);
    }
}
