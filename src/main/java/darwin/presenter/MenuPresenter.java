package darwin.presenter;

import darwin.MapApplication;
import darwin.MenuApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.lang.reflect.Array;

public class MenuPresenter {

    @FXML
    protected Pane pane;
    @FXML
    public TextField formMapHeight;
    @FXML
    public TextField formMapWidth;
    @FXML
    public TextField formInitialPlantSpawn;
    @FXML
    public TextField formSpawnPlantPerDay;
    @FXML
    public TextField formPlantEnergy;
    @FXML
    public TextField formInitialAnimalSpawn;
    @FXML
    public TextField formInitialAnimalEnergy;
    @FXML
    public TextField formReproductionEnergyCost;
    @FXML
    public TextField formReproductionEnergySufficient;
    @FXML
    public TextField formMinMutation;
    @FXML
    public TextField formMaxMutation;
    @FXML
    public TextField formGenomeLength;
    @FXML
    public CheckBox formOptionE;
    @FXML
    public CheckBox formCrazy;
    @FXML
    public CheckBox formExportStatistics;
    @FXML
    public Button createGame;
    @FXML
    public Button exportConfiguration;
    @FXML
    public Button importConfiguration;

    private MenuApplication app;

    @FXML
    public void initialize(){
        this.createGame.setOnAction(event -> {
            Platform.runLater(() -> {
                app.openMapStage();
            });
        });
        this.exportConfiguration.setOnAction(event -> {});
        this.importConfiguration.setOnAction(event -> {});
    }

    public void   setApp(MenuApplication app){
        this.app = app;
    }

    private String[] gatheredArguments(){
        String[] args = {
                formMapHeight.getText(),
                formMapWidth.getText(),
                String.valueOf(formOptionE.isSelected()),
                formSpawnPlantPerDay.getText(),
                formInitialPlantSpawn.getText(),
                formPlantEnergy.getText(),
                formInitialAnimalSpawn.getText(),
                formInitialAnimalEnergy.getText(),
                formReproductionEnergySufficient.getText(),
                formReproductionEnergyCost.getText(),
                formMinMutation.getText(),
                formMaxMutation.getText(),
                String.valueOf(formCrazy.isSelected()),
                formGenomeLength.getText()
        };
        return args;
    }

}
