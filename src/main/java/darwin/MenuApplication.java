package darwin;

import darwin.presenter.MenuPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuApplication extends Application {
    @Override
    public void start(Stage stage){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("fxml/menu.fxml"));
        try {
            GridPane viewRoot = loader.load();
            MenuPresenter presenter = loader.getController();
            stage.setTitle("Menu");
            stage.setScene(new Scene(viewRoot));
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void configureStage(Stage primaryStage, GridPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
