package world.presenter;

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

    private void updateAnimalInfo() {
        genomeLabel.setText(Arrays.toString(selectedAnimal.getGenes()));
        activeGeneLabel.setText(Integer.toString(selectedAnimal.getGenes()[selectedAnimal.getPointer()]));
        energyLabel.setText(Integer.toString(selectedAnimal.getEnergy()));
        eatenPlantsLabel.setText(Integer.toString(selectedAnimal.getEatenPlants()));
        childrenCountLabel.setText(Integer.toString(selectedAnimal.getChilds()));
        descendantsCountLabel.setText(Integer.toString(selectedAnimal.getOffspring()));
        lifeLengthLabel.setText(Integer.toString(selectedAnimal.getAge()));
        deathDayLabel.setText(Integer.toString(selectedAnimal.getOffspring()));
    }
}