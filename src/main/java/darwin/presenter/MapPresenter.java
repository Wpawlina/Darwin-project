package darwin.presenter;

import darwin.Simulation;
import darwin.model.AbstractMap;
import darwin.model.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MapPresenter implements MapChangeListener {

    @FXML
    private GridPane gridPane;

    @FXML
    private Button startButton;

    @FXML
    private VBox simulationStatistics;

    @FXML
    private VBox animalStatistics;

    private StartButtonState startButtonState=StartButtonState.START;

    private Simulation simulation;

    private AbstractMap map ;

    private Stage stage;

    @FXML
    public void onStartButtonClicked() {
        switch (this.startButtonState)
        {
            case START -> {
                this.startButtonState = StartButtonState.STOP;
                this.startButton.setText(StartButtonState.STOP.getText());
                this.simulation.setRunning(true);
            }
            case  STOP -> {
                this.startButtonState = StartButtonState.START;
                this.startButton.setText(StartButtonState.START.getText());
                this.simulation.setRunning(false);

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

    public void setMap(AbstractMap map) {
        this.map = map;
    }





}
