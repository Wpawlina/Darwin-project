package darwin.presenter;

import darwin.CSVWriter;
import darwin.MenuApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;

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
        File conf = new File("conf.csv");
        try {
            conf.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CSVWriter confWriter = new CSVWriter(conf);




        this.createGame.setOnAction(event -> {
            Platform.runLater(() -> {
                app.openMapStage(this.gatheredArguments());
            });
        });
        this.exportConfiguration.setOnAction(event -> confWriter.writeLine(gatheredArguments()));
        this.importConfiguration.setOnAction(event -> {
            String[] confArr = confWriter.readLine();
            if (confArr.length == 15) setArguments(confArr);
        });
    }

    public void   setApp(MenuApplication app){
        this.app = app;
    }

    private String[] gatheredArguments(){
        return new String[] {
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
                formGenomeLength.getText(),
                String.valueOf(formExportStatistics.isSelected())
        };
    }

    private void setArguments(String[] args){
        formMapHeight.setText(args[0]);
        formMapWidth.setText(args[1]);
        formOptionE.setSelected(Boolean.parseBoolean(args[2]));
        formSpawnPlantPerDay.setText(args[3]);
        formInitialPlantSpawn.setText(args[4]);
        formPlantEnergy.setText(args[5]);
        formInitialAnimalSpawn.setText(args[6]);
        formInitialAnimalEnergy.setText(args[7]);
        formReproductionEnergySufficient.setText(args[8]);
        formReproductionEnergyCost.setText(args[9]);
        formMinMutation.setText(args[10]);
        formMaxMutation.setText(args[11]);
        formCrazy.setSelected(Boolean.parseBoolean(args[12]));
        formGenomeLength.setText(args[13]);
        formExportStatistics.setSelected(Boolean.parseBoolean(args[14]));
    }

}
