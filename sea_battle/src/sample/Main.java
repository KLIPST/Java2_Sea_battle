package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Sea battle");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));


        // два грида для выбора клеток в игре
        Scene scene1 = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);

        Scene scene2 = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
