package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("form.fxml"));
        primaryStage.setTitle("File Editor");

        scene = new Scene(root, 957, 590);

       // scene.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());
       // scene.getStylesheets().add((new File("style/style.css")).toURI().toURL().toExternalForm());
        primaryStage.setScene(scene);

        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }

}
