package world.presenter;
import world.basic.Vector2d;
import world.entities.WorldElement;
import world.maps.Boundary;
import world.maps.MapChangeListener;
import world.maps.WorldMap;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.geometry.Pos;


public class SimulationPresenter implements MapChangeListener {
    private WorldMap worldMap;
    private int animalsAmount;
    private int spawningPlantsAmount;
    private int animalEnergy;
    private int plantEnergy;
    private int reproduceReady;
    private int reproduceEnergyCost;
    private int minGeneMutation;
    private int maxGeneMutation;
    private int genomeLength;
    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
    }
    private int day = 0;
    @FXML
    private Button startButton;
    @FXML
    private Button startStopButton;
    @FXML
    private TextField mapWidthField;
    @FXML
    private TextField mapHeightField;
    @FXML
    private GridPane mapGrid;

    public void setAnimalsAmount(int amount) {
        this.animalsAmount = amount;
    }

    public void setSpawningPlantsAmount(int spawningPlantsAmount) {
        this.spawningPlantsAmount = spawningPlantsAmount;
    }

    public void setAnimalEnergy(int animalEnergy) {
        this.animalEnergy = animalEnergy;
    }


    public void setReproduceReady(int reproduceReady) {
        this.reproduceReady = reproduceReady;
    }

    public void setReproduceEnergyCost(int reproduceEnergyCost) {
        this.reproduceEnergyCost = reproduceEnergyCost;
    }

    public void setMinGeneMutation(int minGeneMutation) {
        this.minGeneMutation = minGeneMutation;
    }

    public void setMaxGeneMutation(int maxGeneMutation) {
        this.maxGeneMutation = maxGeneMutation;
    }

    public void setGenomeLength(int genomeLength) {
        this.genomeLength = genomeLength;
    }

    public void setMap(WorldMap map) {
        this.worldMap = map;
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @FXML
    public void drawMap() {
        clearGrid();
        Boundary boundary = worldMap.getCurrentBounds();
        drawGrid(boundary);
    }

    private void drawGrid(Boundary boundary) {
        for (int i = boundary.lowerLeft().getY(); i <= boundary.upperRight().getY(); i++) {
            for (int j = boundary.lowerLeft().getX(); j <= boundary.upperRight().getX(); j++) {
                Vector2d position = new Vector2d(j, i);
                drawGridCell(position, j - boundary.lowerLeft().getX() + 1, boundary.upperRight().getY() - i + 1);
            }
        }
    }

    private void drawGridCell(Vector2d position, int column, int row) {
        WorldElement element = worldMap.objectAt(position);
        Label label = createLabelForElement(element);
        mapGrid.add(label, column, row);
    }

    private Label createLabelForElement(WorldElement element) {
        Label label;
        if (element != null) {
            label = new Label(element.toString());
        } else {
            label = new Label(" ");
        }
        label.setMinWidth(50);
        label.setMinHeight(50);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            day++;
        });
    }

    @FXML
    public void onStartStopClicked() {  //TODO add stop button
        Simulation simulation = new Simulation(animalsAmount, worldMap, animalEnergy, genomeLength, reproduceReady, reproduceEnergyCost, minGeneMutation, maxGeneMutation, spawningPlantsAmount);
        SimulationEngine simulationEngine = new SimulationEngine(new ArrayList<>(List.of(simulation)));
        simulationEngine.runAsync();
    }
}