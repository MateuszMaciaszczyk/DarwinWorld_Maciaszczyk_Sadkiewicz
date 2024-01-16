package world.entities;

import world.basic.MapDirection;
import world.maps.MoveValidator;
import world.basic.Vector2d;

import java.util.ArrayList;
import java.util.List;


public class Animal implements WorldElement {
    private Vector2d position;
    private int orientation;
    private int[] genes;
    private int energy;
    private int pointer;  // pointer to the current gene
    private int reproductionEnergyCost;
    private int energyToReproduce;
    private int minGeneMutation;
    private int maxGeneMutation;
    private String mutationVariant;
    private boolean alive = true;
    private int age = 0;
    private int childs = 0;
    private int offspring = 0;
    private int death = 0;
    List<Animal> parents = new ArrayList<>();

    // constructor for initial animals
    public Animal(Vector2d position, int energy, int genesNumber, int reproductionEnergyCost, int energyToReproduce, String mutationVariant) {
        this.position = position;
        this.energy = energy;
        this.orientation = (int)(Math.random() * 8);
        this.pointer = 0;
        this.reproductionEnergyCost = reproductionEnergyCost;
        this.energyToReproduce = energyToReproduce;
        this.genes = randomGenes(genesNumber);
        this.mutationVariant = mutationVariant;
    }

    // constructor for children
    public Animal(Vector2d position, int energy, int[] genes, int reproductionEnergyCost, int energyToReproduce, int minGeneMutation, int maxGeneMutation, String mutationVariant) {
        this.position = position;
        this.energy = energy;
        this.orientation = (int)(Math.random() * 8);
        this.pointer = (int)(Math.random() * genes.length);
        this.reproductionEnergyCost = reproductionEnergyCost;
        this.energyToReproduce = energyToReproduce;
        this.genes = genes;
        this.minGeneMutation = minGeneMutation;
        this.maxGeneMutation = maxGeneMutation;
        this.mutationVariant = mutationVariant;
    }

    private int[] randomGenes(int n){
        int[] genes = new int[n];
        for(int i = 0; i < n; i++){
            genes[i] = (int)(Math.random() * 8);
        }
        return genes;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public int getAge() {
        return age;
    }

    public int getChilds() {
        return childs;
    }

    public int getOffspring() {
        return offspring;
    }

    public void setParents(Animal parent1, Animal parent2) {
        this.parents.add(parent1);
        this.parents.add(parent2);
    }

    public void updateOffspring() {
        offspring++;
        if (!parents.isEmpty()) {
            parents.get(0).updateOffspring();
            parents.get(1).updateOffspring();
        }
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

    public int getEnergy() {
        return energy;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        this.alive = false;
        this.death = this.age;
    }

    public void move(MoveValidator validator) {
        this.position = getNextMove();
        this.orientation = getNextOrientation();
        pointer = (pointer + 1) % genes.length;
        energy--;
        age++;
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

        if (mutationVariant.equals("Random Mutation"))
            childGenes = randomGenes(childGenes);
        else
            childGenes = swapGenes(childGenes);

        return new Animal(new Vector2d(0, 0), childEnergy, childGenes, reproductionEnergyCost, energyToReproduce, minGeneMutation, maxGeneMutation, mutationVariant);
    }

    public void decreaseEnergy(int n) {
        if (energy != 0) {
            energy -= n;
        }
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
