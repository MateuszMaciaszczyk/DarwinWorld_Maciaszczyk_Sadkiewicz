package world.presenter;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import world.maps.Globe;
import world.maps.HellsGate;

import java.io.IOException;

public class StartPresenter {
    Configuration configuration;
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
    private TextField simulationLength;
    @FXML
    private ChoiceBox MapVariant;
    @FXML
    private ChoiceBox MutationVariant;
    @FXML
    private Button startButton;
    @FXML
    private CheckBox generateCsvCheckBox;
    @FXML
    private TextField configNameField;
    @FXML
    private Button loadConfigButton;
    @FXML
    private Button saveConfigButton;

    @FXML
    public void initialize() {
        startButton.setDisable(true);
        this.MapVariant.setItems(FXCollections.observableArrayList("HellsGate", "Globe"));
        this.MutationVariant.setItems(FXCollections.observableArrayList("Random Mutation", "Swap 2 Genes"));
        TextField[] fields = {widthField, heightField, initialPlantNumberField, numberOfAnimalsField, startingAnimalEnergyField, numberOfSpawningPlantField, plantEnergyField, reproduceReadyField, reproduceEnergyCostField, minGeneMutation, maxGeneMutation, genomeLength};
        for (TextField field : fields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> startButton.setDisable(validateInputFields()));
        }
        ChoiceBox[] choiceBoxes = {MapVariant, MutationVariant};
        for (ChoiceBox choiceBox : choiceBoxes) {
            choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> startButton.setDisable(validateInputFields()));
        }
    }

    public void onSaveConfigClicked() {
        String configName = configNameField.getText();
        if (configName.isEmpty()) {
            System.out.println("Please enter a configuration name.");
            return;
        }

        configuration = new Configuration(configName + ".properties");
        configuration.setProperty("width", widthField.getText());
        configuration.setProperty("height", heightField.getText());
        configuration.setProperty("initialPlantNumber", initialPlantNumberField.getText());
        configuration.setProperty("animalsAmount", numberOfAnimalsField.getText());
        configuration.setProperty("startingAnimalEnergy", startingAnimalEnergyField.getText());
        configuration.setProperty("spawningPlantsAmount", numberOfSpawningPlantField.getText());
        configuration.setProperty("plantEnergy", plantEnergyField.getText());
        configuration.setProperty("reproduceReady", reproduceReadyField.getText());
        configuration.setProperty("reproduceEnergyCost", reproduceEnergyCostField.getText());
        configuration.setProperty("minGeneMutation", minGeneMutation.getText());
        configuration.setProperty("maxGeneMutation", maxGeneMutation.getText());
        configuration.setProperty("genomeLength", genomeLength.getText());
        configuration.setProperty("simulationLength", simulationLength.getText());
        configuration.setProperty("mapVariant", (String) MapVariant.getValue());
        configuration.setProperty("mutationVariant", (String) MutationVariant.getValue());

        try {
            configuration.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onLoadConfigClicked() {
        String configName = configNameField.getText();
        if (configName.isEmpty()) {
            System.out.println("Please enter a configuration name.");
            return;
        }

        configuration = new Configuration(configName + ".properties");
        try {
            configuration.load();
        } catch (IOException e) {
            System.out.println("Failed to load configuration: " + e.getMessage());
            return;
        }

        widthField.setText(configuration.getProperty("width"));
        heightField.setText(configuration.getProperty("height"));
        initialPlantNumberField.setText(configuration.getProperty("initialPlantNumber"));
        numberOfAnimalsField.setText(configuration.getProperty("animalsAmount"));
        startingAnimalEnergyField.setText(configuration.getProperty("startingAnimalEnergy"));
        numberOfSpawningPlantField.setText(configuration.getProperty("spawningPlantsAmount"));
        plantEnergyField.setText(configuration.getProperty("plantEnergy"));
        reproduceReadyField.setText(configuration.getProperty("reproduceReady"));
        reproduceEnergyCostField.setText(configuration.getProperty("reproduceEnergyCost"));
        minGeneMutation.setText(configuration.getProperty("minGeneMutation"));
        maxGeneMutation.setText(configuration.getProperty("maxGeneMutation"));
        genomeLength.setText(configuration.getProperty("genomeLength"));
        simulationLength.setText(configuration.getProperty("simulationLength"));
        MapVariant.setValue(configuration.getProperty("mapVariant"));
        MutationVariant.setValue(configuration.getProperty("mutationVariant"));
    }

    private boolean validateInputFields() {
        TextField[] fields = {widthField, heightField, initialPlantNumberField, numberOfAnimalsField, startingAnimalEnergyField, numberOfSpawningPlantField, plantEnergyField, reproduceReadyField, reproduceEnergyCostField, minGeneMutation, maxGeneMutation, genomeLength};

        for (TextField field : fields) {
            String text = field.getText();
            if (text == null || text.isEmpty()) {
                return true;
            }

            try {
                int value = Integer.parseInt(text);
                if (value <= 0) {
                    return true;
                }
            } catch (NumberFormatException e) {
                return true;
            }
        }
        if (MapVariant.getValue() == null) {
            return true;
        }

        // Check if a mutation variant is selected
        return MutationVariant.getValue() == null;
    }

    private int parseTextFieldToInt(TextField widthField) {
        return Integer.parseInt(widthField.getText());
    }

    @FXML
   public void onStartClicked() {
        try {
            String mapVariant = (String) this.MapVariant.getValue();
            String mutationVariant = (String) this.MutationVariant.getValue();
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
            int simulationLength = parseTextFieldToInt(this.simulationLength);
            boolean saveStatistics = generateCsvCheckBox.isSelected();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
            Parent root = loader.load();
            SimulationPresenter simulationPresenter = loader.getController();

            if (mapVariant.equals("HellsGate")) {
                HellsGate hellsGate = new HellsGate(width, height, plantNumber, reproduceEnergyCost, plantEnergy);
                simulationPresenter.setMap(hellsGate);
                hellsGate.addMapChangeListener(simulationPresenter);
            }
            else {
                Globe globe = new Globe(width, height, plantNumber, reproduceEnergyCost, plantEnergy);
                simulationPresenter.setMap(globe);
                globe.addMapChangeListener(simulationPresenter);
            }

            simulationPresenter.setAnimalsAmount(animalsNumber);
            simulationPresenter.setAnimalEnergy(startingAnimalEnergy);
            simulationPresenter.setSpawningPlantsAmount(numberOfSpawningPlant);
            simulationPresenter.setReproduceReady(reproduceReady);
            simulationPresenter.setReproduceEnergyCost(reproduceEnergyCost);
            simulationPresenter.setMinGeneMutation(minGeneMutation);
            simulationPresenter.setMaxGeneMutation(maxGeneMutation);
            simulationPresenter.setGenomeLength(genomeLength);
            simulationPresenter.setMutationVariant(mutationVariant);
            simulationPresenter.setSaveStatistics(saveStatistics);
            simulationPresenter.setSimulationLength(simulationLength);
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
