package world.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import world.maps.HellsGate;

import java.io.IOException;

public class StartPresenter {
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField initialPlantNumberField;
    @FXML
    private TextField numberOfAnimalsField;
    @FXML
    private TextField startingAnimalEnergyField;
    @FXML
    private TextField numberOfSpawningPlantField;
    @FXML
    private TextField plantEnergyField;
    @FXML
    private TextField reproduceReadyField;
    @FXML
    private TextField reproduceEnergyCostField;
    @FXML
    private TextField minGeneMutation;
    @FXML
    private TextField maxGeneMutation;
    @FXML
    private TextField genomeLength;
    @FXML
    private Button startButton;


    private int parseTextFieldToInt(TextField widthField) {
        return Integer.parseInt(widthField.getText());
    }

    @FXML
    public void onStartClicked() {
        try {
        int width = parseTextFieldToInt(widthField);
        int height = parseTextFieldToInt(heightField);
        int plantNumber = parseTextFieldToInt(initialPlantNumberField);
        int animalsNumber = parseTextFieldToInt(numberOfAnimalsField);
        int startingAnimalEnergy = parseTextFieldToInt(startingAnimalEnergyField);
        int numberOfSpawningPlant = parseTextFieldToInt(numberOfSpawningPlantField);
        int plantEnergy = parseTextFieldToInt(plantEnergyField);
        int reproduceReady = parseTextFieldToInt(reproduceReadyField);
        int reproduceEnergyCost = parseTextFieldToInt(reproduceEnergyCostField);
        int minGeneMutation = parseTextFieldToInt(this.minGeneMutation);
        int maxGeneMutation = parseTextFieldToInt(this.maxGeneMutation);
        int genomeLength = parseTextFieldToInt(this.genomeLength);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
        Parent root = loader.load();
        SimulationPresenter simulationPresenter = loader.getController();

        // TODO - add map variant choice
        HellsGate hellsGate = new HellsGate(width, height, plantNumber, reproduceEnergyCost, plantEnergy);
        hellsGate.addMapChangeListener(simulationPresenter);

        simulationPresenter.setAnimalsAmount(animalsNumber);
        simulationPresenter.setMap(hellsGate);
        simulationPresenter.setAnimalEnergy(startingAnimalEnergy);
        simulationPresenter.setSpawningPlantsAmount(numberOfSpawningPlant);
        simulationPresenter.setReproduceReady(reproduceReady);
        simulationPresenter.setReproduceEnergyCost(reproduceEnergyCost);
        simulationPresenter.setMinGeneMutation(minGeneMutation);
        simulationPresenter.setMaxGeneMutation(maxGeneMutation);
        simulationPresenter.setGenomeLength(genomeLength);
        simulationPresenter.onStartStopClicked();

        Stage simulationStage = new Stage();
        simulationStage.setScene(new Scene(root));
        simulationStage.show();
    }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
