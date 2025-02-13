package darwin;

import darwin.model.WorldMap;
import darwin.presenter.MapPresenter;
import darwin.presenter.MenuPresenter;
import darwin.util.SimulationConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MenuApplication extends Application {
    @Override
    public void start(Stage stage){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("fxml/menu.fxml"));
        try {
            GridPane viewRoot = loader.load();
            MenuPresenter presenter = loader.getController();
            presenter.setApp(this);
            stage.setTitle("Menu");
            stage.setScene(new Scene(viewRoot));
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void openMapStage(String[] arguments)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("fxml/map.fxml"));
        Stage primaryStage = new Stage();


        try {
            BorderPane viewRoot = loader.load();
            MapPresenter presenter = loader.getController();
            configureMapStage(primaryStage, viewRoot);
            primaryStage.show();
            ArrayList<String> args= new ArrayList<String>(Arrays.asList(arguments));
            SimulationConfig config = new SimulationConfig(
                    Integer.parseInt(args.getFirst()),
                    Integer.parseInt(args.get(1)),
                    Boolean.parseBoolean(args.get(2)),
                    Integer.parseInt(args.get(3)),
                    Integer.parseInt(args.get(4)),
                    Integer.parseInt(args.get(5)),
                    Integer.parseInt(args.get(6)),
                    Integer.parseInt(args.get(7)),
                    Integer.parseInt(args.get(8)),
                    Integer.parseInt(args.get(9)),
                    Integer.parseInt(args.get(10)),
                    Integer.parseInt(args.get(11)),
                    Boolean.parseBoolean(args.get(12)),
                    Integer.parseInt(args.get(13)),
                    Boolean.parseBoolean(args.get(14)));
            Simulation simulation= new Simulation(config);


            presenter.setSimulation(simulation);

            presenter.setStage(primaryStage);
            WorldMap map= simulation.getMap();
            presenter.setMap(map);
            map.registerObserver(presenter);
            SimulationEngine engine = new SimulationEngine(simulation);
            primaryStage.setOnCloseRequest(event -> {
                simulation.stop();
            });


            try {
                engine.runAsync();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }




    private void configureMapStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Map app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
