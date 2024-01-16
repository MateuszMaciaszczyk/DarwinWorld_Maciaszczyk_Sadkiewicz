package world.presenter;

import java.util.List;

public class SimulationStatistics {
    private final Simulation simulation;
    private int day = 0;
    private int animalAmount = 0;
    private int grassAmount = 0;
    private int freeSpace = 0;
    private int averageEnergy = 0;
    private int averageLifeLength = 0;
    private double averageChildrenNumber = 0.0;
    private int[] popularGenes;

    public SimulationStatistics(Simulation simulation) {
        this.simulation = simulation;
        this.animalAmount = simulation.getAnimalAmount();
        this.grassAmount = simulation.getGrassAmount();
        this.freeSpace = simulation.getFreeSpace();
        this.averageEnergy = simulation.getAverageEnergy();
        this.averageLifeLength = simulation.getAverageLifeLength();
        this.averageChildrenNumber = simulation.getAverageChildrenNumber();
        this.popularGenes = simulation.getPopularGenes();
    }

    public void updateStatistics(Simulation simulation, WorldMap map, List<Animal> animals, List<Animal> deadAnimals, List<Animal> childs){
        this.animals = animals;
        this.deadAnimals = deadAnimals;
        this.childs = childs;
        this.simulation = simulation;
        this.map = map;
        this.day = simulation.getDay();
        this.animalAmount = animals.size();
        this.grassAmount = map.getGrassesNumber();
        this.freeSpace = map.getFreeSpaceNumber();
        this.averageEnergy = getAverageEnergy();
        this.averageLifeLength = getAverageLifeLength();
        this.averageChildrenNumber = getAverageChildrenNumber();
        this.popularGenes = getPopularGenes();
    }

    public int getDay() {
        return day;
    }

    public int getAnimalAmount() {
        this.animalAmount = animals.size();
        return animalAmount;
    }

    public int getGrassAmount() {
        this.grassAmount = map.getGrassesNumber();
        return grassAmount;
    }

    public int getFreeSpace() {
        return map.getFreeSpaceNumber();
    }

    public int getAverageEnergy(){
        int sum = 0;
        for (Animal animal : animals) {
            sum += animal.getEnergy();
        }
        if (animals.isEmpty()) {
            return 0;
        }
        if (animals.size() == 0) {
            return 0;
        }
        else return sum / animals.size();
    }

    public int getAverageLifeLength(){
        int sum = 0;
        for (Animal deadAnimal : deadAnimals) {
            sum += deadAnimal.getAge();
        }
        if (deadAnimals.isEmpty()) {
            return 0;
        }
        else return sum / deadAnimals.size();
    }

    public double getAverageChildrenNumber(){
        int sum = 0;
        for (Animal animal : animals) {
            sum += animal.getChilds();
        }
        if (animals.isEmpty()) {
            return 0.0;
        }
        else
            return (double) Math.round(((double) sum / (double) animals.size()) * 100) / 100;
    }

    public int[] getPopularGenes() {
        Map<int[], Integer> genotypeFrequency = new HashMap<>();

        for (Animal animal : animals) {
            int[] genes = animal.getGenes();
            genotypeFrequency.put(genes, genotypeFrequency.getOrDefault(genes, 0) + 1);
        }

        int[] mostPopularGenotype = new int[0];
        int maxFrequency = 0;
        for (Map.Entry<int[], Integer> entry : genotypeFrequency.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostPopularGenotype = entry.getKey();
            }
        }
        return mostPopularGenotype;
    }
}
