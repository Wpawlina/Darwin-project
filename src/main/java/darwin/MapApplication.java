package darwin;

import darwin.model.AbstractMap;
import darwin.model.WorldMap;
import darwin.presenter.MapPresenter;
import darwin.util.SimulationConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MapApplication extends Application {


    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("fxml/map.fxml"));
        try {
            BorderPane viewRoot = loader.load();
            MapPresenter presenter = loader.getController();
            configureStage(primaryStage, viewRoot);
            primaryStage.show();
            ArrayList<String> args= new ArrayList<String>( this.getParameters().getRaw());
            SimulationConfig config = new SimulationConfig(Integer.parseInt(args.getFirst()),Integer.parseInt(args.get(1)),Boolean.parseBoolean(args.get(2)),Integer.parseInt(args.get(3)),Integer.parseInt(args.get(4)),Integer.parseInt(args.get(5)),Integer.parseInt(args.get(6)),Integer.parseInt(args.get(7)),Integer.parseInt(args.get(8)),Integer.parseInt(args.get(9)),Integer.parseInt(args.get(10)),Integer.parseInt(args.get(11)),Boolean.parseBoolean(args.get(12)),Integer.parseInt(args.get(13)));
            Simulation simulation= new Simulation(config);

            presenter.setSimulation(simulation);

            presenter.setStage(primaryStage);
            WorldMap map= simulation.getMap();
            presenter.setMap(map);
            map.registerObserver(presenter);
            SimulationEngine engine = new SimulationEngine(simulation);

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



    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
