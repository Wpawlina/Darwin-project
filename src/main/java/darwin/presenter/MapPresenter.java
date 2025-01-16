package darwin.presenter;

import darwin.Simulation;
import darwin.model.AbstractMap;
import darwin.model.MapChangeListener;
import darwin.model.Vector2d;
import darwin.model.WorldMap;
import darwin.model.animal.AbstractAnimal;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.util.Arrays;
import java.util.LinkedList;

import darwin.util.Boundary;


public class MapPresenter implements MapChangeListener {

    @FXML
    private GridPane gridPane;

    @FXML
    private Button startButton;

    @FXML
    private VBox simulationStatistics;

    @FXML
    private VBox animalStatistics;

    @FXML
    private Label infoLabel;

    @FXML
    private Label titleLabel;

    private StartButtonState startButtonState=StartButtonState.START;

    private Simulation simulation;

    private WorldMap map ;

    private Stage stage;

    @FXML
    public void initialize() {
        this.startButton.setOnAction(event -> onStartButtonClicked());
    }

    @Override
    public synchronized void mapChanged(WorldMap map) {
        Platform.runLater(()->{
            this.drawMap();
            this.drawStatistics();
        });
    }

    @Override
    public synchronized void AllAnimalsDead(WorldMap map) {
        Platform.runLater(()->{
            this.startButtonState = StartButtonState.ENDED;
            this.startButton.setText(StartButtonState.ENDED.getNextState().getText());
            this.infoLabel.setText("All animals are dead!!!");
        });
    }

    @FXML
    public synchronized void onStartButtonClicked() {
        switch (this.startButtonState)
        {
            case START -> {
                this.startButtonState = StartButtonState.STOP;
                this.startButton.setText(StartButtonState.STOP.getNextState().getText());
                this.simulation.setRunning(false);
            }
            case  STOP -> {
                this.startButtonState = StartButtonState.START;
                this.startButton.setText(StartButtonState.START.getNextState().getText());
                this.simulation.setRunning(true);

            }
            case ENDED -> {
                this.simulation.setRunning(false);
                this.stage.close();
            }

        }
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void setMap(WorldMap map) {
        this.map = map;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    private void drawMap(){
        this.clearGrid();
        this.drawGrid();
    }

    private  void drawStatistics(){
        this.animalStatistics.getChildren().clear();
        this.simulationStatistics.getChildren().clear();
        this.drawSimulationStatistics();
        this.drawAnimalStatistics();
    }

    @FXML
    private void clearGrid() {
        this.gridPane.getColumnConstraints().clear();
        this.gridPane.getRowConstraints().clear();
    }


    @FXML
    private void drawGrid(){
        Boundary boundary = this.map.getMapBoundary();
        int mapWidth = boundary.upperRight().getX() - boundary.lowerLeft().getX() + 1;
        int mapHeight = boundary.upperRight().getY() - boundary.lowerLeft().getY() + 1;
        int CELL_WIDTH =(int)Math.floor( gridPane.getWidth() / mapWidth);
        int CELL_HEIGHT = (int)Math.floor(gridPane.getHeight() / mapHeight);
        int CELL_SIZE = Math.min(CELL_WIDTH,CELL_HEIGHT);

        for(int i=0; i<mapWidth; i++){
            this.gridPane.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));
        }
        for(int i=0; i<mapHeight; i++){
            this.gridPane.getRowConstraints().add(new RowConstraints(CELL_SIZE));
        }


        GridPane.setHalignment(this.gridPane, HPos.CENTER);
        this.drawFields(CELL_SIZE,mapWidth,mapHeight);


    }

    @FXML
    private void drawFields(int CELL_SIZE,int mapWidth, int mapHeight){
        Boundary boundary = this.map.getMapBoundary();
        Boundary jungleBoundary = this.map.getJungleBoundary();
        int maxEnergy = this.map.getMaxEnergy();
        for(int i=0; i<mapWidth; i++){
            for(int j=0; j<mapHeight; j++){
                StackPane field = new StackPane();
                int y = boundary.upperRight().getY() - j;
                Vector2d position = new Vector2d(i,y);
                if (jungleBoundary.contains(position)){
                    field.setStyle("-fx-background-color: "+ ColorProvider.getJungleColor());
                }
                else{
                    field.setStyle("-fx-background-color: "+ ColorProvider.getSteppeColor());
                }
                field.setOnMouseClicked(event -> {
                    if(this.startButtonState==StartButtonState.STOP) {
                        this.map.getFirstAnimalOnSpace(position).ifPresentOrElse((AbstractAnimal animal) -> {

                            this.simulation.setTrackedAnimal(animal);
                            Platform.runLater(() -> {
                                this.animalStatistics.getChildren().clear();
                                this.drawAnimalStatistics();

                            });
                        }, () -> {
                            this.simulation.setTrackedAnimal(null);
                        });
                    }
                });
                this.gridPane.add(field,i,j);

                if(map.getFirstAnimalOnSpace(position).isPresent()){
                    Circle circle = new Circle((double)CELL_SIZE/2);
                    circle.setStyle("-fx-fill: "+ColorProvider.getAnimalColor(map.getFirstAnimalOnSpace(position).get().getProperties().getEnergy(),maxEnergy));
                    field.getChildren().add(circle);
                } else if (map.getPlantOnSpace(position).isPresent()){
                    Circle circle = new Circle((double)CELL_SIZE/2);
                    circle.setStyle("-fx-fill: "+ ColorProvider.getPlantColor());
                    field.getChildren().add(circle);

                }
            }
        }
    }

    @FXML
    private void drawSimulationStatistics(){
        this.simulationStatistics.getChildren().add(new Label("Day: " + this.simulation.getDay()));
        this.simulationStatistics.getChildren().add(new Label("Animals: " + this.map.getAliveAnimals().size()));
        this.simulationStatistics.getChildren().add(new Label("Plants: " + this.map.countPlant()));
        this.simulationStatistics.getChildren().add(new Label("Empty fields: " + this.map.countEmptyPositions()));
        this.simulationStatistics.getChildren().add(new Label("Most common genome: " + this.map.showMostPopularGenome()));
        this.simulationStatistics.getChildren().add(new Label("Average energy: " + this.map.getAverageEnergy()));
        this.simulationStatistics.getChildren().add(new Label("Average life span: " + this.map.getAverageLifeSpan()));
        this.simulationStatistics.getChildren().add(new Label("Average children number: " + Math.floor(this.map.getAverageChildren()*100)/100));
    }

    @FXML
    private void drawAnimalStatistics(){
        this.simulation.getTrackedAnimal().ifPresentOrElse((AbstractAnimal animal )-> {
            this.animalStatistics.getChildren().add(new Label("Energy: " + animal.getProperties().getEnergy()));
            this.animalStatistics.getChildren().add(new Label("Genome: " + Arrays.toString(animal.getProperties().getGenome())));
            this.animalStatistics.getChildren().add(new Label("Genome index: " + animal.getProperties().getIndex()));
            this.animalStatistics.getChildren().add(new Label("Plants eaten: " + animal.getProperties().getPlantsEaten()));
            this.animalStatistics.getChildren().add(new Label("Children number: " + animal.countChildren()));
            this.animalStatistics.getChildren().add(new Label("Descendents number: " + animal.getProperties().getChildren().size()));
            this.animalStatistics.getChildren().add(new Label(animal.getProperties().getState()== AnimalState.ALIVE?"Life span: " + animal.getProperties().getAge():"Date of death: " + animal.getProperties().getDeathDate()));
        }, () -> {
            this.animalStatistics.getChildren().add(new Label("No animal is being tracked"));
            this.animalStatistics.getChildren().add(new Label("Energy: " ));
            this.animalStatistics.getChildren().add(new Label("Genome: "));
            this.animalStatistics.getChildren().add(new Label("Genome index: "));
            this.animalStatistics.getChildren().add(new Label("Plants eaten: "));
            this.animalStatistics.getChildren().add(new Label("Children number: " ));
            this.animalStatistics.getChildren().add(new Label("Descendents number: "));

        });

    }















}
