package Module_5.Practice.chapter14;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloFX extends Application {
    @Override
    public void start(Stage stage) {
        Button b = new Button("Say Hello");
        b.setOnAction(e -> System.out.println("Hello, JavaFX!"));
        stage.setScene(new Scene(new StackPane(b), 340, 200));
        stage.setTitle("Hello JavaFX");
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}