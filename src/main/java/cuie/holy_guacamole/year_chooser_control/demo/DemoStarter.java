package cuie.holy_guacamole.year_chooser_control.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class DemoStarter extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PresentationModel model = new PresentationModel();
        Region rootPanel = new DemoPane(model);

        Scene scene = new Scene(rootPanel);

        primaryStage.setTitle("Year Chooser Demo");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
