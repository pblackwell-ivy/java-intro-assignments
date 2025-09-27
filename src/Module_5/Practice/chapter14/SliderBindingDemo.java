package Module_5.Practice.chapter14;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SliderBindingDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        Slider slider = new Slider(0, 100, 50);   // min=0, max=100, start=50
        TextField textField = new TextField();

        // Bind the text field's text to the slider's value
        textField.textProperty().bind(slider.valueProperty().asString("%.0f"));

        VBox vbox = new VBox(10, textField, slider);
        Scene scene = new Scene(vbox, 300, 150);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Slider Binding Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}