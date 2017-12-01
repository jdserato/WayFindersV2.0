import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Serato, Jay Vince on November 24, 2017.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        /*Parent root = FXMLLoader.load(new File("src/Management.fxml").toURL());
        Scene scene = new Scene(root, 750, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CSBT Data Editor");
        primaryStage.show();*/
        Stage second = new Stage();
        Parent secondRoot = FXMLLoader.load(new File("src/WayFinders.fxml").toURL());
        Scene sceneTwo = new Scene(secondRoot, screen.getWidth(), screen.getHeight());
        second.setScene(sceneTwo);
        second.setTitle("SBT Directory System");
        second.setFullScreen(true);
        //second.setAlwaysOnTop(true);
        second.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
