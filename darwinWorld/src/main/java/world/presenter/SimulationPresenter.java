package world.presenter;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import world.basic.Vector2d;
import world.entities.Animal;
import world.entities.Grass;
import world.entities.WorldElement;
import world.maps.Boundary;
import world.maps.MapChangeListener;
import world.maps.WorldMap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Simulation simulation;
    private SimulationEngine simulationEngine;
    private SimulationStatistics stats;
    private int animalsAmount;
    private int spawningPlantsAmount;
    private int animalEnergy;
    private int plantEnergy;
    private int reproduceReady;
    private int reproduceEnergyCost;
    private int minGeneMutation;
    private int maxGeneMutation;
    private int genomeLength;
    private String mutationVariant;
    private boolean saveStatistics;

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
    @FXML
    private Label animalCountLabel;
    @FXML
    private Label grassCountLabel;
    @FXML
    private Label freeSpaceCountLabel;
    @FXML
    private Label mostPopularGeneLabel;
    @FXML
    private Label averageEnergyLabel;
    @FXML
    private Label averageLifeLengthLabel;
    @FXML
    private Label averageChildrenCountLabel;
    @FXML
    private Label dayLabel;

    public void setSaveStatistics(boolean saveStatistics) {
        this.saveStatistics = saveStatistics;
    }

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

    public void setMutationVariant(String mutationVariant) {
        this.mutationVariant = mutationVariant;
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
        boolean hasGrass = worldMap.hasGrassAt(position);
        Node node = createNodeForElement(element, hasGrass);
        mapGrid.add(node, column, row);
    }

    private Node createNodeForElement(WorldElement element, boolean hasGrass) {
        Label label = new Label();
        label.setMinWidth(20);
        label.setMinHeight(20);
        label.setAlignment(Pos.CENTER);

        if (element != null) {
            if (element instanceof Grass) { // Assuming * represents grass
                label.setStyle("-fx-background-color: green;");
            } else { // Assuming this is an animal
                Animal animal = (Animal) element;
                int energy = animal.getEnergy();
                Color color;
                if (energy <= 5) {
                    color = Color.BEIGE;
                } else if (energy <= 10) {
                    color = Color.BURLYWOOD;
                } else if (energy <= 20) {
                    color = Color.BROWN;
                } else {
                    color = Color.BLACK;
                }

                Circle circle = new Circle();
                circle.setRadius(5); // Set the radius to half of the cell size
                circle.setFill(color); // Set the color to the calculated color
                label.setGraphic(circle);
                if (hasGrass) {
                    label.setStyle("-fx-background-color: green;");
                }
                else {
                    label.setStyle("-fx-background-color: lightgreen;");
                }
            }
        } else {
            label.setStyle("-fx-background-color: lightgreen;");
        }
        return label;
    }

    @Override
    public void mapChanged(WorldMap worldMap) {
        Platform.runLater(() -> {
            drawMap();
            updateStatistics();
        });
    }

    private void updateStatistics() {
        stats.updateStatistics(simulation, worldMap, simulation.getAnimals(), simulation.getDeadAnimals(), simulation.getChilds());
        dayLabel.setText("Day: " + stats.getDay());
        animalCountLabel.setText("Animal count: " + stats.getAnimalAmount());
        grassCountLabel.setText("Grass count: " + stats.getGrassAmount());
        freeSpaceCountLabel.setText("Free space count: " + stats.getFreeSpace());
        mostPopularGeneLabel.setText("Most popular gene: " + Arrays.toString(stats.getPopularGenes()));
        averageEnergyLabel.setText("Average energy: " + stats.getAverageEnergy());
        averageLifeLengthLabel.setText("Average life length: " + stats.getAverageLifeLength());
        averageChildrenCountLabel.setText("Average children count: " + stats.getAverageChildrenNumber());

        if (saveStatistics) {
            saveStatisticsToCSV();
        }
    }

    private void saveStatisticsToCSV() {
        try (FileWriter writer = new FileWriter("statistics.csv", true)) {
            writer.append(stats.getDay() + ",");
            writer.append(stats.getAnimalAmount() + ",");
            writer.append(stats.getGrassAmount() + ",");
            writer.append(stats.getFreeSpace() + ",");
            writer.append(Arrays.toString(stats.getPopularGenes()) + ",");
            writer.append(stats.getAverageEnergy() + ",");
            writer.append(stats.getAverageLifeLength() + ",");
            writer.append(stats.getAverageChildrenNumber() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onStartStopClicked() {
        try {
            if (simulationEngine == null) {
                this.simulation = new Simulation(animalsAmount, worldMap, animalEnergy, genomeLength, reproduceReady, reproduceEnergyCost, minGeneMutation, maxGeneMutation, spawningPlantsAmount, mutationVariant);
                simulationEngine = new SimulationEngine(new ArrayList<>());
                simulationEngine.getSimulations().add(simulation);
                stats = new SimulationStatistics(simulation, worldMap, simulation.getAnimals(), simulation.getDeadAnimals(), simulation.getChilds());
                simulationEngine.runAsync();
                startStopButton.setText("Stop");
            }
            else if (simulationEngine.isRunning()) {
                simulationEngine.stop();
                startStopButton.setText("Start");
            }
            else {
                simulationEngine.start();
                startStopButton.setText("Stop");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
