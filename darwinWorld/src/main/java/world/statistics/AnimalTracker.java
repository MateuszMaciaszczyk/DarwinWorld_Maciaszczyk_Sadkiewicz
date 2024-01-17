package world.statistics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import world.entities.Animal;

import java.util.Arrays;

public class AnimalTracker {
    private Animal selectedAnimal;
    private Label genomeLabel;
    private Label activeGeneLabel;
    private Label energyLabel;
    private Label eatenPlantsLabel;
    private Label childrenCountLabel;
    private Label descendantsCountLabel;
    private Label lifeLengthLabel;
    private Label deathDayLabel;

    public AnimalTracker(Label genomeLabel, Label activeGeneLabel, Label energyLabel, Label eatenPlantsLabel, Label childrenCountLabel, Label descendantsCountLabel, Label lifeLengthLabel, Label deathDayLabel) {
        this.genomeLabel = genomeLabel;
        this.activeGeneLabel = activeGeneLabel;
        this.energyLabel = energyLabel;
        this.eatenPlantsLabel = eatenPlantsLabel;
        this.childrenCountLabel = childrenCountLabel;
        this.descendantsCountLabel = descendantsCountLabel;
        this.lifeLengthLabel = lifeLengthLabel;
        this.deathDayLabel = deathDayLabel;
    }

    public void selectAnimal(Animal animal) {
        this.selectedAnimal = animal;
        updateAnimalInfo();
    }

    public void updateAnimalInfo() {
        genomeLabel.setText("Gene " + Arrays.toString(selectedAnimal.getGenes()));
        activeGeneLabel.setText("Active gene " + selectedAnimal.getGenes()[selectedAnimal.getPointer()]);
        energyLabel.setText("Energy level " + selectedAnimal.getEnergy());
        eatenPlantsLabel.setText("Eaten plants " + selectedAnimal.getEatenPlants());
        childrenCountLabel.setText("Number of kids " + selectedAnimal.getChilds());
        descendantsCountLabel.setText("Offsprings " + selectedAnimal.getOffspring());  // TODO check if working correctly
        lifeLengthLabel.setText("Age " + selectedAnimal.getAge());
    }
}
