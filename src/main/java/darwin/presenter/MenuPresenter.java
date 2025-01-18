package darwin.presenter;

import darwin.MapApplication;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class MenuPresenter {

    @FXML
    protected Pane pane;
    @FXML
    public TextField formMapHeight;
    @FXML
    public TextField formMapWidth;
    @FXML
    public TextField formPlantStart;
    @FXML
    public TextField formSpawnPlantsPerDay;
    @FXML
    public TextField formPlantEnergy;
    @FXML
    public TextField formAnimalStart;
    @FXML
    public TextField formAnimalStartEnergy;
    @FXML
    public TextField formAnimalEnergyReproductionDepletion;
    @FXML
    public TextField formAnimalEnergyDailyDepletion;
    @FXML
    public TextField formAnimalEnergyToReproduce;
    @FXML
    public TextField formAnimalMutationMinimum;
    @FXML
    public TextField formAnimalMutationMaximum;
    @FXML
    public TextField formAnimalGenotypeLength;
    @FXML
    public CheckBox formVariantMap;
    @FXML
    public CheckBox formVariantAnimal;
    @FXML
    public CheckBox formExportStatistics;
    @FXML
    public Button createGame;
    @FXML
    public Button exportConfiguration;
    @FXML
    public Button importConfiguration;

    @FXML
    public void initialize(){
        this.createGame.setOnAction(event -> Application.launch(MapApplication.class, gatheredArguments()));
        //this.exportConfiguration.setOnAction();
        //this.importConfiguration.setOnAction();
    }

    private String gatheredArguments(){
        return formMapHeight.getText() +
                formMapWidth.getText() +
                formVariantMap.isSelected() +
                formSpawnPlantsPerDay.getText() +
                formPlantStart.getText() +
                formPlantEnergy.getText() +
                formAnimalStart.getText() +
                formAnimalStartEnergy.getText() +
                formAnimalEnergyToReproduce.getText() +
                formAnimalEnergyReproductionDepletion.getText() +
                formAnimalMutationMinimum.getText() +
                formAnimalMutationMaximum.getText() +
                formVariantAnimal.isSelected() +
                formAnimalGenotypeLength.getText();
    }

}
