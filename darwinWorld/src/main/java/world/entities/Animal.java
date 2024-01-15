package world.entities;

import world.basic.MapDirection;
import world.maps.MoveValidator;
import world.basic.Vector2d;


public class Animal implements WorldElement {
    private Vector2d position;
    private int orientation;
    private int[] genes;
    private int energy;
    private int pointer;  // pointer to the current gene

    public int getOrientation() {
        return orientation;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public Vector2d getNextMove() {
        return this.position.add(MapDirection.toUnitVector(getNextOrientation()));
    }

    public void iteratePointer(){
        pointer = (pointer + 1) % genes.length;
    }

    public int getNextOrientation(){
        int direction = genes[pointer];
        return (orientation + direction) % 8;
    }

    @Override
    public String toString() {
        return String.valueOf(energy);
    }

    public void move(MoveValidator validator) {
        this.position = getNextMove();
        this.orientation = getNextOrientation();
        pointer = (pointer + 1) % genes.length;
        energy--;
    }

    public void gainEnergy(int energy) {
        this.energy += energy;
    }

    public boolean isAt(Vector2d position) { return this.position.equals(position); }

    public Vector2d getPosition() { return position; }

    public int[] getGenes() {
        return genes;
    }

    public boolean canBreed() {
        return energy >= energyToReproduce;
    }

    public void updateChilds() {
        this.childs++;
    }

    public Animal breed(Animal other) {
        int childEnergy = 2 * reproductionEnergyCost;
        this.energy -= reproductionEnergyCost;
        other.energy -= reproductionEnergyCost;

        int[] parent1Genes = this.getGenes();
        int[] parent2Genes = other.getGenes();
        int[] childGenes = new int[parent1Genes.length];

        double ratio = (double) this.energy / (this.energy + other.energy);
        int cutoff = (int) (ratio * parent1Genes.length);

        System.arraycopy(parent1Genes, 0, childGenes, 0, cutoff);
        System.arraycopy(parent2Genes, cutoff, childGenes, cutoff, parent1Genes.length - cutoff);

        // Mutate some genes
        childGenes = randomGenes(childGenes);
        //childGenes = swapGenes(childGenes);

        return new Animal(new Vector2d(0, 0), childEnergy, childGenes, reproductionEnergyCost, energyToReproduce, minGeneMutation, maxGeneMutation);
    }

    public void decreaseEnergy(int n) {
        energy -= n;
    }

    private int[] randomGenes(int[] childGenes) {
        int numberOfMutations = (int)(Math.random() * (maxGeneMutation - minGeneMutation + 1)) + minGeneMutation;
        for (int i = 0; i < numberOfMutations; i++) {
            int index = (int)(Math.random() * childGenes.length);
            childGenes[index] = (int)(Math.random() * 8);
        }
        return childGenes;
    }

    private int[] swapGenes(int[] childGenes) {
        int numberOfGene1 = (int)(Math.random() * childGenes.length);
        int numberOfGene2 = (int)(Math.random() * childGenes.length);
        int temp = childGenes[numberOfGene1];
        childGenes[numberOfGene1] = childGenes[numberOfGene2];
        childGenes[numberOfGene2] = temp;
        return childGenes;
    }
}
