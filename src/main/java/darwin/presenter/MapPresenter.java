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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    private Canvas canvas;

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
        this.clearCanvas();
        this.drawGrid();
    }

    private  void drawStatistics(){
        this.animalStatistics.getChildren().clear();
        this.simulationStatistics.getChildren().clear();
        this.drawSimulationStatistics();
        this.drawAnimalStatistics();
    }

    @FXML
    private void clearCanvas() {
        GraphicsContext gc= this.canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }


    @FXML
    private void drawGrid(){
        Boundary boundary = this.map.getMapBoundary();
        int mapWidth = boundary.upperRight().getX() - boundary.lowerLeft().getX() + 1;
        int mapHeight = boundary.upperRight().getY() - boundary.lowerLeft().getY() + 1;
        int CELL_WIDTH =(int)Math.ceil( canvas.getWidth() / mapWidth);
        int CELL_HEIGHT = (int)Math.ceil(canvas.getHeight() / mapHeight);
        int CELL_SIZE = Math.min(CELL_WIDTH,CELL_HEIGHT);
        this.drawFields(CELL_SIZE,mapWidth,mapHeight);
        this.registerCanvasClickEvent(CELL_SIZE);


    }

    @FXML
    private void registerCanvasClickEvent(int CELL_SIZE)
    {
        this.canvas.setOnMouseClicked(event -> {
            if(this.startButtonState==StartButtonState.STOP)
            {
                double x = event.getX();
                double y = event.getY();
                int i = (int) Math.floor(x / CELL_SIZE);
                int j = (int) Math.floor(y / CELL_SIZE);
                Boundary boundary = this.map.getMapBoundary();
                j = boundary.upperRight().getY() - j;

                Vector2d position = new Vector2d(i, j);
                this.simulation.setTrackedAnimal(this.map.getFirstAnimalOnSpace(position).orElse(null));
                Platform.runLater(() -> {
                    this.animalStatistics.getChildren().clear();
                    this.drawAnimalStatistics();
                });
            }

        });


    }

    @FXML
    private void drawFields(int CELL_SIZE,int mapWidth, int mapHeight){
        Boundary boundary = this.map.getMapBoundary();
        Boundary jungleBoundary = this.map.getJungleBoundary();
        int maxEnergy = this.map.getMaxEnergy();
        GraphicsContext gc=this.canvas.getGraphicsContext2D();

        for(int i=0; i<mapWidth; i++){
            for(int j=0; j<mapHeight; j++){
                StackPane field = new StackPane();
                int y = boundary.upperRight().getY() - j;
                Vector2d position = new Vector2d(i,y);
                if (jungleBoundary.contains(position)){
                    gc.setFill(ColorProvider.getJungleColor());
                }
                else{
                    gc.setFill(ColorProvider.getSteppeColor());
                }
                gc.fillRect(i*CELL_SIZE,j*CELL_SIZE,CELL_SIZE,CELL_SIZE);




                if(map.getFirstAnimalOnSpace(position).isPresent()){
                    gc.setFill(ColorProvider.getAnimalColor(this.simulation,map.getFirstAnimalOnSpace(position).get(),maxEnergy));
                    gc.fillOval(i*CELL_SIZE,j*CELL_SIZE,CELL_SIZE,CELL_SIZE);
                } else if (map.getPlantOnSpace(position).isPresent()){
                    gc.setFill(ColorProvider.getPlantColor());
                    gc.fillOval(i*CELL_SIZE,j*CELL_SIZE,CELL_SIZE,CELL_SIZE);
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
